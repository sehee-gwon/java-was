package was.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import was.application.exception.HttpSecurityException;

public class HttpValidatorTest {
    private HttpValidator httpValidator;

    @Before
    public void before() {
        httpValidator = new HttpValidator();
    }

    /**
     * 4. 보안 규칙 작업 (403 반환)
     * 보안 규칙 위반 시 응답 코드 403을 반환하는 에러를 던지는지 검사
     */
    @Test
    public void spec4Test() {
        HttpRequest request = new HttpRequest();

        // 상위 디렉터리 접근시 HttpSecurityException 에러가 발생하는지?
        request.setFile("/../../../../Hello");
        Assert.assertThrows(HttpSecurityException.class, () -> {
            httpValidator.validate(request);
        });

        // 확장자가 .exe 인 파일 요청시 HttpSecurityException 에러가 발생하는지?
        request.setFile(".exe");
        Assert.assertThrows(HttpSecurityException.class, () -> {
            httpValidator.validate(request);
        });
    }
}