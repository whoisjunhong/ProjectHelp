package edu.nyp.projecthelp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;
import org.json.JSONStringer;

import static android.app.Activity.RESULT_OK;

public class RequestActivity extends Fragment implements OnTaskCompleted {

    Spinner serviceSpinner;

    View myView;
    String msgType;
    String name;
    String username;
    String password;
    String gender;
    String userType;
    String address;
    String unitno;
    String id;
    String status;
    Double locationLat;
    Double locationLong;
    String requestDate;
    String requestTime;
    String service;

    private final int PLACE_PICKER_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_request);
        myView = inflater.inflate(R.layout.activity_request, container, false);

        ((NavigationActivity) getActivity()).hideFloatingActionButton();

        Button requestButton = myView.findViewById(R.id.btnRequestService);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                msgType = "request";

                TextView nameTv = getActivity().findViewById(R.id.requestName);
                name = nameTv.getText().toString();

                TextView genderTv = getActivity().findViewById(R.id.requestGender);
                gender = genderTv.getText().toString();

                EditText addressEt = getActivity().findViewById(R.id.requestAddress);
                address = addressEt.getText().toString();

                serviceSpinner = getActivity().findViewById(R.id.spinService);
                service = serviceSpinner.getSelectedItem().toString();

                EditText unitEt = getActivity().findViewById(R.id.unitno);
                unitno = unitEt.getText().toString();

                EditText dateEt = getActivity().findViewById(R.id.requestDate);
                requestDate = dateEt.getText().toString();

                EditText timeEt = getActivity().findViewById(R.id.requestTime);
                requestTime = timeEt.getText().toString();


                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                if (addressEt.getText().toString().equals("")) {
                    addressEt.requestFocus();
                    addressEt.setError("Address is required!!");
                }
                else if (unitEt.getText().toString().equals("")) {
                    unitEt.requestFocus();
                    unitEt.setError("Unit No is required!!");
                }
                else if (dateEt.getText().toString().equals("")) {
                    dateEt.requestFocus();
                    dateEt.setError("Date is required!! (YYYY-MM-DD) Format");
                }
                else if (timeEt.getText().toString().equals("")) {
                    timeEt.requestFocus();
                    timeEt.setError("Time is required!!");
                }
                else {
                    // create data in JSON format
                    String jsonString = convertToJSON();
                    // call AsyncTask to perform network operation on separate thread
                    HttpAsyncTask task = new HttpAsyncTask(RequestActivity.this);
                    task.execute("http://" + Global.HOST+"/" + Global.DIR + "/requestService.php",
                            jsonString);
                }
            }
        });

        final PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Button selectLocation = (Button) myView.findViewById(R.id.btnSearchAddress);
        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                    Toast.makeText(getActivity(), "Opening place picker", Toast.LENGTH_SHORT).show();
                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
                    Toast.makeText(getActivity(), "Some google play service repairable exception thing", Toast.LENGTH_SHORT).show();
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(getActivity(), "Google Play Services not available", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((NavigationActivity) getActivity()).hideFloatingActionButton();

        // Initializing an ArrayAdapter to change text color of spinner
        serviceSpinner = getActivity().findViewById(R.id.spinService);
        ArrayAdapter<CharSequence> requestAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.arrayService, R.layout.spinner_item);
        requestAdapter.setDropDownViewResource(R.layout.spinner_item);
        serviceSpinner.setAdapter(requestAdapter);

        retrievePreferences(); // get id
        msgType = "get";

        String jsonString = convertToJSON();
        HttpAsyncTask task = new HttpAsyncTask(RequestActivity.this);
        task.execute("http://" + Global.HOST + "/" + Global.DIR + "/updateProfile.php", jsonString);
    }

    public void retrievePreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
        Log.d("Check id" ,id);
    }

    public String convertToJSON(){

        JSONStringer jsonText = new JSONStringer();
        try{
            jsonText.object();
            jsonText.key("msgType");
            jsonText.value(msgType);
            jsonText.key("id");
            jsonText.value(id);
            jsonText.key("username");
            jsonText.value(username);
            jsonText.key("password");
            jsonText.value(password);
            jsonText.key("name");
            jsonText.value(name);
            jsonText.key("address");
            jsonText.value(address);
            jsonText.key("unitno");
            jsonText.value(unitno);
            jsonText.key("gender");
            jsonText.value(gender);
            jsonText.key("usertype");
            jsonText.value(userType);
            jsonText.key("lat");
            jsonText.value(locationLat);
            jsonText.key("long");
            jsonText.value(locationLong);
            jsonText.key("requestDate");
            jsonText.value(requestDate);
            jsonText.key("requestTime");
            jsonText.value(requestTime);
            jsonText.key("serviceType");
            jsonText.value(service);

            jsonText.endObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonText.toString();
    }

    @Override
    public void onTaskCompleted(String response) {
        retrieveFromJSON(response);

        if (status.equals("SelectSuccess")) {
            TextView nameTv = getActivity().findViewById(R.id.requestName);
            nameTv.setText(name);
            TextView userTypeEt = getActivity().findViewById(R.id.requestUserType);
            userTypeEt.setText(userType);
            TextView userGender = getActivity().findViewById(R.id.requestGender);
            userGender.setText(gender);
            EditText addressEt = getActivity().findViewById(R.id.requestAddress);
            addressEt.setText(address);
            EditText unitEt = getActivity().findViewById(R.id.unitno);
            unitEt.setText(unitno);
        }
        else if (status.equals("OK")) {
            Toast.makeText(getActivity(), "Request Uploaded!", Toast.LENGTH_LONG).show();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ElderlyScheduledActivity()).commit();
        }
        else {
            System.out.println("No status retrieved");
        }
    }

    public void retrieveFromJSON(String message ){

        try {
            if (msgType.equals("get")) {
                JSONObject jsonObject = new JSONObject(message);
                name = jsonObject.getString("name");
                gender = jsonObject.getString("gender");
                address = jsonObject.getString("address");
                unitno = jsonObject.getString("unitno");
                userType = jsonObject.getString("usertype");
                status = jsonObject.getString("status");
            }
            else if (msgType.equals("request")) {
                JSONObject jsonObject = new JSONObject(message);
                status = jsonObject.getString("status");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place locationSelected = PlacePicker.getPlace(data, getActivity());
                LatLng locationLatLng = locationSelected.getLatLng();
                locationLat = locationLatLng.latitude;
                locationLong = locationLatLng.longitude;

                EditText addressEt = getActivity().findViewById(R.id.requestAddress);
                addressEt.setText(locationSelected.getName());
            }
        }
    }
}