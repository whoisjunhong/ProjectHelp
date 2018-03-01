package edu.nyp.projecthelp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

public class VolunteerScheduledActivity extends Fragment implements OnTaskCompleted {

    View myView;

    // Construct the data source
    ArrayList<ElderlyRequest> listRequest;

    ListView listView = null;

    // Add item to adapter
    JSONArray jsonArray;
    VolunteerScheduledListAdapter adapter;

    String msgType;
    String currentUser;
    String status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ((NavigationActivity) getActivity()).showFloatingActionButton();
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_volunteer_scheduled);
        myView = inflater.inflate(R.layout.activity_volunteer_scheduled, container, false);

        listRequest = new ArrayList<>();
        listView = myView.findViewById(R.id.lvVolunteerScheduled);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), VolunteerScheduledDetailActivity.class);
                ElderlyRequest scheduledDetails =  listRequest.get(position);
                intent.putExtra("volunteerScheduledDetails", scheduledDetails ); //Put your id to your next Intent
                startActivity(intent);
            }
        });


        adapter = new VolunteerScheduledListAdapter(getActivity(), listRequest);

        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        retrievePreferences();
        msgType = "getVolunteerScheduled";

        String jsonString = convertToJSON();
        HttpAsyncTask task = new HttpAsyncTask(VolunteerScheduledActivity.this);
        task.execute("http://" + Global.HOST + "/" + Global.DIR + "/getVolunteerSchedule.php", jsonString);
    }

    public void retrievePreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUser = prefs.getString("id", "");
    }

    public String convertToJSON() {

        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();
            jsonText.key("msgType");
            jsonText.value(msgType);
            jsonText.key("id");
            jsonText.value(currentUser);
            jsonText.endObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonText.toString();
    }

    public void retrieveFromJSON(String message) {

        try {
            JSONObject jsonObject = new JSONObject(message);
            jsonArray = jsonObject.getJSONArray("volunteerScheduledDetails");
            status = jsonObject.getString("status");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskCompleted(String response) {
        retrieveFromJSON(response);
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                ElderlyRequest myElderly = new ElderlyRequest();
                JSONObject jObject = jsonArray.getJSONObject(i);
                myElderly.setRequestId(jObject.getInt("requestId"));
                myElderly.setRequestee(jObject.getString("requestee"));
                myElderly.setType(jObject.getString("type"));
                myElderly.setGender(jObject.getString("gender"));
                myElderly.setAddress(jObject.getString("address"));
                myElderly.setUnitno(jObject.getString("unitno"));
                myElderly.setLocationLat(jObject.getString("locationLat"));
                myElderly.setLocationLong(jObject.getString("locationLong"));
                myElderly.setRequestDate(jObject.getString("requestDate"));
                myElderly.setRequestTime(jObject.getString("requestTime"));
                myElderly.setStatus(jObject.getString("requestStatus"));
                listRequest.add(myElderly);
            }
            listView.setAdapter(adapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}