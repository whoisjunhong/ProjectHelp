package edu.nyp.projecthelp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeActivity extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ((NavigationActivity) getActivity()).showFloatingActionButton();
        myView = inflater.inflate(R.layout.activity_home, container, false);
        Button btnToAbout = (Button) myView.findViewById(R.id.btnAboutUs);
        btnToAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AboutActivity();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button btnToBooking = (Button) myView.findViewById(R.id.btnNearbyPolice);
        btnToBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUri = "https://www.google.com/maps/search/?api=1&query=Police+Station+Singapore";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);
            }
        });

        Button btnToProfile = (Button) myView.findViewById(R.id.btnProfile);
        btnToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileActivity();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button btnToCounter = (Button) myView.findViewById(R.id.btnCounter);
        btnToCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CounterActivity.class));
            }
        });
        return myView;
    }
}
