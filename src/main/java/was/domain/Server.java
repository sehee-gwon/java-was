package was.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class Server {
    private String domain;
    private Map<String, String> errors;
}
