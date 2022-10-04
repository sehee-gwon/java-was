package was.application;

import org.junit.Test;
import was.domain.Setting;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class WebConfigTest {
    /**
     * 2. 설정 파일 관리
     * 각 JSON 설정값이 제대로 매핑되었는지 검사
     */
    @Test
    public void spec2Test() {
        WebConfig config = new WebConfig();
        Setting setting = config.getSetting();

        // 각 JSON 설정값이 있는지?
        assertNotEquals(setting.getPort(), 0);
        assertNotEquals(setting.getNumTreads(), 0);
        assertNotNull(setting.getIndex());
        assertNotNull(setting.getServers());
        assertNotNull(setting.getMappings());
    }
}