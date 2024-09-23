package moheng.applicationrunner.data.dev;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.applicationrunner.data.dto.TripRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Order(1)
@Component
public class TripDevApplicationRunner implements ApplicationRunner {
    private final JdbcTemplate jdbcTemplate;

    public TripDevApplicationRunner(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String countQuery = "SELECT COUNT(*) FROM trip";
        Long count = jdbcTemplate.queryForObject(countQuery, Long.class);

        if (count == 0) {
            final Resource resource1 = new ClassPathResource("json/trip1.json");
            final Resource resource2 = new ClassPathResource("json/trip2.json");
            final ObjectMapper objectMapper = new ObjectMapper();

            final List<TripRunner> tripRunners = new ArrayList<>();
            tripRunners.addAll(findTripRunnersByResource(resource1, objectMapper));
            tripRunners.addAll(findTripRunnersByResource(resource2, objectMapper));

            String sql = "INSERT INTO trip (name, place_name, content_id, description, trip_image_url, visited_count, coordinate_x, coordinate_y, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            List<Object[]> batchArgs = new ArrayList<>();
            for (final TripRunner tripRunner : tripRunners) {
                batchArgs.add(new Object[]{
                        tripRunner.getTitle(),
                        tripRunner.getAddr1(),
                        tripRunner.getContentid(),
                        tripRunner.getOverview(),
                        tripRunner.getFirstimage(),
                        0L,
                        tripRunner.getMapx(),
                        tripRunner.getMapy(),
                        LocalDate.now(),
                        LocalDate.now()
                });
            }

            jdbcTemplate.batchUpdate(sql, batchArgs);
        }
    }

    private List<TripRunner> findTripRunnersByResource(final Resource resource, final ObjectMapper objectMapper) throws IOException {
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<TripRunner>>() {});
    }
}