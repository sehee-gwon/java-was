package was.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
public class Server {
    private String domain;
    private Map<String, String> errors;

    public Server(String domain, Map<String, String> errors) {
        this.domain = domain;
        this.errors = errors;
    }
}
