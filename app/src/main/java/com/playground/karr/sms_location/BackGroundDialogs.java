package com.playground.karr.sms_location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

/**
 * @ To create the illusion of a alert window displayed on its own when app is
 * in the background. Really, this is a Activity but its only displaying an
 * alert window and the Activity borders have been removed.
 */
public class BackGroundDialogs extends Activity {

    public BroadcastReceiver receiver;
    public AlertDialog mAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // custom theme with
        // no borders
        IntentFilter filter = new IntentFilter();
        //filter.addAction(Consts.DISMISS_DIALOG);// we can dismiss it via an
        // intent if we choose
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, final Intent intent) {
                // do something based on the intent's action
                if (context == null) return;
                // if (intent.getAction().equals(Consts.DISMISS_DIALOG)) {
                //   finish();
                //}
            }
        };
        registerReceiver(receiver, filter);
    }

    /**
     * @param reason :the message you wish to display in the alert
     * @brief Shows an alert message using a Dialog window.
     */
    public void showAlert(final String reason) {
        mAlert = new AlertDialog.Builder(this).create();
        mAlert.setCancelable(false);
        TextView Msg_tv = new TextView(this);
        Msg_tv.setTypeface(null, Typeface.BOLD);
        Msg_tv.setTextSize(16.0f);
        Msg_tv.setText(reason);
        Msg_tv.setGravity(Gravity.CENTER_HORIZONTAL);
        mAlert.setView(Msg_tv);
        mAlert.setButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        mAlert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        String reason = extras.getString(Intent.EXTRA_TEXT);
        if (reason.equalsIgnoreCase("DISMISS")) finish();
        else showAlert(reason);// invoke the new dialog to show
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAlert != null) if (mAlert.isShowing()) mAlert.dismiss();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
