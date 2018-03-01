package edu.nyp.projecthelp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONObject;
import org.json.JSONStringer;


public class RegistrationActivity extends AppCompatActivity implements OnTaskCompleted {

    Button registerBtn = null;
    EditText nameEt = null;
    EditText usernameEt = null;
    EditText passwordEt = null;
    Spinner genderSpinner = null;
    EditText addressEt = null;
    EditText unitEt = null;
    Spinner userTypeSpinner = null;

    // Declare variables for profile and response
    String msgType;
    String username;
    String name;
    String password;
    String gender;
    String address;
    String unitno;
    String userType;
    String id;
    String status;
    String checkLogin;

    private final int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_registration);

        registerBtn = findViewById(R.id.btnRegister);
        usernameEt = findViewById(R.id.username);
        nameEt = findViewById(R.id.name);
        passwordEt = findViewById(R.id.password);
        genderSpinner = findViewById(R.id.spinGender);
        addressEt = findViewById(R.id.address);
        unitEt = findViewById((R.id.unitno));
        userTypeSpinner = findViewById(R.id.spinUserType);

        // Initializing an ArrayAdapter to change text color of spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.arrayGender, R.layout.spinner_item);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(
                this, R.array.arrayUserType, R.layout.spinner_item);
        userTypeAdapter.setDropDownViewResource(R.layout.spinner_item);
        userTypeSpinner.setAdapter(userTypeAdapter);

    }

    // Convert profile information to JSON string
    public String convertToJSON() {
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();
            jsonText.key("type");
            jsonText.value(msgType);
            jsonText.key("username");
            jsonText.value(username);
            jsonText.key("name");
            jsonText.value(name);
            jsonText.key("password");
            jsonText.value(password);
            jsonText.key("gender");
            jsonText.value(gender);
            jsonText.key("address");
            jsonText.value(address);
            jsonText.key("unitno");
            jsonText.value(unitno);
            jsonText.key("usertype");
            jsonText.value(userType);
            jsonText.key("id");
            jsonText.value(id);
            jsonText.endObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonText.toString();
    }

    // Handler for upload button click event
    public void registerClick(View v){
        // convert edit text values to string data
        msgType = Global.REQ_UPLOAD;
        EditText usernameEt = findViewById(R.id.username);
        username = usernameEt.getText().toString();
        EditText nameEt = findViewById(R.id.name);
        name = nameEt.getText().toString();
        EditText passwordEt = findViewById(R.id.password);
        password = getMD5(passwordEt.getText().toString());
        Spinner genderSpinner = findViewById(R.id.spinGender);
        gender = genderSpinner.getSelectedItem().toString();
        EditText addressEt = findViewById(R.id.address);
        address = addressEt.getText().toString();
        EditText unitEt = findViewById(R.id.unitno);
        unitno = unitEt.getText().toString();
        Spinner userTypeSpinner = findViewById(R.id.spinUserType);
        userType = userTypeSpinner.getSelectedItem().toString();

        if (nameEt.getText().toString().equals("")) {
            nameEt.requestFocus();
            nameEt.setError("Name is required!!");
        }
        else if (usernameEt.getText().toString().equals("")) {
            usernameEt.requestFocus();
            usernameEt.setError("Username is required!!");
        }
        else if (usernameEt.getText().toString().contains(" ")) {
            usernameEt.requestFocus();
            usernameEt.setError("Username cannot contain spaces!!");
        }
        else if (passwordEt.getText().toString().equals("")) {
            passwordEt.requestFocus();
            passwordEt.setError("Password is required!!");
        }
        else if (addressEt.getText().toString().equals("")) {
            addressEt.requestFocus();
            addressEt.setError("Address is required!!");
        }
        else if (unitEt.getText().toString().equals("")) {
            unitEt.requestFocus();
            unitEt.setError("Unit No is required!!");
        }
        else {
            // create data in JSON format
            String jsonString = convertToJSON();
            // call AsyncTask to perform network operation on separate thread
            HttpAsyncTask task = new HttpAsyncTask(this);
            task.execute("http://" + Global.HOST+"/" + Global.DIR + "/registerProfile.php",
                    jsonString);
        }
    }

    // Retrieve profile information from JSON string
    public void retrieveFromJSON(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            msgType = jsonObject.getString("type");
            status = jsonObject.getString("status");
            if (status.equals("OK")) {
                if (msgType.equals(Global.REQ_UPLOAD)) {
                    id = jsonObject.getString("id");
                    username = jsonObject.getString("username");
                    name = jsonObject.getString("name");
                    password = jsonObject.getString("password");
                    gender = jsonObject.getString("gender");
                    address = jsonObject.getString("address");
                    unitno = jsonObject.getString("unitno");
                    userType = jsonObject.getString("usertype");
                    checkLogin = jsonObject.getString("checkLogin");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handler for HTTPAsyncTask complete event
    @Override
    public void onTaskCompleted(String response) {
        retrieveFromJSON(response);
        // if response is from upload request
        if (status.equals("OK")) {
            try {
                SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("id", id);
                editor.putString("checkLogin", checkLogin);

                editor.commit();

                Toast.makeText(getApplicationContext(), "Profile Created Successfully! \nWelcome " + name + "!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, NavigationActivity.class));
            }
            catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Profile creation failed, please try again later! :(", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Profile creation failed, please try again later! :(", Toast.LENGTH_SHORT).show();
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

    // Prevent creating multiple intents
    @Override
    public void onBackPressed() {
        finish();
    }

    public void intentToLogin(View v) {
        finish();
    }
}
