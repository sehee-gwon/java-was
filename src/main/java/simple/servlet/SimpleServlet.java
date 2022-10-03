package simple.servlet;

import was.domain.HttpRequest;
import was.domain.HttpResponse;

import java.io.IOException;

public interface SimpleServlet {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}
