package was.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
public class Setting {
    private int port;
    private int numTreads;
    private String index;
    private List<Server> servers;
    private List<Mapping> mappings;

    public Setting(int port, int numTreads, String index,
                        List<Server> servers, List<Mapping> mappings) {
        this.port = port;
        this.numTreads = numTreads;
        this.index = index;
        this.servers = servers;
        this.mappings = mappings;
    }
}
