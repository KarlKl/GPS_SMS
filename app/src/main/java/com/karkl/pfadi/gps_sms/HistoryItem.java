package com.karkl.pfadi.gps_sms;

import java.util.Date;

/**
 * Created by karl.klingelhuber on 03.03.2015.
 */
public class HistoryItem {
    final long time = new Date().getTime();
    final String value;

    public HistoryItem(String value) {
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public String getValue() {
        return value;
    }
}
