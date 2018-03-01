package edu.nyp.projecthelp;

import android.app.Fragment;
import android.content.Intent;
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

public class VolunteerActivity extends Fragment implements OnTaskCompleted {

    View myView;

    // Construct the data source
    ArrayList<ElderlyRequest> listRequest;

    ListView listView = null;

    // Add item to adapter
    JSONArray jsonArray;
    RequestListAdapter adapter;

    String msgType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ((NavigationActivity) getActivity()).showFloatingActionButton();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_volunteer);
        myView = inflater.inflate(R.layout.activity_volunteer, container, false);

        listRequest = new ArrayList<>();
        listView = myView.findViewById(R.id.lvVolunteer);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), "Showing request", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), RequestDetailActivity.class);
                ElderlyRequest requestDetails =  listRequest.get(position);
                intent.putExtra("requestDetails", requestDetails ); //Put your id to your next Intent
                startActivity(intent);
            }
        });


        adapter = new RequestListAdapter(getActivity(), listRequest);

        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        msgType = "getRequest";

        String jsonString = convertToJSON();
        HttpAsyncTask task = new HttpAsyncTask(VolunteerActivity.this);
        task.execute("http://" + Global.HOST + "/" + Global.DIR + "/getRequest.php", jsonString);
    }

    public String convertToJSON() {

        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();
            jsonText.key("msgType");
            jsonText.value(msgType);
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
            jsonArray = jsonObject.getJSONArray("requestDetails");


        } catch (Exception e) {
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