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

public class MedicationFragment extends Fragment implements View.OnClickListener, MyMedicationInterface{

    private View view;
    private MedicationAdapter adapter;
    private RecyclerView rvMedications;
    private TextView tvNumberOfMedication;
    private Button btnMedicine;
    private Button btnMultiple;
    private AddMedicineFragment fragmentAddMedicine;
    private AddMultipleFragment fragmentAddMultiple;

    @Override
    public void onClick(View view) {
        if(view == btnMedicine)
            showMedicineFragment();
        else if(view == btnMultiple)
            showMultipleFragment();
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
        rvMedications = view.findViewById(R.id.rvMedications);
        btnMedicine = view.findViewById(R.id.btnMedicine);
        btnMultiple = view.findViewById(R.id.btnMultiple);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMedications.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MedicationAdapter(getActivity());
        rvMedications.setAdapter(adapter);

        tvNumberOfMedication.setText(String.valueOf(adapter.getItemCount()) + " medications listed");


        setListeners();
    }

    public void setListeners(){
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
