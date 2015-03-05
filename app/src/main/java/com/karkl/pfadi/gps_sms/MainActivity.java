package com.karkl.pfadi.gps_sms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
public class MainActivity extends Activity {

    public static final int MENU_CONTEXT_OPEN_MAPS = 1;
    public static final int MENU_CONTEXT_FORWARD_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<HistoryItem> historyList = loadHistoryItems();

        ListView lv = (ListView) findViewById(R.id.listView);

        // Set historyList in the ListAdapter
        lv.setAdapter(new ListAdapter(this, historyList));
        lv.setOnItemClickListener(onListItemClick());

        registerForContextMenu(lv);
    }

    private List<HistoryItem> loadHistoryItems() {
        Log.d("gps_sms", "loadHistoryItems");
        List<HistoryItem> historyList = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String historyJsonString = prefs.getString("history", null);
        if (historyJsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(historyJsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    historyList.add(new HistoryItem(jsonObject.getString("data")));
                }
            } catch (JSONException e) {
                Log.e("gps_sms", "Error while reading history", e);
            }
        }
        return historyList;
    }

    protected AdapterView.OnItemClickListener onListItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListView lv = (ListView) findViewById(R.id.listView);
                HistoryItem historyItem = (HistoryItem) lv.getAdapter().getItem(position);
                Log.d("GPS_SMS", "clicked list item");
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1, MENU_CONTEXT_OPEN_MAPS, 1, "Open Maps");
        menu.add(1, MENU_CONTEXT_FORWARD_SMS, 2, "Forward via SMS");
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListView lv = (ListView) findViewById(R.id.listView);
        HistoryItem historyItem = (HistoryItem) lv.getAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case MENU_CONTEXT_OPEN_MAPS:
                startActivity(new OpenMapsIntent(historyItem.getValue()));
                break;
            case MENU_CONTEXT_FORWARD_SMS:
                startActivity(new ForwardSmsIntent(historyItem.getValue()));
                break;
        }
        return true;
    }
}
