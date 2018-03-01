package edu.nyp.projecthelp;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONStringer;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnTaskCompleted {

    // Declare variables for profile and response
    String msgType;
    String name;
    String password;
    String gender;
    String userType;
    String username;
    String id;
    String status;
    String checkLogin;
    String jsonString;

    final int REQUESTCODE = 1;

    FragmentManager fragmentManager = getFragmentManager();
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeActivity()).commit();
        }

        // Hide keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Bottom right toolbar shortcut (Put as add request in future development)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new RequestActivity()).commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        checkLogin();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == this.REQUESTCODE) {
            if(resultCode == RESULT_OK) {
                String status = intent.getData().toString();
                if (status.equals("LoginSuccess")) {
                    checkLogin();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "No result code", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No request code", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
    };

    public void showFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        this.menu = menu;
        return true;
    }

    /*public void onResume() {
        super.onResume();

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void checkLogin() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);

        msgType = Global.REQ_CHECKLOGIN;

        if(prefs.contains("id")) {
            id = prefs.getString("id", null);
            checkLogin = prefs.getString("checkLogin", null);

            jsonString = convertToJSON();
            HttpAsyncTask task = new HttpAsyncTask(this);
            task.execute("http://" + Global.HOST + "/" + Global.DIR + "/checkLogin.php", jsonString);
        }
        else {
            startActivityForResult(new Intent(NavigationActivity.this, LoginActivity.class), REQUESTCODE);
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //here toolbar is your id in xml

        if (id == R.id.nav_request) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RequestActivity()).commit();
        }
        else if (id == R.id.nav_elderly_scheduled) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ElderlyScheduledActivity()).commit();
        }
        else if (id == R.id.nav_volunteer_scheduled) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new VolunteerScheduledActivity()).commit();
        }
        else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutActivity()).commit();
        }
        else if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeActivity()).commit();
        }
        else if (id == R.id.nav_profile) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileActivity()).commit();
        }
        else if (id == R.id.nav_volunteer) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new VolunteerActivity()).commit();
        }
        else if (id == R.id.nav_counter) {
            startActivity(new Intent(this, CounterActivity.class));
        }
        else if (id == R.id.nav_logout) {
            logoutUser();
            finish();
            startActivity(new Intent(NavigationActivity.this, NavigationActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Retrieve profile information from JSON string
    public void retrieveFromJSON(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            msgType = jsonObject.getString("type");
            status = jsonObject.getString("status");
            if (msgType.equals(Global.REQ_CHECKLOGIN)) {
                username = jsonObject.getString("username");
                name = jsonObject.getString("name");
                userType = jsonObject.getString("usertype");
                gender = jsonObject.getString("gender");
            } else if (msgType.equals(Global.REQ_UPLOAD)) {
                id = jsonObject.getString("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMenu() {
        if (userType != null) {
            if (userType.equals("Volunteer")) {
                NavigationView navigationView = findViewById(R.id.nav_view);
                Menu hideMenu = navigationView.getMenu();
                hideMenu.findItem(R.id.nav_request).setVisible(false);
                hideMenu.findItem(R.id.nav_elderly_scheduled).setVisible(false);
            }
            else if (userType.equals("Elderly")) {
                NavigationView navigationView = findViewById(R.id.nav_view);
                Menu hideMenu = navigationView.getMenu();
                hideMenu.findItem(R.id.nav_volunteer).setVisible(false);
                hideMenu.findItem(R.id.nav_volunteer_scheduled).setVisible(false);
            }
        }
    }

    // Handler for HTTPAsyncTask complete event
    @Override
    public void onTaskCompleted(String response) {
        retrieveFromJSON(response);

        // if response is from upload request
        if(msgType.equals(Global.REQ_CHECKLOGIN)) {
            if (status.equals("OK")) {
                setMenu();
                NavigationView navigationView = findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);

                TextView tvName = headerView.findViewById(R.id.navName);
                tvName.setText(name);
                TextView tvUserType = headerView.findViewById(R.id.navUserType);
                tvUserType.setText(userType);
                TextView tvHomeName = findViewById(R.id.welcomeHome);
                tvHomeName.setText("Hi " + name + "!");
            }
            else {
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = user.edit();

                editor.clear();
                editor.commit();

                Toast.makeText(this, "Multiple login detected! Login again to continue!", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(NavigationActivity.this, LoginActivity.class), REQUESTCODE);
            }
        }
    }

    // Convert profile information to JSON string
    public String convertToJSON() {
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();
            jsonText.key("type");
            jsonText.value(msgType);
            jsonText.key("name");
            jsonText.value(name);
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
            jsonText.key("checkLogin");
            jsonText.value(checkLogin);
            jsonText.endObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonText.toString();
    }

    public void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

        editor.commit();
    }


}
