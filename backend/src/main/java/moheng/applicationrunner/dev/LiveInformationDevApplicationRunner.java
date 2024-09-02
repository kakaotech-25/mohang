package moheng.applicationrunner.dev;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.applicationrunner.dto.LiveInformationRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Order(9)
@Component
public class LiveInformationDevApplicationRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public LiveInformationDevApplicationRunner(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Resource resource = new ClassPathResource("liveinformation.json");
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<LiveInformationRunner> liveInformationRunners = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<LiveInformationRunner>>() {});

        List<String> uniqueNames = liveInformationRunners.stream()
                .flatMap(runner -> runner.getLiveinformation().stream())
                .distinct()
                .filter(name -> !existsByName(name))
                .collect(Collectors.toList());

        if (!uniqueNames.isEmpty()) {
            String sql = "INSERT INTO live_information (name, created_at, updated_at) VALUES (?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            List<Object[]> batchArgs = uniqueNames.stream()
                    .map(name -> new Object[]{name, now, now})
                    .collect(Collectors.toList());

            jdbcTemplate.batchUpdate(sql, batchArgs);
        }
    }

    private boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM live_information WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }
}
