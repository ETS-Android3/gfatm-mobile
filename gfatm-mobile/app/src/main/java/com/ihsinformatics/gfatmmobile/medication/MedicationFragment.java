package com.ihsinformatics.gfatmmobile.medication;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.AddTestFragment;
import com.ihsinformatics.gfatmmobile.commonlab.LabFragment;
import com.ihsinformatics.gfatmmobile.commonlab.LabTestAdapter;
import com.ihsinformatics.gfatmmobile.commonlab.LabTestsFragment;

import java.util.ArrayList;

public class MedicationFragment extends Fragment implements View.OnClickListener, MyMedicationInterface{

    private View view;
    private TextView tvNumberOfMedication;
    private Button btnMedicine;
    private Button btnMultiple;
    private AddMedicineFragment fragmentAddMedicine;
    private AddMultipleFragment fragmentAddMultiple;
    private MedicationListFragment fragmentCurrentRegimen;
    private MedicationListFragment fragmentCompleteRegimen;
    private Button btnCurrentRegimen;
    private Button btnCompleteRegimen;

    @Override
    public void onClick(View view) {
        if(view == btnMedicine)
            showMedicineFragment();
        else if(view == btnMultiple)
            showMultipleFragment();
        else if(view == btnCurrentRegimen)
            showCurrentRegimenFragment();
        else if(view == btnCompleteRegimen)
            showCompleteRegimenFragment();
    }

    public void showCurrentRegimenFragment() {

        btnCurrentRegimen.setTextColor(App.getColor(getActivity(), R.attr.colorPrimaryDark));
        btnCurrentRegimen.setBackgroundResource(R.drawable.selected_border_button);

        btnCompleteRegimen.setTextColor(getResources().getColor(R.color.dark_grey));
        btnCompleteRegimen.setBackgroundResource(R.drawable.border_button);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.show(fragmentCurrentRegimen);
        fragmentTransaction.hide(fragmentCompleteRegimen);
        fragmentTransaction.commit();
    }

    public void showCompleteRegimenFragment() {

        btnCurrentRegimen.setTextColor(getResources().getColor(R.color.dark_grey));
        btnCurrentRegimen.setBackgroundResource(R.drawable.border_button);

        btnCompleteRegimen.setTextColor(App.getColor(getActivity(), R.attr.colorPrimaryDark));
        btnCompleteRegimen.setBackgroundResource(R.drawable.selected_border_button);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragmentCurrentRegimen);
        fragmentTransaction.show(fragmentCompleteRegimen);
        fragmentTransaction.commit();
    }

    public void toggleMainPageVisibility(boolean visibility) {
        view.findViewById(R.id.layoutMedicationMain).setVisibility(visibility ? View.VISIBLE : View.GONE);
        if (visibility) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            if (fragmentAddMedicine != null)
                fragmentTransaction.remove(fragmentAddMedicine);
            if (fragmentAddMultiple != null)
                fragmentTransaction.remove(fragmentAddMultiple);
            fragmentTransaction.commit();
        }
    }

    private void showMedicineFragment(){
        try {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentAddMedicine = AddMedicineFragment.class.newInstance();
            fragmentAddMedicine.onAttachToParentFragment(MedicationFragment.this);
            fragmentTransaction.replace(R.id.my_medication_fragment, (Fragment) fragmentAddMedicine);
            fragmentTransaction.commit();
            toggleMainPageVisibility(false);
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void showMultipleFragment(){
        try {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentAddMultiple = AddMultipleFragment.class.newInstance();
            fragmentAddMultiple.onAttachToParentFragment(MedicationFragment.this);
            fragmentTransaction.replace(R.id.my_medication_fragment, (Fragment) fragmentAddMultiple);
            fragmentTransaction.commit();
            toggleMainPageVisibility(false);
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.medication_fragment, container, false);

        tvNumberOfMedication = view.findViewById(R.id.tvNumberOfMedication);
        btnMedicine = view.findViewById(R.id.btnMedicine);
        btnMultiple = view.findViewById(R.id.btnMultiple);
        btnCurrentRegimen = view.findViewById(R.id.btnCurrentTab);
        btnCompleteRegimen = view.findViewById(R.id.btnCompleteTab);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNumberOfMedication.setText("Medications");

        initFragments();
        setListeners();
        showCurrentRegimenFragment();
    }

    private void initFragments(){
        fragmentCurrentRegimen = new MedicationListFragment();
        fragmentCurrentRegimen.onAttachToParentFragment(this);
        Bundle bundle = new Bundle();
        bundle.putString("Medication type", "Current");
        /*ArrayList pendingData = new ArrayList();
        pendingData.addAll(pendingTests);
        bundle.putSerializable("data", pendingData);*/
        fragmentCurrentRegimen.setArguments(bundle);

        fragmentCompleteRegimen = new MedicationListFragment();
        fragmentCompleteRegimen.onAttachToParentFragment(this);
        bundle = new Bundle();
        bundle.putString("Medication type", "Complete");
        /*ArrayList completeData = new ArrayList();
        completeData.addAll(completedTests);
        bundle.putSerializable("data", completeData);*/
        fragmentCompleteRegimen.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_place_medication, fragmentCurrentRegimen, "Current Medication");
        fragmentTransaction.add(R.id.fragment_place_medication, fragmentCompleteRegimen, "Complete Medication");
        fragmentTransaction.commit();
    }

    public void setListeners(){
        btnCurrentRegimen.setOnClickListener(this);
        btnCompleteRegimen.setOnClickListener(this);
        btnMedicine.setOnClickListener(this);
        btnMultiple.setOnClickListener(this);
    }

    @Override
    public void onCancelButtonClick() {
        int color = App.getColor(getActivity(), R.attr.colorAccent);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.dialog).create();
        alertDialog.setMessage(getString(R.string.warning_before_close_adding_test));
        Drawable backIcon = getResources().getDrawable(R.drawable.ic_back);
        backIcon.setAutoMirrored(true);
        DrawableCompat.setTint(backIcon, color);
        alertDialog.setIcon(backIcon);
        alertDialog.setTitle(getResources().getString(R.string.back_to_medications));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        toggleMainPageVisibility(true);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));
    }

    public boolean isAddMedicineScreenVisible() {
        if (fragmentAddMedicine != null)
            return fragmentAddMedicine.isVisible();
        return false;
    }

    public boolean isAddMultipleScreenVisible() {
        if (fragmentAddMultiple != null)
            return fragmentAddMultiple.isVisible();
        return false;
    }

}
