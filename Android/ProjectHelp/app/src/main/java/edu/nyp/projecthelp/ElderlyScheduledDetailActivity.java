package edu.nyp.projecthelp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONStringer;

public class ElderlyScheduledDetailActivity extends AppCompatActivity implements OnTaskCompleted {

    ElderlyRequest elderlyScheduledDetails;
    String msgType;
    String currentUser;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_scheduled_detail);

        elderlyScheduledDetails = (ElderlyRequest) getIntent().getSerializableExtra("elderlyScheduledDetails");

        populateActivity();
    }

    private void populateActivity() {

        TextView nameTv = findViewById(R.id.tvName);
        nameTv.setText(elderlyScheduledDetails.requestee);
        TextView typeTv = findViewById(R.id.tvRequestType);
        typeTv.setText(elderlyScheduledDetails.type);
        TextView genderTv = findViewById(R.id.tvGender);
        genderTv.setText(elderlyScheduledDetails.gender);
        TextView addressTv = findViewById(R.id.tvAddress);
        SpannableString content = new SpannableString(elderlyScheduledDetails.address);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        addressTv.setText(content);
        TextView unitNoTv = findViewById(R.id.tvUnitNo);
        unitNoTv.setText("#" + elderlyScheduledDetails.unitno);
        TextView requestDateTv = findViewById(R.id.tvRequestDate);
        requestDateTv.setText(elderlyScheduledDetails.requestDate);
        TextView requestTimeTv = findViewById(R.id.tvRequestTime);
        requestTimeTv.setText(elderlyScheduledDetails.requestTime);
        Button submitBtn = findViewById(R.id.btnSubmit);
        if (elderlyScheduledDetails.status.equals("T")) {
            submitBtn.setText("Delete Request");
        }
        else if (elderlyScheduledDetails.status.equals("P")) {
            submitBtn.setText("Mark as Completed");
        }
        else if (elderlyScheduledDetails.status.equals("F")) {
            submitBtn.setEnabled(false);
            submitBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void btnRequestLocation(View v) {

        if (elderlyScheduledDetails.locationLat.equals("") && elderlyScheduledDetails.locationLong.equals("")) {
            String strUri = "http://maps.google.com/maps?q="  + elderlyScheduledDetails.address;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            startActivity(intent);
        }
        else {
            String strUri = "http://maps.google.com/maps?q=loc:" + elderlyScheduledDetails.locationLat + "," + elderlyScheduledDetails.locationLong + " (" + elderlyScheduledDetails.address + ")";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            startActivity(intent);
        }

    }

    public void btnSubmit(View v) {

        retrievePreferences();
        if (elderlyScheduledDetails.status.equals("T")) {
            msgType = "delete";

            String jsonString = convertToJSON();
            HttpAsyncTask task = new HttpAsyncTask(this);
            task.execute("http://" + Global.HOST + "/" + Global.DIR + "/deleteRequest.php", jsonString);
        }
        else if (elderlyScheduledDetails.status.equals("P")) {
            msgType = "completed";

            String jsonString = convertToJSON();
            HttpAsyncTask task = new HttpAsyncTask(this);
            task.execute("http://" + Global.HOST + "/" + Global.DIR + "/markAsCompleted.php", jsonString);
        }

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
            jsonText.value(elderlyScheduledDetails.requestId);
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
        if(status.equals("OK")) {
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            Toast.makeText(this, "Unable to complete action!", Toast.LENGTH_LONG).show();
        }
    }
}
