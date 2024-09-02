package moheng.applicationrunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import moheng.applicationrunner.dto.LiveInformationRunner;
import moheng.liveinformation.domain.LiveInformation;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.print.DocFlavor;
import java.io.FileReader;
import java.util.List;

@Order(9)
@Component
public class TestApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource = new ClassPathResource("liveinformation.json");

        // ObjectMapper를 사용하여 JSON 파일을 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        List<LiveInformationRunner> liveInformationRunners = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<LiveInformationRunner>>() {});


        for(LiveInformationRunner liveInformationRunner : liveInformationRunners) {
            System.out.println(liveInformationRunner.getLiveinformation());
        }
    }
}
