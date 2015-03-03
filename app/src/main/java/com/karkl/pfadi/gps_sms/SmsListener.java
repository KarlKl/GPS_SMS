package com.karkl.pfadi.gps_sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.karkl.pfadi.gps_sms.userresponse.ForwardSmsIntent;
import com.karkl.pfadi.gps_sms.userresponse.OpenMapsIntent;
import com.playground.karr.sms_location.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

/**
 * Created by karl.klingelhuber on 06.02.2015.
 */
public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody().trim();
                        // This is the simple way, but current expression validates the range of the lat/long values
                        // "^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$";
                        String pattern_Coordinates = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$";
                        if (msgBody.matches(pattern_Coordinates)) {
                            handleCoordinateSms(context, msgBody);
                            saveInHistory(context, msgBody);
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }

    private void saveInHistory(Context context, String coordinates) {
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String historyJsonString = prefs.getString("history", null);
        try {
            JSONArray jsonArray = new JSONArray(historyJsonString);
            String historyEntry = new JSONArray("{" +
                    "'time': '" + new Date().getTime() + "', " +
                    "'data': '" + coordinates + "' " +
                    "}").toString();
            jsonArray.put(historyEntry);
            SharedPreferences.Editor e = prefs.edit();
            e.putString("history", jsonArray.toString());
            e.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleCoordinateSms(final Context context, final String coordinates) {
        Intent intent = new Intent(context, SMSPopup.class);
        intent.putExtra("coordinates", coordinates);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        notifyMe(context, coordinates);
    }

    public void notifyMe(Context context, String coordinates) {
        // prepare intent which is triggered if the
        // notification is selected

        ForwardSmsIntent smsIntent = new ForwardSmsIntent(coordinates);
        PendingIntent pSmsIntent = PendingIntent.getActivity(context, 0, smsIntent, 0);
        OpenMapsIntent mapsIntent = new OpenMapsIntent(coordinates);
        PendingIntent pMapsIntent = PendingIntent.getActivity(context, 0, mapsIntent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.received_gps_sms))
                .setContentText(coordinates)
                .setSmallIcon(R.mipmap.ic_gps_sms)
                .setContentIntent(pMapsIntent)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_mapmode, "Maps", pMapsIntent)
                .addAction(android.R.drawable.sym_action_email, "SMS", pSmsIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

}
