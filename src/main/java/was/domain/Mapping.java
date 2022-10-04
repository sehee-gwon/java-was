package was.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
public class Mapping {
    private String name;
    private Map<String, String> router;

    public Mapping(String name, Map<String, String> router) {
        this.name = name;
        this.router = router;
    }
}
