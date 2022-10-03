package was.domain;

import was.application.exception.HttpSecurityException;
import was.domain.enumeration.HttpMethod;

public class HttpValidator {
    public void validate(HttpRequest request) {
        validate403(request);
        validate501(request);
    }

    public void validate403(HttpRequest request) {
        if (request.getFile() == null) return;

        // HTTP_ROOT 디렉터리의 상위 디렉터리에 접근할 때
        if (request.getFile().startsWith("/../")) {
            throw new HttpSecurityException("http root parent directory no access");
        }

        // 확장자가 .exe 인 파일을 요청받았을 때
        if (request.getFile().endsWith(".exe")) {
            throw new HttpSecurityException("file extension 'exe' is not allowed");
        }
    }

    public void validate501(HttpRequest request) {
        // 지금은 GET 메소드만 지원 가능
        if (HttpMethod.GET != request.getMethod()) {
            throw new UnsupportedOperationException("http method not implemented");
        }
    }
}
