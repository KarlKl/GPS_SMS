package com.karkl.pfadi.gps_sms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.playground.karr.sms_location.R;

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
    private final List<HistoryItem> smsList;

    public ListAdapter(Context context, List<HistoryItem> smsList) {
        super(context, R.layout.activity_main, smsList);
        this.context = context;
        this.smsList = smsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_main, parent, false);

        TextView senderNumber = (TextView) rowView.findViewById(R.id.smsNumberText);
        senderNumber.setText(smsList.get(position).getValue());

        return rowView;
    }

}
