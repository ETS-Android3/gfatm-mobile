package com.ihsinformatics.gfatmmobile;

/**
 * Created by Rabbia on 11/10/2016.
 */

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.shared.FormsObject;
import com.ihsinformatics.gfatmmobile.shared.Roles;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.ArrayList;
import java.util.Arrays;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FormFragment extends Fragment implements View.OnClickListener {

    /*private LinearLayout programForms;
    private LinearLayout mainContent;*/
    private View mainview;
    /*private TextView program;*/
    private LinearLayout commonForms;
    private TextView screening;
    private LinearLayout screeningForms;
    private TextView test;
    private LinearLayout testForms;
    private TextView treatment;
    private LinearLayout treatmentForms;

    Context context;

    public static ServerService serverService;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mainview = inflater.inflate(R.layout.form_fragment, container, false);

       /* mainContent = (LinearLayout) mainview.findViewById(R.id.mainContent);
        programForms = (LinearLayout) mainview.findViewById(R.id.content_main);*/
        commonForms = (LinearLayout) mainview.findViewById(R.id.content_common);
        screeningForms = (LinearLayout) mainview.findViewById(R.id.content_screening);
        screening = (TextView) mainview.findViewById(R.id.screening);
        screening.setOnClickListener(this);

        treatmentForms = (LinearLayout) mainview.findViewById(R.id.content_treatment);
        treatment = (TextView) mainview.findViewById(R.id.treatment);
        treatment.setOnClickListener(this);

        testForms = (LinearLayout) mainview.findViewById(R.id.content_test);
        test = (TextView) mainview.findViewById(R.id.test);
        test.setOnClickListener(this);

        /*program = (TextView) mainview.findViewById(R.id.program);*/

        serverService = new ServerService(screeningForms.getContext());
        context = screeningForms.getContext();

        fillTestFormContent();
        fillScreeningFormContent();
        fillTreatmentFormContent();
        fillCommonFormContent();

        return mainview;
    }

    public void showMainContent(Boolean flag){

        if(flag){

            Drawable.ConstantState stateB = getResources().getDrawable(R.drawable.ic_less).getConstantState();

            screening.setVisibility(View.VISIBLE);
            Drawable.ConstantState stateA = screening.getCompoundDrawables()[2].getConstantState();
            if ((stateA != null && stateB != null && stateA.equals(stateB)) || serverService.getBitmap(screening.getCompoundDrawables()[2]).sameAs(serverService.getBitmap(getResources().getDrawable(R.drawable.ic_less))))
                screeningForms.setVisibility(View.VISIBLE);
            else
                screening.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);

            test.setVisibility(View.VISIBLE);

            stateA = test.getCompoundDrawables()[2].getConstantState();
            if ((stateA != null && stateB != null && stateA.equals(stateB)) || serverService.getBitmap(test.getCompoundDrawables()[2]).sameAs(serverService.getBitmap(getResources().getDrawable(R.drawable.ic_less))))
                testForms.setVisibility(View.VISIBLE);
            else
                test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);

            treatment.setVisibility(View.VISIBLE);

            stateA = treatment.getCompoundDrawables()[2].getConstantState();
            if ((stateA != null && stateB != null && stateA.equals(stateB)) || serverService.getBitmap(treatment.getCompoundDrawables()[2]).sameAs(serverService.getBitmap(getResources().getDrawable(R.drawable.ic_less))))
                treatmentForms.setVisibility(View.VISIBLE);
            else
                treatment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);

            commonForms.setVisibility(View.VISIBLE);

        }else {
            screeningForms.setVisibility(View.GONE);
            screening.setVisibility(View.GONE);
            treatment.setVisibility(View.GONE);
            treatmentForms.setVisibility(View.GONE);
            testForms.setVisibility(View.GONE);
            test.setVisibility(View.GONE);
            commonForms.setVisibility(View.GONE);
        }

    }

    public void fillScreeningFormContent() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_form, new BlankFragment());
        fragmentTransaction.commit();
        //screeningForms.setVisibility(View.VISIBLE);
        screeningForms.removeAllViews();

        ArrayList<FormsObject> forms = Forms.getScreeningFormList();

        ArrayList<FormsObject> formsShown = new ArrayList<FormsObject>();
        for (int i = 0; i < forms.size(); i++) {
            final FormsObject form = forms.get(i);

            Boolean add = false;

            if (!(App.getRoles().contains(Roles.DEVELOPER))) {

                String pr = App.getPrivileges();
                if(pr.contains("Add "+form.getName()))
                    add = true;

            } else
                add = true;

            if(add){

                if(App.getPatient() != null){

                    int lowerLimit = form.getAgeLowerLimit();
                    int upperLimit = form.getAgeUpperLimit();
                    int age = App.getPatient().getPerson().getAge();

                    if(lowerLimit != -1 ) {
                        if (age >= lowerLimit)
                            add = true;
                        else
                            add = false;
                    }

                    if(upperLimit != -1) {
                        if (age <= upperLimit)
                            add = true;
                        else
                            add = false;
                    }

                    if(add)
                        formsShown.add(form);
                }
                else
                    formsShown.add(form);

            }

            /*if(add)
                formsShown.add(form);*/

        }

        int formsInRow = 3;
        if (formsShown.size() < 12)
            formsInRow = 2;

        for (int i = 0; i < formsShown.size(); i++) {

            LinearLayout layout = new LinearLayout(screeningForms.getContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            layout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

            for (int j = 0; j < formsInRow; j++) {

                if (i >= formsShown.size())
                    break;

                final FormsObject form = formsShown.get(i);

                Button b = new Button(screeningForms.getContext());
                int color = App.getColor(screeningForms.getContext(), R.attr.colorBackground);
                b.setBackgroundColor(color);
                b.setText(form.getName());
                b.setTextColor(App.getColor(screeningForms.getContext(), form.getColor()));
                b.setTypeface(null, Typeface.NORMAL);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                param.setMargins(10, 10, 10, 10);
                b.setLayoutParams(param);
                Drawable icon = this.getResources().getDrawable(form.getIcon());
                b.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                DrawableCompat.setTint(b.getCompoundDrawables()[1], App.getColor(screeningForms.getContext(), form.getColor()));

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (App.getLocation() == null || App.getLocation().equals("")) {
                            Toast toast = Toast.makeText(screeningForms.getContext(), getResources().getString(R.string.location_not_select), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (!form.getName().equals(Forms.FAST_SCREENING_FORM) && App.getPatient() == null) {
                            Toast toast = Toast.makeText(screeningForms.getContext(), getResources().getString(R.string.patient_not_select), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {

                            if(form.getName().equals(Forms.FAST_SCREENING_FORM))
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
            screeningForms.addView(layout);

        }

        if(screeningForms.getChildCount() == 0) {
            screeningForms.setVisibility(View.GONE);
            screening.setVisibility(View.GONE);
        }
        else {
            //screeningForms.setVisibility(View.VISIBLE);
            screening.setVisibility(View.VISIBLE);
        }

    }

    public void fillTestFormContent() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_form, new BlankFragment());
        fragmentTransaction.commit();
        //testForms.setVisibility(View.VISIBLE);
        testForms.removeAllViews();

        ArrayList<FormsObject> forms = Forms.getTestFormList();

        ArrayList<FormsObject> formsShown = new ArrayList<FormsObject>();
        for (int i = 0; i < forms.size(); i++) {
            final FormsObject form = forms.get(i);

            Boolean add = false;

            if (!(App.getRoles().contains(Roles.DEVELOPER))) {

                if(App.getPrivileges().contains("Add "+form.getName()))
                    add = true;

            } else
                add = true;

            if(add){

                if(App.getPatient() != null){

                    int lowerLimit = form.getAgeLowerLimit();
                    int upperLimit = form.getAgeUpperLimit();
                    int age = App.getPatient().getPerson().getAge();

                    if(lowerLimit != -1 ) {
                        if (age >= lowerLimit)
                            add = true;
                        else
                            add = false;
                    }

                    if(upperLimit != -1) {
                        if (age <= upperLimit)
                            add = true;
                        else
                            add = false;
                    }

                    if(add)
                        formsShown.add(form);
                }
                else
                    formsShown.add(form);

            }

        }

        int formsInRow = 3;
        if (formsShown.size() < 12)
            formsInRow = 2;

        for (int i = 0; i < formsShown.size(); i++) {

            LinearLayout layout = new LinearLayout(testForms.getContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            layout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

            for (int j = 0; j < formsInRow; j++) {

                if (i >= formsShown.size())
                    break;

                final FormsObject form = formsShown.get(i);

                Button b = new Button(testForms.getContext());
                int color = App.getColor(testForms.getContext(), R.attr.colorBackground);
                b.setBackgroundColor(color);
                b.setText(form.getName());
                b.setTextColor(App.getColor(testForms.getContext(), form.getColor()));
                b.setTypeface(null, Typeface.NORMAL);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                param.setMargins(10, 10, 10, 10);
                b.setLayoutParams(param);
                Drawable icon = this.getResources().getDrawable(form.getIcon());
                b.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                DrawableCompat.setTint(b.getCompoundDrawables()[1], App.getColor(testForms.getContext(), form.getColor()));

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (App.getLocation() == null || App.getLocation().equals("")) {
                            Toast toast = Toast.makeText(testForms.getContext(), getResources().getString(R.string.location_not_select), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (!form.getName().equals(Forms.FAST_SCREENING_FORM) && App.getPatient() == null) {
                            Toast toast = Toast.makeText(testForms.getContext(), getResources().getString(R.string.patient_not_select), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {

                            if(form.getName().equals(Forms.FAST_SCREENING_FORM))
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
            testForms.addView(layout);

        }

        if(testForms.getChildCount() == 0) {
            testForms.setVisibility(View.GONE);
            test.setVisibility(View.GONE);
        }
        else {
            //testForms.setVisibility(View.VISIBLE);
            test.setVisibility(View.VISIBLE);
        }


    }

    public void fillTreatmentFormContent() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_form, new BlankFragment());
        fragmentTransaction.commit();
        //treatmentForms.setVisibility(View.VISIBLE);
        treatmentForms.removeAllViews();

        ArrayList<FormsObject> forms = Forms.getTreatmentFormList();

        ArrayList<FormsObject> formsShown = new ArrayList<FormsObject>();
        for (int i = 0; i < forms.size(); i++) {
            final FormsObject form = forms.get(i);

            Boolean add = false;

            if (!(App.getRoles().contains(Roles.DEVELOPER) || Arrays.asList(form.getRoles()).contains(Roles.ALL))) {

                if(App.getPrivileges().contains("Add "+form.getName()))
                    add = true;

            } else
                add = true;

            if(add){

                if(App.getPatient() != null){

                    int lowerLimit = form.getAgeLowerLimit();
                    int upperLimit = form.getAgeUpperLimit();
                    int age = App.getPatient().getPerson().getAge();

                    if(lowerLimit != -1 ) {
                        if (age >= lowerLimit)
                            add = true;
                        else
                            add = false;
                    }

                    if(upperLimit != -1) {
                        if (age <= upperLimit)
                            add = true;
                        else
                            add = false;
                    }

                    if(add)
                        formsShown.add(form);
                }
                else
                    formsShown.add(form);

            }

        }

        int formsInRow = 3;
        if (formsShown.size() < 12)
            formsInRow = 2;

        for (int i = 0; i < formsShown.size(); i++) {

            LinearLayout layout = new LinearLayout(treatmentForms.getContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            layout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

            for (int j = 0; j < formsInRow; j++) {

                if (i >= formsShown.size())
                    break;

                final FormsObject form = formsShown.get(i);

                Button b = new Button(treatmentForms.getContext());
                int color = App.getColor(treatmentForms.getContext(), R.attr.colorBackground);
                b.setBackgroundColor(color);
                b.setText(form.getName());
                b.setTextColor(App.getColor(treatmentForms.getContext(), form.getColor()));
                b.setTypeface(null, Typeface.NORMAL);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                param.setMargins(10, 10, 10, 10);
                b.setLayoutParams(param);
                Drawable icon = this.getResources().getDrawable(form.getIcon());
                b.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                DrawableCompat.setTint(b.getCompoundDrawables()[1], App.getColor(treatmentForms.getContext(), form.getColor()));

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (App.getLocation() == null || App.getLocation().equals("")) {
                            Toast toast = Toast.makeText(treatmentForms.getContext(), getResources().getString(R.string.location_not_select), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (!form.getName().equals(Forms.FAST_SCREENING_FORM) && App.getPatient() == null) {
                            Toast toast = Toast.makeText(treatmentForms.getContext(), getResources().getString(R.string.patient_not_select), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {

                            if(form.getName().equals(Forms.FAST_SCREENING_FORM) )
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
            treatmentForms.addView(layout);

        }

        if(treatmentForms.getChildCount() == 0) {
            treatmentForms.setVisibility(View.GONE);
            treatment.setVisibility(View.GONE);
        }
        else {
            // treatmentForms.setVisibility(View.VISIBLE);
            treatment.setVisibility(View.VISIBLE);
        }

    }


    public boolean isFormVisible() {

        if (screening.getVisibility() == View.VISIBLE || treatment.getVisibility() == View.VISIBLE || test.getVisibility() == View.VISIBLE || commonForms.getVisibility() == View.VISIBLE)
            return false;
        else
            return true;

    }

    public void setMainContentVisible(Boolean visible) {
        if (visible) {
            //screeningForms.setVisibility(View.VISIBLE);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_form, new BlankFragment());
            fragmentTransaction.commit();
            showMainContent(true);
        }

    }

    public void openForm(FormsObject form, String formId, Boolean openFlag) {
        screening.setVisibility(View.GONE);
        screeningForms.setVisibility(View.GONE);
        test.setVisibility(View.GONE);
        testForms.setVisibility(View.GONE);
        treatment.setVisibility(View.GONE);
        treatmentForms.setVisibility(View.GONE);
        commonForms.setVisibility(View.GONE);

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

    @Override
    public void onClick(View v) {

        int color = App.getColor(context, R.attr.colorPrimaryDark);
        int color1 = App.getColor(context, R.attr.colorAccent);

        if(v == screening){
            if (screeningForms.getVisibility() == View.VISIBLE) {
                screeningForms.setVisibility(View.GONE);
                screening.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(screening.getCompoundDrawables()[2], color);
            }
            else {
                screeningForms.setVisibility(View.VISIBLE);
                screening.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(screening.getCompoundDrawables()[2], color1);

                testForms.setVisibility(View.GONE);
                test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(screening.getCompoundDrawables()[2], color);

                treatmentForms.setVisibility(View.GONE);
                treatment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(treatment.getCompoundDrawables()[2], color);
            }
        }  else if(v == test){
            if (testForms.getVisibility() == View.VISIBLE) {
                testForms.setVisibility(View.GONE);
                test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(test.getCompoundDrawables()[2], color);
            }
            else {
                testForms.setVisibility(View.VISIBLE);
                test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(test.getCompoundDrawables()[2], color1);

                screeningForms.setVisibility(View.GONE);
                screening.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(screening.getCompoundDrawables()[2], color);

                treatmentForms.setVisibility(View.GONE);
                treatment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(treatment.getCompoundDrawables()[2], color);
            }
        }   else if(v == treatment){
            if (treatmentForms.getVisibility() == View.VISIBLE) {
                treatmentForms.setVisibility(View.GONE);
                treatment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(treatment.getCompoundDrawables()[2], color);
            }
            else {
                treatmentForms.setVisibility(View.VISIBLE);
                treatment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(treatment.getCompoundDrawables()[2], color1);

                screeningForms.setVisibility(View.GONE);
                screening.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(screening.getCompoundDrawables()[2], color);

                testForms.setVisibility(View.GONE);
                test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(test.getCompoundDrawables()[2], color);
            }
        }
    }

    public void fillCommonFormContent() {

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

            if (!(App.getRoles().contains(Roles.DEVELOPER))) {

                String pr = App.getPrivileges();
                if(pr.contains("Add "+form.getName()))
                    add = true;

            } else
                add = true;

            if(add){

                if(App.getPatient() != null){

                    int lowerLimit = form.getAgeLowerLimit();
                    int upperLimit = form.getAgeUpperLimit();
                    int age = App.getPatient().getPerson().getAge();

                    if(lowerLimit != -1 ) {
                        if (age >= lowerLimit)
                            add = true;
                        else
                            add = false;
                    }

                    if(upperLimit != -1) {
                        if (age <= upperLimit)
                            add = true;
                        else
                            add = false;
                    }

                    if(add)
                        formsShown.add(form);
                }
                else
                    formsShown.add(form);

            }

            /*if(add)
                formsShown.add(form);*/

        }

        int formsInRow = 3;
        if (formsShown.size() < 12)
            formsInRow = 2;

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
                        } else if (!form.getName().equals(Forms.FAST_SCREENING_FORM) && App.getPatient() == null) {
                            Toast toast = Toast.makeText(commonForms.getContext(), getResources().getString(R.string.patient_not_select), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {

                            if(form.getName().equals(Forms.FAST_SCREENING_FORM))
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

        if(commonForms.getChildCount() == 0)
            commonForms.setVisibility(View.GONE);
        else
            commonForms.setVisibility(View.VISIBLE);

    }
}