package com.qiuyj.cdexpr.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author qiuyj
 * @since 2022-04-21
 */
public abstract class FunctionUtils {

    private static final Map<String, DateTimeFormatter> FORMATTER_MAP = new HashMap<>();
    static {
        FORMATTER_MAP.put("yyyy-MM-dd", DateTimeFormatter.ISO_LOCAL_DATE);
        FORMATTER_MAP.put("yyyy-MM-dd HH:mm:ss", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        FORMATTER_MAP.put("yyyyMMdd", DateTimeFormatter.ofPattern("yyyyMMdd"));
        FORMATTER_MAP.put("yyyyMMddHHmmss", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private FunctionUtils() {
        // for private
    }

    public static String dateFormat(Object date, String pattern) {
        TemporalAccessor temporalAccessor;
        if (date instanceof Date d) {
            temporalAccessor = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        else if (date instanceof TemporalAccessor) {
            temporalAccessor = (TemporalAccessor) date;
        }
        else {
            throw new IllegalArgumentException("date must be Date or subclass of TemporalAccessor");
        }
        DateTimeFormatter dateTimeFormatter = FORMATTER_MAP.get(pattern);
        if (Objects.isNull(dateTimeFormatter)) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        }
        return dateTimeFormatter.format(temporalAccessor);
    }
}
