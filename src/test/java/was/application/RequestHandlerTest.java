package was.application;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import was.domain.Mapping;
import was.domain.Server;
import was.domain.Setting;
import was.domain.enumeration.DateFormat;
import was.domain.enumeration.HttpMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestHandlerTest {
    private Setting setting;
    private String version;

    private Socket connection;
    private OutputStream out;

    @Before
    public void before() throws IOException {
        setting = getSetting();
        version = "HTTP/1.1";

        connection = mock(Socket.class);
        out = new ByteArrayOutputStream();

        when(connection.getOutputStream()).thenReturn(out);
    }

    /**
     * 1. HTTP/1.1의 Host 헤더 해석
     * 설정에 따라 서버에서 다른 데이터를 제공하는지 검사
     * @throws IOException
     */
    @Test
    public void spec1Test() throws IOException {
        // 설정파일 N개 서버 검사
        for (Server server : setting.getServers()) {
            String domain = server.getDomain();

            // http 요청 헤더 문자열 생성, 임의로 403 응답 재현(.exe)
            InputStream in = new ByteArrayInputStream(getHeader(HttpMethod.GET, domain, "/.exe").getBytes());
            when(connection.getInputStream()).thenReturn(in);

            // WebServer 에서 요청이 들어오는 것처럼 동작
            RequestHandler requestHandler = new RequestHandler(setting, connection);
            requestHandler.run();

            // 출력 내용에 해당 서버 도메인이 포함되었는지?
            assertTrue(out.toString().indexOf(domain) > -1);

            out = new ByteArrayOutputStream();  // 출력 스트림 초기화
            when(connection.getOutputStream()).thenReturn(out);
        }
    }

    /**
     * 3. 403, 404, 500 오류 처리
     * 각 오류 발생 시 적절한 HTML 을 반환하는지 검사
     * @throws IOException
     */
    @Test
    public void spec3Test() throws IOException {
        Server server = setting.getServers().get(0);

        // 설정 파일 내 Errors 조회
        for (String code : server.getErrors().keySet()) {
            HttpMethod method = HttpMethod.GET;
            String uri;

            int errorCode = Integer.parseInt(code);

            if (HttpURLConnection.HTTP_FORBIDDEN == errorCode) {  // 403 재현
                uri = "/.exe";
            } else if (HttpURLConnection.HTTP_NOT_FOUND == errorCode) {  // 404 재현
                uri = "/GWONSEHEE404REALTRUE";
            } else {  // 500 재현 (GET 메소드만 지원, 이외 다른 메소드는 501)
                method = HttpMethod.POST;
                uri = "/Hello";
            }

            InputStream in = new ByteArrayInputStream(getHeader(method, "localhost", uri).getBytes());
            when(connection.getInputStream()).thenReturn(in);

            // WebServer 에서 요청이 들어오는 것처럼 동작
            RequestHandler requestHandler = new RequestHandler(setting, connection);
            requestHandler.run();

            // 출력 내용에 설정 파일의 오류 코드가 포함되었는지?
            assertTrue(out.toString().indexOf(code) > -1);

            out = new ByteArrayOutputStream();  // 출력 스트림 초기화
            when(connection.getOutputStream()).thenReturn(out);
        }
    }

    /**
     * 6. 간단한 WAS 구현
     * 설정 파일을 이용해 매핑되었는지 검사
     * @throws IOException
     */
    @Test
    public void spec6Test() throws IOException {
        for (Mapping mapping : setting.getMappings()) {
            for (Map.Entry<String, String> entry : mapping.getRouter().entrySet()) {
                // http 요청 헤더 문자열 생성 (url: router)
                InputStream in = new ByteArrayInputStream(getHeader(HttpMethod.GET, "localhost", entry.getKey()).getBytes());
                when(connection.getInputStream()).thenReturn(in);

                // WebServer 에서 요청이 들어오는 것처럼 동작
                RequestHandler requestHandler = new RequestHandler(setting, connection);
                requestHandler.run();

                // 출력 내용에 설정 파일의 문구가 포함되었는지?
                assertTrue(out.toString().indexOf(entry.getValue()) > -1);

                out = new ByteArrayOutputStream();  // 출력 스트림 초기화
                when(connection.getOutputStream()).thenReturn(out);
            }
        }
    }

    /**
     * 7. 현재 시각을 출력하는 SimpleServlet 구현체 작성
     * 현재 시각을 출력하는지 검사 (분까지만 검사)
     * @throws IOException
     */
    @Test
    public void spec7Test() throws IOException {
        InputStream in = new ByteArrayInputStream(getHeader(HttpMethod.GET, "localhost", "/Time").getBytes());
        when(connection.getInputStream()).thenReturn(in);

        RequestHandler requestHandler = new RequestHandler(setting, connection);
        requestHandler.run();

        // 출력 내용에 현재 시각이 포함되어 있는지?
        String dateHourMinute = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormat.DATE_HOUR_MINUTE.getFormat()));
        assertTrue(out.toString().indexOf(dateHourMinute) > -1);
    }

    @After
    public void after() throws IOException {
        connection.close();
    }

    /**
     * header 문자열 생성
     * @param method
     * @param host
     * @param uri
     * @return
     */
    private String getHeader(HttpMethod method, String host, String uri) {
        StringBuffer sb = new StringBuffer();
        sb.append(method.name()).append(" ").append(uri).append(" ").append(version);
        sb.append("\r\n");
        sb.append("Host: ").append(host);
        return sb.toString();
    }

    /**
     * 설정 파일 테스트 -> WebConfigTest.java
     * 테스트 실패 가능성이 있으므로 하드코딩으로 세팅
     * @return
     */
    private Setting getSetting() {
        // servers
        List<Server> servers = new ArrayList<>();

        Map<String, String> errors1 = new HashMap<>();
        errors1.put("403", "static/a/error/403.html");
        errors1.put("404", "static/a/error/404.html");
        errors1.put("500", "static/a/error/500.html");

        Server server1 = new Server("www.a.com", errors1);

        servers.add(server1);

        Map<String, String> errors2 = new HashMap<>();
        errors2.put("403", "static/b/error/403.html");
        errors2.put("404", "static/b/error/404.html");
        errors2.put("500", "static/b/error/500.html");

        Server server2 = new Server("www.b.com", errors2);

        servers.add(server2);

        // mappings
        List<Mapping> mappings = new ArrayList<>();

        Map<String, String> router = new HashMap<>();
        router.put("/Hello", "Hello");
        router.put("/service.Hello", "service.Hello");
        router.put("/Greeting", "Hello");
        router.put("/super.Greeting", "service.Hello");
        router.put("/Time", "Time");

        Mapping mapping = new Mapping("simple", router);

        mappings.add(mapping);

        return new Setting(8080, 50, "static/index.html", servers, mappings);
    }
}