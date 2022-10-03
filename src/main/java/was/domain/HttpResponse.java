package was.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import was.domain.enumeration.DateFormat;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@ToString
@Getter
public class HttpResponse {
    private Writer writer;

    public HttpResponse(OutputStream out) {
        this.writer = new OutputStreamWriter(out);
    }

    public void setHeader() throws IOException {
        this.writer.write("HTTP/1.1 " + HttpURLConnection.HTTP_OK + "\r\n");
        this.writer.write("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormat.DATE_TIME.getFormat())) + "\r\n");
        this.writer.write("Content-Type: text/html\r\n\r\n");
    }

    public void setBody(InputStream in) throws IOException {
        this.writer.write(IOUtils.toString(in, StandardCharsets.UTF_8));
    }
}
