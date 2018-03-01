package edu.nyp.projecthelp;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONStringer;

public class ProfileActivity extends Fragment implements OnTaskCompleted {

    View myView;

    // Declare variables for profile and response
    String msgType;
    String username;
    String password;
    String gender;
    String userType;
    String address;
    String unitno;
    String id;
    String status;
    String updateStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_profile, container, false);
        ((NavigationActivity) getActivity()).showFloatingActionButton();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_profile);

        Button mButton = (Button) myView.findViewById(R.id.btnSaveChanges);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here you set what you want to do when user clicks your button,
                // EDIT do something

                msgType = "update";

                EditText usernameEt = getActivity().findViewById(R.id.profileUsername);
                username = usernameEt.getText().toString();

                EditText addressEt = getActivity().findViewById(R.id.profileAddress);
                address = addressEt.getText().toString();

                EditText unitEt = getActivity().findViewById(R.id.unitno);
                unitno = unitEt.getText().toString();

                EditText oldPassword = getActivity().findViewById(R.id.profileOldPassword);
                String etOldPassword = getMD5(oldPassword.getText().toString());
                oldPassword.setHintTextColor(Color.WHITE);

                EditText newPassword = getActivity().findViewById(R.id.profilePassword);
                String editedPassword = getMD5(newPassword.getText().toString());
                newPassword.setHintTextColor(Color.WHITE);

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                if (usernameEt.getText().toString().equals("")) {
                    usernameEt.requestFocus();
                    usernameEt.setError("Username is required!!");
                }
                else if (usernameEt.getText().toString().contains(" ")) {
                    usernameEt.requestFocus();
                    usernameEt.setError("Username cannot contain spaces!!");
                }
                else if (addressEt.getText().toString().equals("")) {
                    addressEt.requestFocus();
                    addressEt.setError("Address is required!!");
                }
                else if (unitEt.getText().toString().equals("")) {
                    unitEt.requestFocus();
                    unitEt.setError("Unit No is required!!");
                }
                else if (oldPassword.getText().toString().equals("")) {
                    oldPassword.requestFocus();
                    oldPassword.setError("Current password is required!!");
                }
                else if (newPassword.getText().toString().equals("")) {
                    newPassword.requestFocus();
                    newPassword.setError("New password is required!!");
                }
                else {
                    try {
                        if(etOldPassword.equals(password)){
                            password = editedPassword;
                            String jsonString = convertToJSON();
                            HttpAsyncTask task = new HttpAsyncTask(ProfileActivity.this);
                            task.execute("http://" + Global.HOST + "/" + Global.DIR + "/updateProfile.php", jsonString);
                            Toast.makeText(getActivity().getApplicationContext(),"Edit Successful!!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity().getApplicationContext(),"Current Password is wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        retrievePreferences(); // get id
        msgType = "get";

        String jsonString = convertToJSON();
        HttpAsyncTask task = new HttpAsyncTask(ProfileActivity.this);
        task.execute("http://" + Global.HOST + "/" + Global.DIR + "/updateProfile.php", jsonString);

        EditText oldPassword = getActivity().findViewById(R.id.profileOldPassword);
        oldPassword.setHintTextColor(Color.LTGRAY);
        EditText newPassword = getActivity().findViewById(R.id.profilePassword);
        newPassword.setHintTextColor(Color.LTGRAY);
    }

    public void retrievePreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        id = prefs.getString("id", "");
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
            jsonText.key("address");
            jsonText.value(address);
            jsonText.key("unitno");
            jsonText.value(unitno);
            jsonText.endObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonText.toString();
    }

    public void retrieveFromJSON(String message ){

        try {
            JSONObject jsonObject = new JSONObject(message);
            id = jsonObject.getString("id");
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
            gender = jsonObject.getString("gender");
            address = jsonObject.getString("address");
            unitno = jsonObject.getString("unitno");
            userType = jsonObject.getString("usertype");
            status = jsonObject.getString("status");
            updateStatus = jsonObject.getString("updateStatus");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskCompleted(String response) {
        retrieveFromJSON(response);

        if (status.equals("SelectSuccess")) {
            EditText usernameEt = getActivity().findViewById(R.id.profileUsername);
            usernameEt.setText(username);
            TextView userTypeEt = getActivity().findViewById(R.id.profileUserType);
            userTypeEt.setText(userType);
            TextView userGender = getActivity().findViewById(R.id.profileGender);
            userGender.setText(gender);
            EditText addressEt = getActivity().findViewById(R.id.profileAddress);
            addressEt.setText(address);
            EditText unitEt = getActivity().findViewById(R.id.unitno);
            unitEt.setText(unitno);
        }
        else {
            System.out.println("No status retrieved");
        }
    }

    public String getMD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
