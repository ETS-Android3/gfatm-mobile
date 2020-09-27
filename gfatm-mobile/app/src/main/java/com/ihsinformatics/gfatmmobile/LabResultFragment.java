package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LabResultFragment extends Fragment {

    TextView tv;
    String resultType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.lab_results_fragment, container, false);
        tv = mainContent.findViewById(R.id.tv);
        resultType = getArguments().getString("result_type");
        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv.setText(resultType);
    }

}
