package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * Created by Rabbia on 11/10/2016.
 */

public class BlankFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment

        View mainContent = inflater.inflate(
                R.layout.fragment_blank, container, false);

        return mainContent;
    }
}