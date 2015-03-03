package com.karkl.pfadi.gps_sms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.karkl.pfadi.gps_sms.userresponse.ForwardSmsIntent;
import com.karkl.pfadi.gps_sms.userresponse.OpenMapsIntent;


public class SMSPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // custom theme with
        String coordinates = this.getIntent().getStringExtra("coordinates");
        showSmsCoordinateOptions(coordinates);
    }


    private void showSmsCoordinateOptions(final String coordinates) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("SMS mit Standortdaten erhalten!")
                .setMessage("Wähle eine Option?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Weiterleiten", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked, do something
                        Toast.makeText(SMSPopup.this, "Please send it further",
                                Toast.LENGTH_SHORT).show();
//                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                        sendIntent.putExtra("sms_body", coordinates);
//                        sendIntent.setType("vnd.android-dir/mms-sms");
                        ForwardSmsIntent smsIntent = new ForwardSmsIntent(coordinates);
                        SMSPopup.this.startActivity(smsIntent);
                    }
                })
                .setNegativeButton("In Maps öffnen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                                Uri.parse("geo:" + coordinates));
//                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        OpenMapsIntent mapsIntent = new OpenMapsIntent(coordinates);
                        SMSPopup.this.startActivity(mapsIntent);
                    }
                })                        //Do nothing on no
                .setCancelable(true)
                .create()
                .show();
    }
}
