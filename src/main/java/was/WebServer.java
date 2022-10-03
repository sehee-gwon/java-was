package was;

import lombok.extern.slf4j.Slf4j;
import was.application.RequestHandler;
import was.application.WebConfig;
import was.domain.Setting;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class WebServer {
    public static void main(String[] args) {
        // 설정 파일 불러오기
        WebConfig config = new WebConfig();
        Setting setting = config.getSetting();

        // 소켓 접속 요청 대기
        ExecutorService pool = Executors.newFixedThreadPool(setting.getNumTreads());
        try (ServerSocket server = new ServerSocket(setting.getPort())) {
            log.info("===============================");
            log.info("|       JAVA WEB SERVER       |");
            log.info("|            START            |");
            log.info("|          PORT:" + setting.getPort() + "          |");
            log.info("===============================");

            log.debug("application.json: {}", setting);

            while (true) {
                Socket request = server.accept();
                Runnable r = new RequestHandler(setting, request);
                pool.submit(r);
            }
        } catch (IOException e) {
            log.error("Socket request accept Error: {}", e.getMessage(), e);
        }
    }
}
