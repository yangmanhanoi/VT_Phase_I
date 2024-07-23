package com.namduong.viettel.utils;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class TimeConverter {
    // Converts a long timestamp to LocalDateTime with a specified time zone
    public static LocalDateTime convertTimestampToLocalDateTime(long timestamp, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    // Converts a LocalDateTime to a long timestamp with a specified time zone
    public static long convertLocalDateTimeToTimestamp(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        return localDateTime.toInstant(zoneOffset).toEpochMilli();
    }
}
