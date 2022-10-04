package simple.service;

import lombok.extern.slf4j.Slf4j;
import simple.servlet.SimpleServlet;
import was.domain.HttpRequest;
import was.domain.HttpResponse;

import java.io.IOException;
import java.io.Writer;

@Slf4j
public class Hello implements SimpleServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        Writer writer = response.getWriter();
        writer.write("service.Hello, ");
        writer.write(request.getParameter("name"));
    }
}