package edu.nyp.projecthelp;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by student on 2/2/18.
 */

public class ElderlyScheduledListAdapter extends ArrayAdapter<ElderlyRequest> {
    public ElderlyScheduledListAdapter(Context context, ArrayList<ElderlyRequest> users) {
        super(context, 0, users);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.elderly_scheduled_list, parent, false);
        }

        // Get the data item for this position
        ElderlyRequest elderlyRequest = getItem(position);

        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.requesteeName);
        TextView tvAddress = convertView.findViewById(R.id.requesteeAddress);
        TextView tvType = convertView.findViewById(R.id.requesteeType);
        TextView tvStatus = convertView.findViewById(R.id.requesteeStatus);

        // Populate the data into the template view using the data object
        tvName.setText("Name: " + elderlyRequest.getRequestee());
        tvAddress.setText("Address: " + elderlyRequest.getAddress());
        tvType.setText(elderlyRequest.getType());
        if (elderlyRequest.getStatus().equals("T")) {
            tvStatus.setText("Waiting for volunteers to respond!");
            tvStatus.setTextColor(getContext().getColor(R.color.colorOrange));
        }
        else if (elderlyRequest.getStatus().equals("P")) {
            tvStatus.setText("Volunteer found! He / She will come on " + elderlyRequest.getRequestDate() + " at " + elderlyRequest.getRequestTime());
            tvStatus.setTextColor(getContext().getColor(R.color.colorGreen));
        }
        else if (elderlyRequest.getStatus().equals("F")) {
            tvStatus.setText("Request Completed!");
            tvStatus.setTextColor(getContext().getColor(R.color.black_overlay));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}

