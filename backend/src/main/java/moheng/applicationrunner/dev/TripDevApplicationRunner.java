package moheng.applicationrunner.dev;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.applicationrunner.dto.LiveInformationRunner;
import moheng.applicationrunner.dto.TripRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(10)
@Component
public class TripDevApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Resource resource = new ClassPathResource("triptmp.json");
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<TripRunner> tripRunners = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<TripRunner>>() {});

        for(TripRunner tripRunner : tripRunners) {
            System.out.println(tripRunner);
        }
    }
}
