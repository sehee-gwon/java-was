package was.application;

import lombok.extern.slf4j.Slf4j;
import simple.servlet.SimpleServlet;
import was.application.exception.HttpSecurityException;
import was.domain.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;

@Slf4j
public class RequestHandler implements Runnable {
    private Setting setting;
    private Socket connection;
    private HttpValidator httpValidator;

    public RequestHandler(Setting setting, Socket connection) {
        this.setting = setting;
        this.connection = connection;
        this.httpValidator = new HttpValidator();
    }

    @Override
    public void run() {
        HttpRequest request = null;
        HttpResponse response;

        try {
            InputStream in = this.connection.getInputStream();
            OutputStream out = this.connection.getOutputStream();

            // http 요청 헤더 분석
            request = new HttpRequest();
            request.parse(in, this.setting.getIndex());
            log.debug("http request: {}", request);

            // 빈 요청이나 파비콘은 스킵, Postman 에서 테스트시 요청을 2번 보냄...
            // 첫번째 요청은 빈 값을 던져 서버가 살아있는지 확인하고, 두번째 요청에 값을 전송하는 것 같음
            if (request.getMethod() == null || request.getFile().indexOf("favicon.ico") > -1) return;

            // HTTP 유효성 검증 (403, 500)
            httpValidator.validate(request);

            // http 응답 출력 스트림 세팅
            response = new HttpResponse(out);

            // 설정 파일 검사
            String file = request.getFile();
            Mapping mapping = setting.getMappings().stream()
                    .filter(m -> m.getRouter().containsKey(file))
                    .findFirst()
                    .orElse(null);

            // http 응답 헤더 구성
            if (mapping != null) {  // class read
                // response header
                response.setHeader();
                // response body
                Class<?> clazz = Class.forName(mapping.getName() + "." + mapping.getRouter().get(file));
                SimpleServlet servlet = (SimpleServlet) clazz.getDeclaredConstructor().newInstance();
                servlet.service(request, response);
            } else {    // file read, 요청한 경로의 파일이 있다면 응답
                InputStream fileIn = getClass().getClassLoader().getResourceAsStream(file);
                if (fileIn != null) {
                    response.setHeader();      // response header
                    response.setBody(fileIn);  // response body
                } else {    // class 도 없고, 파일도 존재하지 않는다면 404 처리
                    throw new FileNotFoundException("file not found");
                }
            }

            response.getWriter().flush();

        } catch (HttpSecurityException e) {  // 403
            log.error(e.getMessage(), e);
            ResponseHandler.failure(this.connection, request, this.setting, HttpURLConnection.HTTP_FORBIDDEN);
        } catch (FileNotFoundException | ClassNotFoundException e) {  // 404
            log.error(e.getMessage(), e);
            ResponseHandler.failure(this.connection, request, this.setting, HttpURLConnection.HTTP_NOT_FOUND);
        } catch (UnsupportedOperationException e) {  // 501
            log.error(e.getMessage(), e);
            ResponseHandler.failure(this.connection, request, this.setting, HttpURLConnection.HTTP_NOT_IMPLEMENTED);
        } catch (Exception e) {  // 500
            log.error("RequestHandler run Error: {}", e.getMessage(), e);
            ResponseHandler.failure(this.connection, request, this.setting, HttpURLConnection.HTTP_INTERNAL_ERROR);
        } finally {
            try {
                this.connection.close();
            } catch (IOException e) {
                log.error("socket connection close Error: {}", e.getMessage(), e);
            }
        }
    }
}
