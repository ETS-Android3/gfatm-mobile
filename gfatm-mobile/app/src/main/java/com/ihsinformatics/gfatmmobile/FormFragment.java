package com.ihsinformatics.gfatmmobile;

/**
 * Created by Rabbia on 11/10/2016.
 */

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.shared.FormsObject;

import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FormFragment extends Fragment {

    private LinearLayout mainContent;
    private View mainview;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mainview = inflater.inflate(R.layout.form_fragment, container, false);

        mainContent = (LinearLayout) mainview.findViewById(R.id.content_main);

        fillMainContent();

        return mainview;
    }

    public void fillMainContent() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_form, new BlankFragment());
        fragmentTransaction.commit();
        mainContent.setVisibility(View.VISIBLE);
        mainContent.removeAllViews();

        ArrayList<FormsObject> forms = new ArrayList<FormsObject>();

        if (App.getProgram().equals("PET"))
            forms = Forms.getPETFormList();
        else if (App.getProgram().equals("PMDT"))
            forms = Forms.getPMDTFormList();
        else if (App.getProgram().equals("FAST"))
            forms = Forms.getFASTFormList();
        else if (App.getProgram().equals("COMORBIDITIES"))
            forms = Forms.getCommorbiditiesFormList();
        else if (App.getProgram().equals("CHILDHOOD TB"))
            forms = Forms.getChildhoodTBFormList();

        for (int i = 0; i < forms.size(); i++) {

            LinearLayout layout = new LinearLayout(mainContent.getContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            layout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

            for (int j = 0; j < 3; j++) {

                if (i >= forms.size())
                    break;

                final FormsObject form = forms.get(i);

                Button b = new Button(mainContent.getContext());
                int color = App.getColor(mainContent.getContext(), R.attr.colorBackground);
                b.setBackgroundColor(color);
                b.setText(form.getName());
                b.setTextColor(App.getColor(mainContent.getContext(), form.getColor()));
                b.setTypeface(null, Typeface.NORMAL);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                param.setMargins(10, 10, 10, 10);
                b.setLayoutParams(param);
                Drawable icon = this.getResources().getDrawable(form.getIcon());
                b.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                DrawableCompat.setTint(b.getCompoundDrawables()[1], App.getColor(mainContent.getContext(), form.getColor()));

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*if (App.getPatient() == null) {
                            Toast toast = Toast.makeText(mainContent.getContext(), getResources().getString(R.string.patient_not_select), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {*/
                            mainContent.setVisibility(View.GONE);

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
                        /*}*/

                    }
                });

                layout.addView(b);

                i++;
            }

            i--;
            mainContent.addView(layout);
        }

    }

    public boolean isFormVisible() {

        if (mainContent.getVisibility() == View.GONE)
            return true;

        else
            return false;

    }

    public void setMainContentVisible(Boolean visible) {
        if (visible) {
            mainContent.setVisibility(View.VISIBLE);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_form, new BlankFragment());
            fragmentTransaction.commit();
        } else
            mainContent.setVisibility(View.GONE);

    }

    public void openForm(FormsObject form, String formId, Boolean openFlag) {
        mainContent.setVisibility(View.GONE);

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