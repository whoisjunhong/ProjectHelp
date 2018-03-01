package edu.nyp.projecthelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by student on 31/1/18.
 */

public class RequestListAdapter extends ArrayAdapter<ElderlyRequest> {
    public RequestListAdapter(Context context, ArrayList<ElderlyRequest> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_list, parent, false);
        }

        // Get the data item for this position
        ElderlyRequest elderlyRequest = getItem(position);

        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.requesteeName);
        TextView tvAddress = convertView.findViewById(R.id.requesteeAddress);
        TextView tvType = convertView.findViewById(R.id.requesteeType);

        // Populate the data into the template view using the data object
        tvName.setText("Name: " + elderlyRequest.getRequestee());
        tvAddress.setText("Address: " + elderlyRequest.getAddress());
        tvType.setText(elderlyRequest.getType());

        // Return the completed view to render on screen
        return convertView;
    }
}
