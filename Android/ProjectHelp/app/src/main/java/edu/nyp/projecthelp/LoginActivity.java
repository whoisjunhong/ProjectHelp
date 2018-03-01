package edu.nyp.projecthelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONStringer;

public class LoginActivity extends AppCompatActivity implements OnTaskCompleted {

    Button loginBtn = null;
    EditText usernameEt = null;
    EditText passwordEt = null;

    String msgType;
    String username;
    String name;
    String password;
    String gender;
    String userType;
    String id;
    String status;
    String checkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.btnLogin);
        usernameEt = findViewById(R.id.username);
        passwordEt = findViewById(R.id.password);
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
            jsonText.key("password");
            jsonText.value(password);
            jsonText.key("gender");
            jsonText.value(gender);
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

    // Retrieve profile information from JSON string
    public void retrieveFromJSON(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            msgType = jsonObject.getString("type");
            status = jsonObject.getString("status");
            if (status.equals("OK")) {
                if (msgType.equals(Global.REQ_DOWNLOAD)) {
                    username = jsonObject.getString("username");
                    name = jsonObject.getString("name");
                    password = jsonObject.getString("password");
                    gender = jsonObject.getString("gender");
                    userType = jsonObject.getString("usertype");
                }
                else if (msgType.equals(Global.REQ_UPLOAD)) {
                    id = jsonObject.getString("id");
                    username = jsonObject.getString("username");
                    name = jsonObject.getString("name");
                    userType = jsonObject.getString("usertype");
                    checkLogin = jsonObject.getString("checkLogin");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handler for upload button click event
    public void loginClick(View v) {
        // convert edit text values to string data
        msgType = Global.REQ_UPLOAD;
        EditText usernameEt = (EditText)findViewById(R.id.username);
        EditText passwordEt = (EditText)findViewById(R.id.password);
        username = usernameEt.getText().toString();
        password = getMD5(passwordEt.getText().toString());

        if (usernameEt.getText().toString().equals("")) {
            usernameEt.requestFocus();
            usernameEt.setError("Username is required!!");
        }
        else if (passwordEt.getText().toString().equals("")) {
            passwordEt.requestFocus();
            passwordEt.setError("Password is required!!");
        }
        else {
            // create data in JSON format
            String jsonString = convertToJSON();
            // call AsyncTask to perform network operation on separate thread
            HttpAsyncTask task = new HttpAsyncTask(this);
            task.execute("http://" + Global.HOST+"/" + Global.DIR + "/retrieveProfile.php",
                    jsonString);
        }
    }

    // Handler for HTTPAsyncTask complete event
    @Override
    public void onTaskCompleted(String response) {
        retrieveFromJSON(response);
        // if response is from upload request
        if (status.equals("OK")) {
            SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("id", id);
            editor.putString("userType", userType);
            editor.putString("checkLogin", checkLogin);

            editor.commit();

            Toast.makeText(getApplicationContext(), "Welcome back, " + name  + "!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setData(Uri.parse("LoginSuccess"));
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Login failed! \n Try again :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void intentToRegister(View v) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    @Override
    public void onBackPressed() {
        // Blocks user from pressing back button
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
