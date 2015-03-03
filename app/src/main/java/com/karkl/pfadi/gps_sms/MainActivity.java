package com.karkl.pfadi.gps_sms;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.karkl.pfadi.gps_sms.userresponse.ForwardSmsIntent;
import com.karkl.pfadi.gps_sms.userresponse.OpenMapsIntent;
import com.playground.karr.sms_location.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity. Displays a list of numbers.
 *
 * @author itcuties
 */
@TargetApi(17)
public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<HistoryItem> smsList = loadHistoryItems();

//                new ArrayList<HistoryItem>();

//        Uri uri = Uri.parse("content://sms/inbox");
//        Cursor c = getContentResolver().query(uri, null, null, null, null);
//        startManagingCursor(c);
//
//        // Read the sms data and store it in the list
//        if (c.moveToFirst()) {
//            for (int i = 0; i < c.getCount(); i++) {
//                SMSData sms = new SMSData();
//                sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
//                sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
//                smsList.add(sms);
//
//                c.moveToNext();
//            }
//        }
//        c.close();

        // Set smsList in the ListAdapter
        setListAdapter(new ListAdapter(this, smsList));

    }

    private List<HistoryItem> loadHistoryItems() {
        List<HistoryItem> historyList = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String historyJsonString = prefs.getString("history", null);
        try {
            JSONArray jsonArray = new JSONArray(historyJsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                historyList.add(new HistoryItem(jsonObject.getString("value")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        System.out.println("hello world");
        SMSData sms = (SMSData) getListAdapter().getItem(position);
        notifyMe(sms);


//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder
//                .setTitle("SMS mit Standortdaten!")
//                .setMessage("Wähle eine Option?")
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setPositiveButton("Weiterleiten", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Yes button clicked, do something
//                        Toast.makeText(getApplicationContext(), "Please send it further",
//                                Toast.LENGTH_SHORT).show();
//                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                        sendIntent.putExtra("sms_body", "coordinates");
//                        sendIntent.setType("vnd.android-dir/mms-sms");
//                        startActivity(sendIntent);
//                    }
//                })
//                .setNegativeButton("In Maps öffnen", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                                Uri.parse("geo:48.2161098,18.6295106"));
//                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                        startActivity(intent);
//                    }
//                })						//Do nothing on no
//                .setCancelable(true)
//                .show();
    }

    public void notifyMe(SMSData sms) {
        System.out.println("hello");
        // prepare intent which is triggered if the
        // notification is selected

        ForwardSmsIntent smsIntent = new ForwardSmsIntent(sms.getBody());
        PendingIntent pSmsIntent = PendingIntent.getActivity(this, 0, smsIntent, 0);
        OpenMapsIntent mapsIntent = new OpenMapsIntent(sms.getBody());
        PendingIntent pMapsIntent = PendingIntent.getActivity(this, 0, mapsIntent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(this)
                .setContentTitle(getResources().getString(R.string.received_gps_sms))
                .setContentText(sms.getBody())
                .setSmallIcon(R.mipmap.ic_gps_sms)
                .setContentIntent(pMapsIntent)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_mapmode, "Maps", pMapsIntent)
                .addAction(android.R.drawable.sym_action_email, "SMS", pSmsIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);


//        // Instantiate a Builder object.
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//// Creates an Intent for the Activity
//        Intent notifyIntent =
//                new Intent(this, getClass());
//// Sets the Activity to start in a new, empty task
//        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//// Creates the PendingIntent
//        PendingIntent notifyPendingIntent =
//                PendingIntent.getActivity(
//                        this,
//                        0,
//                        notifyIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//
//// Puts the PendingIntent into the notification builder
//        builder.setContentIntent(notifyPendingIntent);
//// Notifications are issued by sending them to the
//// NotificationManager system service.
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//// Builds an anonymous Notification object from the builder, and
//// passes it to the NotificationManager
//        mNotificationManager.notify(5, builder.build());
    }

}
