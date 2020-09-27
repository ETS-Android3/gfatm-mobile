package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LabFragment extends Fragment implements View.OnClickListener {

    private View mainview;
    public LabResultFragment fragmentIncompleteLabs;
    public LabResultFragment fragmentCompleteLabs;
    Button incompleteLabButton;
    Button completeLabButton;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.lab_fragment, container, false);
        incompleteLabButton = mainview.findViewById(R.id.incompleteButton);
        completeLabButton = mainview.findViewById(R.id.completeButton);

        fragmentIncompleteLabs = new LabResultFragment();
        bundle = new Bundle();
        bundle.putString("result_type", "INCOMPLETE");
        fragmentIncompleteLabs.setArguments(bundle);

        fragmentCompleteLabs = new LabResultFragment();
        bundle = new Bundle();
        bundle.putString("result_type", "COMPLETE");
        fragmentCompleteLabs.setArguments(bundle);

        return mainview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        incompleteLabButton.setOnClickListener(this);
        completeLabButton.setOnClickListener(this);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_place, fragmentIncompleteLabs, "INCOMPLETE_LABS");
        fragmentTransaction.add(R.id.fragment_place, fragmentCompleteLabs, "COMPLETE_LABS");
        fragmentTransaction.commit();

        showIncompleteLabsFragment();
    }

    @Override
    public void onClick(View v) {
        if (v == incompleteLabButton)
            showIncompleteLabsFragment();
        else if (v == completeLabButton)
            showCompleteLabsFragment();
    }

    public void showIncompleteLabsFragment(){
        int color = App.getColor(getActivity(), R.attr.colorPrimaryDark);

        incompleteLabButton.setTextColor(color);
        incompleteLabButton.setBackgroundResource(R.drawable.selected_border_button);

        completeLabButton.setTextColor(getResources().getColor(R.color.dark_grey));
        completeLabButton.setBackgroundResource(R.drawable.border_button);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.show(fragmentIncompleteLabs);
        fragmentTransaction.hide(fragmentCompleteLabs);
        fragmentTransaction.commit();
    }

    public void showCompleteLabsFragment(){
        int color = App.getColor(getActivity(), R.attr.colorPrimaryDark);

        incompleteLabButton.setTextColor(getResources().getColor(R.color.dark_grey));
        incompleteLabButton.setBackgroundResource(R.drawable.border_button);

        completeLabButton.setTextColor(color);
        completeLabButton.setBackgroundResource(R.drawable.selected_border_button);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragmentIncompleteLabs);
        fragmentTransaction.show(fragmentCompleteLabs);
        fragmentTransaction.commit();
    }
}