package was.application;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import was.domain.HttpRequest;
import was.domain.Server;
import was.domain.Setting;
import was.domain.enumeration.DateFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ResponseHandler {
    public static Server getServer(HttpRequest request, Setting setting) {
        // 일치하는 설정이 없다면, 첫번째 설정값을 디폴트로 한다.
        Server server = setting.getServers().stream()
                .filter(s -> request != null && s.getDomain().equals(request.getHost()))
                .findAny()
                .orElse(null);

        if (server == null) server = setting.getServers().get(0);

        return server;
    }

    public static void failure(Socket connection, HttpRequest request, Setting setting, int code) {
        Server server = getServer(request, setting);

        // 과제에서 제시하는 403, 404 코드만 별도 처리
        // 나머지 오류의 사용자(html) 노출은 500 페이지로 처리한다. 응답 코드는 받은 대로 리턴함..
        if (HttpURLConnection.HTTP_FORBIDDEN == code || HttpURLConnection.HTTP_NOT_FOUND == code) {
            write(connection, server.getErrors().get(Integer.toString(code)), code);
        } else {
            write(connection, server.getErrors().get(Integer.toString(HttpURLConnection.HTTP_INTERNAL_ERROR)), code);
        }
    }

    public static void write(Socket connection, String file, int code) {
        try {
            // header
            Writer writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write("HTTP/1.1 " + code + "\r\n");
            writer.write("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormat.DATE_TIME.getFormat())) + "\r\n");
            writer.write("Content-Type: text/html\r\n\r\n");

            // body (error.html)
            try {
                InputStream in = ResponseHandler.class.getClassLoader().getResourceAsStream(file);
                writer.write(IOUtils.toString(in, StandardCharsets.UTF_8));
            } catch (IOException e) {
                log.error("http failure response body write Error: {}", e.getMessage(), e);
            }

            writer.flush();
        } catch (IOException e) {
            log.error("http failure response header write Error: {}", e.getMessage(), e);
        }
    }
}
