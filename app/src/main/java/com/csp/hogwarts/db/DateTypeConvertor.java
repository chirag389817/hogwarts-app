package com.csp.hogwarts.db;

import android.os.Build;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class DateTypeConvertor {
    @TypeConverter
    public static LocalDateTime toDate(Long dateLong){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateLong == null ? null: LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong),
                    TimeZone.getDefault().toZoneId());
        }
        return null;
    }

    @TypeConverter
    public static long fromDate(LocalDateTime date){
        if (date!=null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZonedDateTime zdt = ZonedDateTime.of(date, ZoneId.systemDefault());
            return zdt.toInstant().toEpochMilli();
        }
        return 0;
    }
}
