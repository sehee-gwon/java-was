package was.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import was.domain.enumeration.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ToString
@Getter
public class HttpRequest {
    private String host;
    private HttpMethod method;
    private String uri;
    private String version;
    private String file;
    private Map<String, String> parameters = new HashMap<>();

    public void setHost(String host) {
        this.host = host;
    }

    public void parse(InputStream in, String index) {
        List<String> headers = new ArrayList<>();

        // Http header 정보 가져오기 (1, 2줄)
        try {
             BufferedReader br = new BufferedReader(new InputStreamReader(in));
             for (int i=0; i<2; i++) {
                 headers.add(br.readLine());
             }
        } catch (IOException ioe) {
            log.error("Http header get Error: {}", ioe.getMessage(), ioe);
        }

        // 빈 공백을 보내는 경우 때문에 체크 (Postman)
        if (!headers.isEmpty()) {
            String data1 = headers.get(0);
            String data2 = headers.get(1);

            // 첫번째 데이터 (method uri version) => ex) GET /index.html?id=1 HTTP/1.1
            String[] tokens = data1.split("\\s+");
            this.method = HttpMethod.valueOf(tokens[0]);

            if (HttpMethod.GET == method) {
                // 두번째 데이터 (host) => ex) Host: www.a.com
                // Postman 에서 전송시 두 번째에 Host 가 위치하지 않을 수 있으나, 이외에 도메인을 가져올 방법을 못 찾음 (readLine 전체조회시 무한로딩)
                if (data2 != null && data2.startsWith("Host")) {
                    this.host = headers.get(1).split(":")[1].trim();
                }

                // 파일 주소가 없다면 인덱스 파일 설정
                this.uri = tokens[1];
                if (this.uri.endsWith("/")) this.uri = index;

                if (tokens.length > 2) this.version = tokens[2];

                // query string 검사
                if (this.uri.indexOf("?") > -1) {
                    String[] uris = this.uri.split("\\?");
                    this.file = uris[0];  // /index.html
                    String[] queryString = uris[1].split("\\&");  // ?id=1&name=sehee
                    for (String qs : queryString) {
                        String[] query = qs.split("=");
                        this.parameters.put(query[0], query[1]);
                    }
                } else {
                    this.file = this.uri;
                }
            }
        }
    }

    public String getParameter(String key) {
        return this.getParameters().getOrDefault(key, "");
    }
}
