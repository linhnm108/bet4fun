package com.fafc.bet4fun.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DateTimeUtils.class);

    public static Date convertLocalDateToUTC(Date localDate) {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return convertStringToDate(dateFormat.format(localDate), Constants.DATE_PATTERN);
    }

    public static Date convertUTCDateToLocal(Date utcDate) {
        return new Date(utcDate.getTime() + TimeZone.getDefault().getOffset(utcDate.getTime()));
    }

    public static String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);
        return dateFormat.format(date);
    }

    public static Date convertStringToDate(String strDate, String datePattern) {
        try {
            return DateUtils.parseDate(strDate, datePattern);
        } catch (ParseException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }
}
