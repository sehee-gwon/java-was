package simple;

import lombok.extern.slf4j.Slf4j;
import simple.servlet.SimpleServlet;
import was.domain.HttpRequest;
import was.domain.HttpResponse;
import was.domain.enumeration.DateFormat;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Time implements SimpleServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        Writer writer = response.getWriter();
        LocalDateTime dateTime = LocalDateTime.now();
        writer.write(dateTime.format(DateTimeFormatter.ofPattern(DateFormat.DATE_TIME.getFormat())));
    }
}
