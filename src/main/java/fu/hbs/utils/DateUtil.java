package fu.hbs.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {
    public static String formatInstantToPattern(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        // Format the Instant with the specified formatter and locale
        return formatter.format(instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh")));
    }
}
