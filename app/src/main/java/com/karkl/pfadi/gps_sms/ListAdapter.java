package com.karkl.pfadi.gps_sms;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * List adapter for storing SMS data
 *
 * @author itcuties
 */
public class ListAdapter extends ArrayAdapter<HistoryItem> {

    // List context
    private final Context context;
    // List values
    private final List<HistoryItem> historyListList;

    public ListAdapter(Context context, List<HistoryItem> historyList) {
        super(context, android.R.layout.simple_list_item_1, android.R.id.text1, historyList);
        this.context = context;
        this.historyListList = historyList;
    }
}
