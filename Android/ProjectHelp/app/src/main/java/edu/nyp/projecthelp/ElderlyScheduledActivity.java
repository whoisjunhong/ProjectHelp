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

public class ElderlyScheduledActivity extends Fragment implements OnTaskCompleted {

    View myView;

    // Construct the data source
    ArrayList<ElderlyRequest> listRequest;

    ListView listView = null;

    // Add item to adapter
    JSONArray jsonArray;
    ElderlyScheduledListAdapter adapter;

    String msgType;
    String currentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ((NavigationActivity) getActivity()).showFloatingActionButton();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_elderly_scheduled);
        myView = inflater.inflate(R.layout.activity_elderly_scheduled, container, false);

        listRequest = new ArrayList<>();
        listView = myView.findViewById(R.id.lvElderlyScheduled);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), "Showing request", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), ElderlyScheduledDetailActivity.class);
                ElderlyRequest scheduledDetails =  listRequest.get(position);
                intent.putExtra("elderlyScheduledDetails", scheduledDetails ); //Put your id to your next Intent
                startActivity(intent);
            }
        });


        adapter = new ElderlyScheduledListAdapter(getActivity(), listRequest);

        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        msgType = "getElderlyScheduled";

        String jsonString = convertToJSON();
        HttpAsyncTask task = new HttpAsyncTask(ElderlyScheduledActivity.this);
        task.execute("http://" + Global.HOST + "/" + Global.DIR + "/getElderlySchedule.php", jsonString);
    }

    public String convertToJSON() {

        retrievePreferences();

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
            jsonArray = jsonObject.getJSONArray("elderlyScheduledDetails");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void retrievePreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUser = prefs.getString("id", "");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}