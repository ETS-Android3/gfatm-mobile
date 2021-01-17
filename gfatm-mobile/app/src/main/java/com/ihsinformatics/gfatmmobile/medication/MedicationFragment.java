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
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrug;
import com.ihsinformatics.gfatmmobile.medication.utils.MedicationUtils;

import java.util.ArrayList;
import java.util.List;

public class MedicationFragment extends Fragment implements View.OnClickListener, MyMedicationInterface, DrugRenewListener {

    private View view;
    private Button btnMedicine;
    private Button btnMultiple;
    private AddMedicineFragment fragmentAddMedicine;
    private AddMultipleFragment fragmentAddMultiple;
    private MedicationListFragment fragmentCurrentRegimen;
    private MedicationListFragment fragmentCompleteRegimen;
    private Button btnCurrentRegimen;
    private Button btnCompleteRegimen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.medication_fragment, container, false);
        btnMedicine = view.findViewById(R.id.btnMedicine);
        btnMedicine.setVisibility(View.GONE);
        btnMultiple = view.findViewById(R.id.btnMultiple);
        btnCurrentRegimen = view.findViewById(R.id.btnCurrentTab);
        btnCompleteRegimen = view.findViewById(R.id.btnCompleteTab);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private List<DrugOrderEntity> drugOrderEntities;
    private ArrayList<DrugOrderEntity> completedDrugOrderEntities;
    private ArrayList<DrugOrderEntity> currentDrugOrderEntities;

    public void bringtoFront() {
        completedDrugOrderEntities = new ArrayList<>();
        currentDrugOrderEntities = new ArrayList<>();
        if(App.getPatient() == null) {
            btnMultiple.setEnabled(false);
        } else {
            btnMultiple.setEnabled(true);
            drugOrderEntities = DataAccess.getInstance().getDrugOrdersByPatientUUID(App.getPatient().getUuid());
            for(DrugOrderEntity e: drugOrderEntities) {
                if(MedicationUtils.isCurrentActive(e)) {
                    currentDrugOrderEntities.add(e);
                } else {
                    List<DrugOrderEntity> entities = DataAccess.getInstance().getDrugOrdersByPreviousOrderUUID(e.getUuid());
                    if(entities.size()==0)
                        completedDrugOrderEntities.add(e);
                }
            }
        }

        initFragments();
        setListeners();
        showCurrentRegimenFragment();
    }

    private void initFragments() {

        if(fragmentCurrentRegimen == null) {
            fragmentCurrentRegimen = new MedicationListFragment();
            fragmentCurrentRegimen.setOnRenewListener(this);
            Bundle bundle = new Bundle();
            bundle.putString("Medication type", "Current");
            bundle.putSerializable("drugs", currentDrugOrderEntities);
            fragmentCurrentRegimen.setArguments(bundle);
        } else {
            fragmentCurrentRegimen.updateData(currentDrugOrderEntities);
        }

        if(fragmentCompleteRegimen == null) {
            fragmentCompleteRegimen = new MedicationListFragment();
            fragmentCompleteRegimen.setOnRenewListener(this);
            Bundle bundle = new Bundle();
            bundle.putString("Medication type", "Complete");
            bundle.putSerializable("drugs", completedDrugOrderEntities);
            fragmentCompleteRegimen.setArguments(bundle);
        } else {
            fragmentCompleteRegimen.updateData(completedDrugOrderEntities);
        }

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        if(!fragmentCurrentRegimen.isAdded())
        fragmentTransaction.add(R.id.fragment_place_medication, fragmentCurrentRegimen, "Current Medication");
        if(!fragmentCompleteRegimen.isAdded())
        fragmentTransaction.add(R.id.fragment_place_medication, fragmentCompleteRegimen, "Complete Medication");
        fragmentTransaction.commit();
    }

    public void setListeners() {
        btnCurrentRegimen.setOnClickListener(this);
        btnCompleteRegimen.setOnClickListener(this);
        btnMedicine.setOnClickListener(this);
        btnMultiple.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnMedicine)
            showMedicineFragment();
        else if (view == btnMultiple)
            showMultipleFragment();
        else if (view == btnCurrentRegimen)
            showCurrentRegimenFragment();
        else if (view == btnCompleteRegimen)
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

    private void showMedicineFragment() {
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

    public void showMultipleFragment() {
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

    public void showMultipleFragment(boolean isRenew, DrugOrderEntity drugOrder) {
        try {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentAddMultiple = AddMultipleFragment.class.newInstance();
            fragmentAddMultiple.onAttachToParentFragment(MedicationFragment.this);
            Bundle bundle = new Bundle();
            bundle.putBoolean("type", isRenew);
            bundle.putSerializable("order", drugOrder);
            fragmentAddMultiple.setArguments(bundle);
            fragmentTransaction.replace(R.id.my_medication_fragment, (Fragment) fragmentAddMultiple);
            fragmentTransaction.commit();
            toggleMainPageVisibility(false);
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onSaveButtonClick() {
        bringtoFront();
        toggleMainPageVisibility(true);
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


    @Override
    public void onRenew(DrugOrderEntity drug) {
        showMultipleFragment(true, drug);
    }

    @Override
    public void onRevise(DrugOrderEntity drug) {
        showMultipleFragment(false, drug);
    }

    @Override
    public void onStop(DrugOrderEntity drug) {
        bringtoFront();
    }
}