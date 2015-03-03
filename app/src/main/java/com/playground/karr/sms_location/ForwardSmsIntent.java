package com.playground.karr.sms_location;

import android.content.Intent;

/**
 * Created by karl.klingelhuber on 25.02.2015.
 */
public class ForwardSmsIntent extends Intent {
    public ForwardSmsIntent(String coordinates) {
        this.putExtra("sms_body", coordinates);
        this.setType("vnd.android-dir/mms-sms");
    }
}
