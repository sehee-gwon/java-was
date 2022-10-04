package was.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import was.domain.Setting;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Getter
public class WebConfig {
    private Setting setting;

    public WebConfig() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();

        // 설정 파일 불러오기 (json -> object)
        try (InputStream in = loader.getResourceAsStream("application.json")) {
            String json = IOUtils.toString(in, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);   // 매핑되지 않는 필드 무시
            this.setting = objectMapper.readValue(json, Setting.class);
        } catch (JsonProcessingException e) {
            log.error("Json parsing Error: {}", e.getMessage(), e);
        } catch (IOException e) {
            log.error("File get Error: {}", e.getMessage(), e);
        }
    }
}
