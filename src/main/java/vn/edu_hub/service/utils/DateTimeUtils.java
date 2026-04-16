package vn.edu_hub.service.utils;

import io.micrometer.common.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateTimeUtils {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_TIME_2_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String ZONE_ID_7 = "UTC+07:00";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final DateTimeFormatter vnFormatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM) //định dạng ngày giờ mức (vừa đủ, không quá dài/ngắn)
            .withLocale(new Locale("vi", "VN")) // hiển thị theo ngôn ngữ/ định dạng việt nam
            .withZone(ZoneId.of(ZONE_ID_7)); // gán múi giờ viêt nam, đảm bảo khi format, kết quả được chuyển đúng theo giờ việt nam, ngay cả khi server chạy ở múi giờ khác

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);//được dùng để chuyển đổi qua lại giữa LocalDate và chuỗi ngày tháng có định dạng dd/MM/yyyy.
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    public static final DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern(DATE_TIME_2_FORMAT);
    public static final ZoneId zoneId = ZoneId.of(ZONE_ID_7);

    public static LocalDate toLocalDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(zoneId).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static Instant toInstantStart(String dateString) {
        if (StringUtils.isBlank(dateString)) return null;
        LocalDate date = LocalDate.parse(dateString, dateFormatter);
        ZonedDateTime zonedDateTime = date.atTime(LocalTime.MIN).atZone(zoneId);
        return zonedDateTime.toInstant();
    }

    public static Instant toInstantEnd(String dateString) {
        if (StringUtils.isBlank(dateString)) return null;
        LocalDate date = LocalDate.parse(dateString, dateFormatter);
        ZonedDateTime zonedDateTime = date.atTime(LocalTime.MAX).atZone(zoneId);
        return zonedDateTime.toInstant();
    }

    public static LocalDate toLocalDate(String dateString) {
        return StringUtils.isBlank(dateString) ? null : LocalDate.parse(dateString, dateFormatter);
    }

    public static long localDateToMillis(LocalDate localDate) {
        return localDate.atStartOfDay(zoneId).toInstant().toEpochMilli();
    }

    public static Instant getEndOfMonth(int year, int month) {
        LocalDate endOfMonth = YearMonth.of(year, month).atEndOfMonth();
        return LocalDateTime.of(endOfMonth, LocalTime.MAX).atZone(zoneId).toInstant();
    }

    public static Instant getStartOfMonth(int year, int month) {
        return LocalDateTime.of(year, month, 1, 0, 0, 0).atZone(zoneId).toInstant();
    }
}
