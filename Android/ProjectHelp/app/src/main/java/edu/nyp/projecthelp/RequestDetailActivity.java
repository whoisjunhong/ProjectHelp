package edu.nyp.projecthelp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONStringer;


public class RequestDetailActivity extends AppCompatActivity implements OnTaskCompleted {

    ElderlyRequest requestDetail;
    String msgType;
    String currentUser;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        requestDetail = (ElderlyRequest) getIntent().getSerializableExtra("requestDetails") ;

        populateActivity();
    }

    private void populateActivity() {

        TextView nameTv = findViewById(R.id.tvName);
        nameTv.setText(requestDetail.requestee);
        TextView typeTv = findViewById(R.id.tvRequestType);
        typeTv.setText(requestDetail.type);
        TextView genderTv = findViewById(R.id.tvGender);
        genderTv.setText(requestDetail.gender);
        TextView addressTv = findViewById(R.id.tvAddress);
        SpannableString content = new SpannableString(requestDetail.address);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        addressTv.setText(content);
        TextView unitNoTv = findViewById(R.id.tvUnitNo);
        unitNoTv.setText("#" + requestDetail.unitno);
        TextView requestDateTv = findViewById(R.id.tvRequestDate);
        requestDateTv.setText(requestDetail.requestDate);
        TextView requestTimeTv = findViewById(R.id.tvRequestTime);
        requestTimeTv.setText(requestDetail.requestTime);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void btnRequestLocation(View v) {

        if (requestDetail.locationLat.equals("") && requestDetail.locationLong.equals("")) {
            String strUri = "http://maps.google.com/maps?q="  + requestDetail.address;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            startActivity(intent);
        }
        else {
            String strUri = "http://maps.google.com/maps?q=loc:" + requestDetail.locationLat + "," + requestDetail.locationLong + " (" + requestDetail.address + ")";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            startActivity(intent);
        }
    }

    public void applyClick(View v) {

        retrievePreferences();
        msgType = "apply";

        String jsonString = convertToJSON();
        HttpAsyncTask task = new HttpAsyncTask(this);
        task.execute("http://" + Global.HOST + "/" + Global.DIR + "/apply.php", jsonString);
    }

    public void retrievePreferences() {
        SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUser = prefs.getString("id", "");
    }

    public String convertToJSON() {

        JSONStringer jsonText = new JSONStringer();

        try {
            jsonText.object();
            jsonText.key("msgType");
            jsonText.value(msgType);
            jsonText.key("requestId");
            jsonText.value(requestDetail.requestId);
            Log.d("requestId", String.valueOf(requestDetail.requestId));
            jsonText.key("aceptee");
            Log.d("currentUser", currentUser);
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
            status = jsonObject.getString("status");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskCompleted(String response) {
        retrieveFromJSON(response);
        if (status.equals("OK")) {
            Toast.makeText(this, "Application Successful!!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
