package com.ihsinformatics.gfatmmobile;

/**
 * Created by Rabbia on 11/10/2016.
 */

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.shared.FormsObject;
import com.ihsinformatics.gfatmmobile.shared.Roles;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FormFragment extends Fragment{

    private LinearLayout prgramForms;
    //private LinearLayout commonForms;
    private LinearLayout mainContent;
    private View mainview;
    //private TextView program;
    //private TextView common;

    public static ServerService serverService;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mainview = inflater.inflate(R.layout.form_fragment, container, false);

        mainContent = (LinearLayout) mainview.findViewById(R.id.mainContent);
        prgramForms = (LinearLayout) mainview.findViewById(R.id.content_main);
        //commonForms = (LinearLayout) mainview.findViewById(R.id.common_form);
        //program = (TextView) mainview.findViewById(R.id.program);
       // common = (TextView) mainview.findViewById(R.id.common);

        serverService = new ServerService(mainContent.getContext());

        //fillCommonFormContent();
        fillProgramFormContent();

        return mainview;
    }


    public void showMainContent(Boolean flag){

        if(flag){
            prgramForms.setVisibility(View.VISIBLE);
            //commonForms.setVisibility(View.VISIBLE);
            //program.setVisibility(View.VISIBLE);
           // common.setVisibility(View.VISIBLE);
        }else {
            prgramForms.setVisibility(View.GONE);
            //commonForms.setVisibility(View.GONE);
            //program.setVisibility(View.GONE);
            //common.setVisibility(View.GONE);
        }


    }

    /*public void fillCommonFormContent() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_form, new BlankFragment());
        fragmentTransaction.commit();
        commonForms.setVisibility(View.VISIBLE);
        commonForms.removeAllViews();

        ArrayList<FormsObject> forms = Forms.getCommonFormList();

        ArrayList<FormsObject> formsShown = new ArrayList<FormsObject>();
        for (int i = 0; i < forms.size(); i++) {
            final FormsObject form = forms.get(i);

            Boolean add = false;

            if (!App.getRoles().contains(Roles.DEVELOPER)) {
                for (int k = 0; k < form.getRoles().length; k++) {
                    String role = form.getRoles()[k];
                    if (App.getRoles().contains(role)) {
                        add = true;
                        break;
                    }
                }
            } else
                add = true;

            if (add)
                formsShown.add(form);
        }

        int formsInRow = 3;

        for (int i = 0; i < formsShown.size(); i++) {

            LinearLayout layout = new LinearLayout(commonForms.getContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            layout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

            for (int j = 0; j < formsInRow; j++) {

                if (i >= formsShown.size())
                    break;

                final FormsObject form = formsShown.get(i);

                Button b = new Button(commonForms.getContext());
                int color = App.getColor(commonForms.getContext(), R.attr.colorBackground);
                b.setBackgroundColor(color);
                b.setText(form.getName());
                b.setTextColor(App.getColor(commonForms.getContext(), form.getColor()));
                b.setTypeface(null, Typeface.NORMAL);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                param.setMargins(10, 10, 10, 10);
                b.setLayoutParams(param);
                Drawable icon = this.getResources().getDrawable(form.getIcon());
                b.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                DrawableCompat.setTint(b.getCompoundDrawables()[1], App.getColor(commonForms.getContext(), form.getColor()));

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (App.getLocation() == null || App.getLocation().equals("")) {
                            Toast toast = Toast.makeText(commonForms.getContext(), getResources().getString(R.string.location_not_select), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (!(form.getName().equals(Forms.FAST_SCREENING_FORM) || form.getName().equals(Forms.PMDT_BASIC_MANAGEMENT_UNIT_VISIT)) && App.getPatient() == null) {
                            Toast toast = Toast.makeText(commonForms.getContext(), getResources().getString(R.string.patient_not_select), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {

                            if(form.getName().equals(Forms.FAST_SCREENING_FORM) || form.getName().equals(Forms.PMDT_BASIC_MANAGEMENT_UNIT_VISIT))
                                MainActivity.headerLayout.setVisibility(View.GONE);

                            showMainContent(false);

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            try {
                                FragmentTransaction replace = fragmentTransaction.replace(R.id.fragment_form, (Fragment) form.getClassName().newInstance());
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            fragmentTransaction.commit();
                        }

                    }
                });

                layout.addView(b);

                i++;
            }

            i--;
            commonForms.addView(layout);

        }

        if(commonForms.getChildCount() == 0){
            common.setVisibility(View.GONE);
            commonForms.setVisibility(View.GONE);
        }

    }*/


    public void fillProgramFormContent() {

        if(App.getProgram().equals("")){

            if(!App.getLocation().equals("")) {
                //program.setText(App.getProgram());
                prgramForms.removeAllViews();

                String[] locatinPrograms = serverService.getLocationsProgamByName(App.getLocation());

                final TitledRadioGroup programSelection = new TitledRadioGroup(mainContent.getContext(), null, getResources().getString(R.string.select_program_continue), getResources().getStringArray(R.array.programs), "", App.VERTICAL, App.VERTICAL);

                programSelection.getRadioGroup().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        App.setProgram(App.get(programSelection));

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mainContent.getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Preferences.PROGRAM, App.getProgram());
                        editor.apply();

                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(App.getProgram() + "  |  " + App.getLocation());
                        fillProgramFormContent();
                        MainActivity.fragmentReport.fillReportFragment();
                        MainActivity.fragmentSummary.updateSummaryFragment();

                    }
                });

                final ArrayList<RadioButton> buttons = programSelection.getRadioGroup().getButtons();
                for (int i = 0; i < buttons.size(); i++) {

                    if (!locatinPrograms[0].equals("Y") && buttons.get(i).getText().equals(getResources().getString(R.string.fast)))
                        buttons.get(i).setVisibility(View.GONE);
                    if (!locatinPrograms[1].equals("Y") && buttons.get(i).getText().equals(getResources().getString(R.string.pet)))
                        buttons.get(i).setVisibility(View.GONE);
                    if (!locatinPrograms[2].equals("Y") && buttons.get(i).getText().equals(getResources().getString(R.string.childhood_tb)))
                        buttons.get(i).setVisibility(View.GONE);
                    if (!locatinPrograms[3].equals("Y") && buttons.get(i).getText().equals(getResources().getString(R.string.comorbidities)))
                        buttons.get(i).setVisibility(View.GONE);
                }


                prgramForms.addView(programSelection);
            }
        }
        else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_form, new BlankFragment());
            fragmentTransaction.commit();
            prgramForms.setVisibility(View.VISIBLE);
            prgramForms.removeAllViews();
            //program.setText(App.getProgram());

            ArrayList<FormsObject> forms = new ArrayList<FormsObject>();

            if (App.getProgram().equalsIgnoreCase("PET"))
                forms = Forms.getPETFormList();
            else if (App.getProgram().equalsIgnoreCase("PMDT"))
                forms = Forms.getPMDTFormList();
            else if (App.getProgram().equalsIgnoreCase("FAST"))
                forms = Forms.getFASTFormList();
            else if (App.getProgram().equalsIgnoreCase("COMORBIDITIES"))
                forms = Forms.getCommorbiditiesFormList();
            else if (App.getProgram().equalsIgnoreCase("CHILDHOOD TB"))
                forms = Forms.getChildhoodTBFormList();

            ArrayList<FormsObject> formsShown = new ArrayList<FormsObject>();
            for (int i = 0; i < forms.size(); i++) {
                final FormsObject form = forms.get(i);

                Boolean add = false;

                if (!App.getRoles().contains(Roles.DEVELOPER)) {
                    for (int k = 0; k < form.getRoles().length; k++) {
                        String role = form.getRoles()[k];
                        if (App.getRoles().contains(role)) {
                            add = true;
                            break;
                        }
                    }
                } else
                    add = true;

                if (add)
                    formsShown.add(form);
            }

            int formsInRow = 3;
            if (formsShown.size() < 12)
                formsInRow = 2;

            for (int i = 0; i < formsShown.size(); i++) {

                LinearLayout layout = new LinearLayout(prgramForms.getContext());
                layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                layout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                for (int j = 0; j < formsInRow; j++) {

                    if (i >= formsShown.size())
                        break;

                    final FormsObject form = formsShown.get(i);

                    Button b = new Button(prgramForms.getContext());
                    int color = App.getColor(prgramForms.getContext(), R.attr.colorBackground);
                    b.setBackgroundColor(color);
                    b.setText(form.getName());
                    b.setTextColor(App.getColor(prgramForms.getContext(), form.getColor()));
                    b.setTypeface(null, Typeface.NORMAL);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    param.setMargins(10, 10, 10, 10);
                    b.setLayoutParams(param);
                    Drawable icon = this.getResources().getDrawable(form.getIcon());
                    b.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                    DrawableCompat.setTint(b.getCompoundDrawables()[1], App.getColor(prgramForms.getContext(), form.getColor()));

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (App.getLocation() == null || App.getLocation().equals("")) {
                                Toast toast = Toast.makeText(prgramForms.getContext(), getResources().getString(R.string.location_not_select), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else if (!(form.getName().equals(Forms.FAST_SCREENING_FORM) || form.getName().equals(Forms.PMDT_BASIC_MANAGEMENT_UNIT_VISIT)) && App.getPatient() == null) {
                                Toast toast = Toast.makeText(prgramForms.getContext(), getResources().getString(R.string.patient_not_select), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {

                                if (form.getName().equals(Forms.FAST_SCREENING_FORM) || form.getName().equals(Forms.PMDT_BASIC_MANAGEMENT_UNIT_VISIT))
                                    MainActivity.headerLayout.setVisibility(View.GONE);

                                showMainContent(false);

                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                try {
                                    FragmentTransaction replace = fragmentTransaction.replace(R.id.fragment_form, (Fragment) form.getClassName().newInstance());
                                } catch (java.lang.InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                fragmentTransaction.commit();
                            }

                        }
                    });

                    layout.addView(b);

                    i++;
                }

                i--;
                prgramForms.addView(layout);

            }
        }

    }

    public boolean isFormVisible() {

        if (prgramForms.getVisibility() == View.GONE)
            return true;

        else
            return false;

    }

    public void setMainContentVisible(Boolean visible) {
        if (visible) {
            prgramForms.setVisibility(View.VISIBLE);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_form, new BlankFragment());
            fragmentTransaction.commit();

            showMainContent(true);

        } else
            prgramForms.setVisibility(View.GONE);

    }

    public void openForm(FormsObject form, String formId, Boolean openFlag) {
        prgramForms.setVisibility(View.GONE);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        try {
            Fragment newFragment = (Fragment) form.getClassName().newInstance();
            Bundle args = new Bundle();
            args.putString("formId", formId);
            args.putBoolean("open", openFlag);
            newFragment.setArguments(args);
            FragmentTransaction replace = fragmentTransaction.replace(R.id.fragment_form, newFragment);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fragmentTransaction.commit();
    }

}