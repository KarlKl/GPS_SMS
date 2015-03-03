package com.karkl.pfadi.gps_sms.userresponse;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by karl.klingelhuber on 25.02.2015.
 */
public class OpenMapsIntent extends Intent {
    public OpenMapsIntent(String coordinates) {
        super(Intent.ACTION_VIEW,
                Uri.parse("geo:" + coordinates));
//        this.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
    }
}
