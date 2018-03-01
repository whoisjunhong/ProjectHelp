package edu.nyp.projecthelp;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutActivity extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ((NavigationActivity) getActivity()).showFloatingActionButton();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_about);
        ((NavigationActivity) getActivity()).showFloatingActionButton();
        myView = inflater.inflate(R.layout.activity_about, container, false);
        return myView;
    }
}
