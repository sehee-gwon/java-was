package was.domain.enumeration;

import lombok.Getter;

@Getter
public enum DateFormat {
    DATE_TIME("yyyy-MM-dd HH:mm:ss");

    private String format;

    DateFormat(String format) {
        this.format = format;
    }
}
