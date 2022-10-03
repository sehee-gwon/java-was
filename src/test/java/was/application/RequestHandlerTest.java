package was.application;

import org.junit.Before;
import org.junit.Test;
import was.domain.Setting;

import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class RequestHandlerTest {
    private Setting setting;
    private Socket socket;

    @Before
    public void before() {
        WebConfig config = new WebConfig();
        this.setting = config.getSetting();

        socket = mock(Socket.class);
    }

    @Test
    public void spec_1() {

    }

    @Test
    public void spec_2() {
        assertNotNull(this.setting);
    }
}