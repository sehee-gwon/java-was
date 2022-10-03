package was.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
public class Setting {
    private int port;
    private int numTreads;
    private String index;
    private List<Server> servers;
    private List<Mapping> mappings;
}
