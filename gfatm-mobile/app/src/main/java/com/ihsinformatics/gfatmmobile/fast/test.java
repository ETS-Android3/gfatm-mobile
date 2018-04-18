package com.ihsinformatics.gfatmmobile.fast;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class test extends AbstractFormActivity {

    TitledEditText text;
    TitledSpinner spinner;
    TitledRadioGroup radioGroup;
    TitledCheckBoxes checkBoxes;
    MyCheckBox checkBox;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 5;
        formName = Forms.PET_INDEX_PATIENT_REGISTRATION;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        pager =  (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener (this);
        navigationSeekbar.setMax (pageCount - 1);
        formNameView.setText(formName);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if(App.isLanguageRTL()){
            for (int i = pageCount -1; i >= 0; i--) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }
        else {
            for (int i = 0; i < pageCount; i++) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        gotoFirstPage();

        return mainContent;
    }


    @Override
    public void initViews() {

        text = new TitledEditText(mainContent.getContext(), null, "Question", "", "Hint", 10, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        spinner = new TitledSpinner(mainContent.getContext(), "title", "Question", getResources().getStringArray(R.array.programs), "PET", App.HORIZONTAL );
        radioGroup = new TitledRadioGroup(mainContent.getContext(), null, "Question", getResources().getStringArray(R.array.programs), "PMDT", App.VERTICAL, App.HORIZONTAL);
        checkBoxes = new TitledCheckBoxes(mainContent.getContext(), "title", "Question", getResources().getStringArray(R.array.programs), new Boolean[]{true,false,true,false,false} , App.VERTICAL, App.HORIZONTAL);
        checkBox = new MyCheckBox(mainContent.getContext(), "Hello", false);

        views = new View[]{text.getEditText(), spinner.getSpinner(), radioGroup.getRadioGroup(), checkBoxes, checkBox};

        viewGroups = new View[][]
                { {text, spinner},
                        {radioGroup},
                        {checkBoxes, checkBox},
                        {},
                        {}};

    }

    @Override
    public void updateDisplay() {

    }


    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public boolean submit() {
        resetViews();
        return false;
    }

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public void refill(int encounterId) {

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageCount;
        }

        @Override
        public Object instantiateItem(View container, int position) {

            ViewGroup viewGroup = groups.get(position);
            ((ViewPager) container).addView(viewGroup, 0);

            return viewGroup;
        }

        @Override
        public void destroyItem(View container, int position, Object obj) {
            ((ViewPager) container).removeView((View) obj);
        }

        @Override
        public boolean isViewFromObject(View container, Object obj) {
            return container == obj;
        }

    }

}
