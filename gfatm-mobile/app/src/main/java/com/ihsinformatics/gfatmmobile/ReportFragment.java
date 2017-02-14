package com.ihsinformatics.gfatmmobile;

/**
 * Created by Rabbia on 11/10/2016.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.util.ServerService;


public class ReportFragment extends Fragment {

    Context context;

    LinearLayout reportLayout;

    ServerService serverService;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View mainContent = inflater.inflate(
                R.layout.report_fragment, container, false);

        context = mainContent.getContext();

        serverService = new ServerService(context.getApplicationContext());

        reportLayout = (LinearLayout) mainContent.findViewById(R.id.reportFragment);

        fillReportFragment();

        return mainContent;
    }

    private void fillReportFragment() {

        Object[][] encounters = serverService.getAllEncounterFromLocalDB();

        if (encounters != null) {
            for (int i = 0; i < encounters.length; i++) {

                TextView tv = new TextView(context);
                tv.setText(String.valueOf(encounters[i][0]));
                reportLayout.addView(tv);
            }
        }



    }
}
