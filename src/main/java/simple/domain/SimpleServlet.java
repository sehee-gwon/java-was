package simple.domain;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface SimpleServlet {
    void service(HttpRequest request, HttpResponse response);
}
