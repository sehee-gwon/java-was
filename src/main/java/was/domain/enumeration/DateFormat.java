package was.domain.enumeration;

import lombok.Getter;

@Getter
public enum DateFormat {
    DATE_TIME("yyyy-MM-dd HH:mm:ss"),
    DATE_HOUR_MINUTE("yyyy-MM-dd HH:mm");

    private String format;

    DateFormat(String format) {
        this.format = format;
    }
}
