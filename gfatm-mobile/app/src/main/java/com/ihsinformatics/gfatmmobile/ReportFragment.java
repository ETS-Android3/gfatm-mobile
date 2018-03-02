package com.ihsinformatics.gfatmmobile;

/**
 * Created by Rabbia on 11/10/2016.
 */

import android.app.Fragment;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.util.ServerService;


public class ReportFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

    Context context;

    ServerService serverService;

    ImageView refersh;

    TextView common;
    LinearLayout commonReportLayout;

    TextView fast;
    LinearLayout fastReportLayout;

    TextView childhoodtb;
    LinearLayout childhoodtbReportLayout;

    TextView commorbidities;
    LinearLayout commorbiditiesReportLayout;

    TextView pet;
    LinearLayout petReportLayout;

    TextView pmdt;
    LinearLayout pmdtReportLayout;

    TextView cc;
    LinearLayout ccReportLayout;

    TextView ztts;
    LinearLayout zttsReportLayout;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View mainContent = inflater.inflate(
                R.layout.report_fragment, container, false);

        context = mainContent.getContext();

        serverService = new ServerService(context.getApplicationContext());

        int color = App.getColor(context, R.attr.colorPrimaryDark);
        int color1 = App.getColor(context, R.attr.colorAccent);

        refersh = (ImageView) mainContent.findViewById(R.id.refresh);
        refersh.setOnTouchListener(this);

        common = (TextView) mainContent.findViewById(R.id.common);
        common.setOnClickListener(this);
        commonReportLayout = (LinearLayout) mainContent.findViewById(R.id.commonReportFragment);

        fast = (TextView) mainContent.findViewById(R.id.fast);
        fast.setOnClickListener(this);
        fastReportLayout = (LinearLayout) mainContent.findViewById(R.id.fastReportFragment);

        childhoodtb = (TextView) mainContent.findViewById(R.id.childhoodtb);
        childhoodtb.setOnClickListener(this);
        childhoodtbReportLayout = (LinearLayout) mainContent.findViewById(R.id.childhoodtbReportFragment);

        commorbidities = (TextView) mainContent.findViewById(R.id.commorbidties);
        commorbidities.setOnClickListener(this);
        commorbiditiesReportLayout = (LinearLayout) mainContent.findViewById(R.id.commorbidtiesReportFragment);

        pet = (TextView) mainContent.findViewById(R.id.pet);
        pet.setOnClickListener(this);
        petReportLayout = (LinearLayout) mainContent.findViewById(R.id.petReportFragment);

        pmdt = (TextView) mainContent.findViewById(R.id.pmdt);
        pmdt.setOnClickListener(this);
        pmdtReportLayout = (LinearLayout) mainContent.findViewById(R.id.pmdtReportFragment);

        cc = (TextView) mainContent.findViewById(R.id.cc);
        cc.setOnClickListener(this);
        ccReportLayout = (LinearLayout) mainContent.findViewById(R.id.ccReportFragment);

        ztts = (TextView) mainContent.findViewById(R.id.ztts);
        ztts.setOnClickListener(this);
        zttsReportLayout = (LinearLayout) mainContent.findViewById(R.id.zttsReportFragment);

        fillReportFragment();

        return mainContent;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(getResources().getColor(R.color.dark_grey), PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                fillReportFragment();

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return true;
    }

    public void fillReportFragment() {

        String program = App.getProgram();
        final int color = App.getColor(context, R.attr.colorPrimaryDark);
        final int color1 = App.getColor(context, R.attr.colorAccent);
        final int color2 = App.getColor(context, R.attr.colorPrimary);

        commonReportLayout.setVisibility(View.GONE);
        common.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
        DrawableCompat.setTint(common.getCompoundDrawables()[2], color);

        fastReportLayout.setVisibility(View.GONE);
        fast.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
        DrawableCompat.setTint(fast.getCompoundDrawables()[2], color);

        childhoodtbReportLayout.setVisibility(View.GONE);
        childhoodtb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
        DrawableCompat.setTint(childhoodtb.getCompoundDrawables()[2], color);

        commorbiditiesReportLayout.setVisibility(View.GONE);
        commorbidities.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
        DrawableCompat.setTint(commorbidities.getCompoundDrawables()[2], color);

        petReportLayout.setVisibility(View.GONE);
        pet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
        DrawableCompat.setTint(pet.getCompoundDrawables()[2], color);

        pmdtReportLayout.setVisibility(View.GONE);
        pmdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
        DrawableCompat.setTint(pmdt.getCompoundDrawables()[2], color);

        zttsReportLayout.setVisibility(View.GONE);
        ztts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
        DrawableCompat.setTint(ztts.getCompoundDrawables()[2], color);

        commonReportLayout.removeAllViews();
        fastReportLayout.removeAllViews();
        childhoodtbReportLayout.removeAllViews();
        commorbiditiesReportLayout.removeAllViews();
        petReportLayout.removeAllViews();
        pmdtReportLayout.removeAllViews();
        ccReportLayout.removeAllViews();
        zttsReportLayout.removeAllViews();

        final Object[][] zttsEncounters = serverService.getAllEncounterFromLocalDB(getResources().getString(R.string.ztts));
        if (zttsEncounters == null || zttsEncounters.length == 0) {
            ztts.setVisibility(View.GONE);
            zttsReportLayout.setVisibility(View.GONE);
        } else {

            ztts.setVisibility(View.VISIBLE);
            for (int i = 0; i < zttsEncounters.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(context);
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                final LinearLayout encounterDetailsLayout = new LinearLayout(context);
                encounterDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                encounterDetailsLayout.setPadding(0, 10, 0, 0);

                final LinearLayout moreLayout = new LinearLayout(context);
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final TextView text = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(zttsEncounters[i][0]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                text.setPadding(10, 0, 0, 0);
                text.setTag(String.valueOf(zttsEncounters[i][1]));
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                verticalLayout.addView(encounterDetailsLayout);
                verticalLayout.addView(moreLayout);

                LinearLayout ll1 = new LinearLayout(context);
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv1 = new TextView(context);
                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                tv1.setTextColor(color);
                //tv1.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                tv1.setLayoutParams(p);
                ll1.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(App.convertToTitleCase(String.valueOf(zttsEncounters[i][3])));
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                //tv2.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p1.weight = 1;
                tv2.setLayoutParams(p1);
                ll1.addView(tv2);

                encounterDetailsLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(context);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setPadding(0, 0, 0, 10);

                TextView tv3 = new TextView(context);
                tv3.setText(getResources().getString(R.string.location) + "  ");
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                tv3.setTextColor(color);
                //tv3.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p2.weight = 1;
                tv3.setLayoutParams(p2);
                ll2.addView(tv3);

                TextView tv4 = new TextView(context);
                tv4.setText(String.valueOf(zttsEncounters[i][4]));
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                //tv4.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p3.weight = 1;
                tv4.setLayoutParams(p3);
                ll2.addView(tv4);

                encounterDetailsLayout.addView(ll2);

                encounterDetailsLayout.setVisibility(View.GONE);
                moreLayout.setVisibility(View.GONE);

                zttsReportLayout.addView(verticalLayout);

                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            encounterDetailsLayout.setVisibility(View.GONE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                            text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                            text.setTextSize(getResources().getDimension(R.dimen.small));
                        } else {

                            moreLayout.removeAllViews();
                            Object[][] obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            if (obs.length == 0) {
                                encounterDetailsLayout.removeAllViews();

                                Object[][] encounterObject = serverService.getEncounterIdByEncounterType(text.getText().toString());
                                text.setTag(String.valueOf(encounterObject[0][1]));

                                LinearLayout ll1 = new LinearLayout(context);
                                ll1.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv1 = new TextView(context);
                                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                                tv1.setTextColor(color);
                                //tv1.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv1.setLayoutParams(p);
                                ll1.addView(tv1);

                                TextView tv2 = new TextView(context);
                                tv2.setText(App.convertToTitleCase(String.valueOf(encounterObject[0][3])));
                                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv2.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv2.setLayoutParams(p1);
                                ll1.addView(tv2);

                                encounterDetailsLayout.addView(ll1);

                                LinearLayout ll2 = new LinearLayout(context);
                                ll2.setOrientation(LinearLayout.HORIZONTAL);
                                ll2.setPadding(0, 0, 0, 10);

                                TextView tv3 = new TextView(context);
                                tv3.setText(getResources().getString(R.string.location) + "  ");
                                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                                tv3.setTextColor(color);
                                tv3.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p2.weight = 1;
                                tv3.setLayoutParams(p2);
                                ll2.addView(tv3);

                                TextView tv4 = new TextView(context);
                                tv4.setText(String.valueOf(encounterObject[0][4]));
                                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv4.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p3.weight = 1;
                                tv4.setLayoutParams(p3);
                                ll2.addView(tv4);

                                encounterDetailsLayout.addView(ll2);

                                obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            } else{

                            }

                            for (int j = 0; j < obs.length; j++) {
                                LinearLayout ll5 = new LinearLayout(context);
                                ll5.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv8 = new TextView(context);
                                tv8.setText(App.convertToTitleCase(String.valueOf(obs[j][1])) + ":  ");
                                tv8.setTextSize(getResources().getDimension(R.dimen.small));
                                tv8.setTextColor(color);
                                //tv8.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv8.setLayoutParams(p);
                                ll5.addView(tv8);

                                TextView tv9 = new TextView(context);
                                tv9.setText(String.valueOf(App.convertToTitleCase(String.valueOf(obs[j][0]))));
                                tv9.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv9.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv9.setLayoutParams(p1);
                                ll5.addView(tv9);

                                moreLayout.addView(ll5);
                            }


                            for (int k = 0; k < zttsReportLayout.getChildCount(); k++) {

                                View view = zttsReportLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);

                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);
                                LinearLayout mL2 = (LinearLayout) ((LinearLayout) view).getChildAt(2);

                                mL.setVisibility(View.GONE);
                                mL2.setVisibility(View.GONE);
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                                DrawableCompat.setTint(t.getCompoundDrawables()[2], color2);
                                t.setTypeface(t.getTypeface(), Typeface.NORMAL);
                                t.setTextSize(getResources().getDimension(R.dimen.small));

                            }

                            moreLayout.setVisibility(View.VISIBLE);
                            encounterDetailsLayout.setVisibility(View.VISIBLE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setTypeface(text.getTypeface(), Typeface.BOLD);
                            text.setTextSize(getResources().getDimension(R.dimen.medium));
                        }
                    }
                });

            }

        }

        Object[][] petEncounters = serverService.getAllEncounterFromLocalDB(getResources().getString(R.string.pet));
        if (petEncounters == null || petEncounters.length == 0) {
            pet.setVisibility(View.GONE);
            petReportLayout.setVisibility(View.GONE);
        } else {

            pet.setVisibility(View.VISIBLE);
            for (int i = 0; i < petEncounters.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(context);
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                final LinearLayout encounterDetailsLayout = new LinearLayout(context);
                encounterDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                encounterDetailsLayout.setPadding(0, 10, 0, 0);

                final LinearLayout moreLayout = new LinearLayout(context);
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final TextView text = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(petEncounters[i][0]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                text.setPadding(10, 0, 0, 0);
                text.setTag(String.valueOf(petEncounters[i][1]));
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                verticalLayout.addView(encounterDetailsLayout);
                verticalLayout.addView(moreLayout);

                LinearLayout ll1 = new LinearLayout(context);
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv1 = new TextView(context);
                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                tv1.setTextColor(color);
                //tv1.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                tv1.setLayoutParams(p);
                ll1.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(App.convertToTitleCase(String.valueOf(petEncounters[i][3])));
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                //tv2.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p1.weight = 1;
                tv2.setLayoutParams(p1);
                ll1.addView(tv2);

                encounterDetailsLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(context);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setPadding(0, 0, 0, 10);

                TextView tv3 = new TextView(context);
                tv3.setText(getResources().getString(R.string.location) + "  ");
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                tv3.setTextColor(color);
                //tv3.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p2.weight = 1;
                tv3.setLayoutParams(p2);
                ll2.addView(tv3);

                TextView tv4 = new TextView(context);
                tv4.setText(String.valueOf(petEncounters[i][4]));
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                //tv4.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p3.weight = 1;
                tv4.setLayoutParams(p3);
                ll2.addView(tv4);

                encounterDetailsLayout.addView(ll2);

                encounterDetailsLayout.setVisibility(View.GONE);
                moreLayout.setVisibility(View.GONE);

                petReportLayout.addView(verticalLayout);

                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            encounterDetailsLayout.setVisibility(View.GONE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                            text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                            text.setTextSize(getResources().getDimension(R.dimen.small));
                        } else {

                            moreLayout.removeAllViews();
                            Object[][] obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            if (obs.length == 0) {
                                encounterDetailsLayout.removeAllViews();

                                Object[][] encounterObject = serverService.getEncounterIdByEncounterType(text.getText().toString());
                                text.setTag(String.valueOf(encounterObject[0][1]));

                                LinearLayout ll1 = new LinearLayout(context);
                                ll1.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv1 = new TextView(context);
                                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                                tv1.setTextColor(color);
                                //tv1.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv1.setLayoutParams(p);
                                ll1.addView(tv1);

                                TextView tv2 = new TextView(context);
                                tv2.setText(App.convertToTitleCase(String.valueOf(encounterObject[0][3])));
                                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv2.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv2.setLayoutParams(p1);
                                ll1.addView(tv2);

                                encounterDetailsLayout.addView(ll1);

                                LinearLayout ll2 = new LinearLayout(context);
                                ll2.setOrientation(LinearLayout.HORIZONTAL);
                                ll2.setPadding(0, 0, 0, 10);

                                TextView tv3 = new TextView(context);
                                tv3.setText(getResources().getString(R.string.location) + "  ");
                                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                                tv3.setTextColor(color);
                                tv3.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p2.weight = 1;
                                tv3.setLayoutParams(p2);
                                ll2.addView(tv3);

                                TextView tv4 = new TextView(context);
                                tv4.setText(String.valueOf(encounterObject[0][4]));
                                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv4.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p3.weight = 1;
                                tv4.setLayoutParams(p3);
                                ll2.addView(tv4);

                                encounterDetailsLayout.addView(ll2);

                                obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            } else{

                            }

                            for (int j = 0; j < obs.length; j++) {
                                LinearLayout ll5 = new LinearLayout(context);
                                ll5.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv8 = new TextView(context);
                                tv8.setText(App.convertToTitleCase(String.valueOf(obs[j][1])) + ":  ");
                                tv8.setTextSize(getResources().getDimension(R.dimen.small));
                                tv8.setTextColor(color);
                                //tv8.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv8.setLayoutParams(p);
                                ll5.addView(tv8);

                                TextView tv9 = new TextView(context);
                                tv9.setText(String.valueOf(App.convertToTitleCase(String.valueOf(obs[j][0]))));
                                tv9.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv9.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv9.setLayoutParams(p1);
                                ll5.addView(tv9);

                                moreLayout.addView(ll5);
                            }


                            for (int k = 0; k < petReportLayout.getChildCount(); k++) {

                                View view = petReportLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);

                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);
                                LinearLayout mL2 = (LinearLayout) ((LinearLayout) view).getChildAt(2);

                                mL.setVisibility(View.GONE);
                                mL2.setVisibility(View.GONE);
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                                DrawableCompat.setTint(t.getCompoundDrawables()[2], color2);
                                t.setTypeface(t.getTypeface(), Typeface.NORMAL);
                                t.setTextSize(getResources().getDimension(R.dimen.small));

                            }

                            moreLayout.setVisibility(View.VISIBLE);
                            encounterDetailsLayout.setVisibility(View.VISIBLE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setTypeface(text.getTypeface(), Typeface.BOLD);
                            text.setTextSize(getResources().getDimension(R.dimen.medium));
                        }
                    }
                });

            }

        }

        final Object[][] commorbiditiesEncounters = serverService.getAllEncounterFromLocalDB(getResources().getString(R.string.comorbidities));
        if (commorbiditiesEncounters == null || commorbiditiesEncounters.length == 0) {
            commorbidities.setVisibility(View.GONE);
            commorbiditiesReportLayout.setVisibility(View.GONE);
        } else {

            commorbidities.setVisibility(View.VISIBLE);
            for (int i = 0; i < commorbiditiesEncounters.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(context);
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                final LinearLayout encounterDetailsLayout = new LinearLayout(context);
                encounterDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                encounterDetailsLayout.setPadding(0, 10, 0, 0);

                final LinearLayout moreLayout = new LinearLayout(context);
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final TextView text = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(commorbiditiesEncounters[i][0]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                text.setPadding(10, 0, 0, 0);
                text.setTag(String.valueOf(commorbiditiesEncounters[i][1]));
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                verticalLayout.addView(encounterDetailsLayout);
                verticalLayout.addView(moreLayout);

                LinearLayout ll1 = new LinearLayout(context);
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv1 = new TextView(context);
                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                tv1.setTextColor(color);
                //tv1.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                tv1.setLayoutParams(p);
                ll1.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(App.convertToTitleCase(String.valueOf(commorbiditiesEncounters[i][3])));
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                //tv2.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p1.weight = 1;
                tv2.setLayoutParams(p1);
                ll1.addView(tv2);

                encounterDetailsLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(context);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setPadding(0, 0, 0, 10);

                TextView tv3 = new TextView(context);
                tv3.setText(getResources().getString(R.string.location) + "  ");
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                tv3.setTextColor(color);
                //tv3.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p2.weight = 1;
                tv3.setLayoutParams(p2);
                ll2.addView(tv3);

                TextView tv4 = new TextView(context);
                tv4.setText(String.valueOf(commorbiditiesEncounters[i][4]));
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                //tv4.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p3.weight = 1;
                tv4.setLayoutParams(p3);
                ll2.addView(tv4);

                encounterDetailsLayout.addView(ll2);

                encounterDetailsLayout.setVisibility(View.GONE);
                moreLayout.setVisibility(View.GONE);

                commorbiditiesReportLayout.addView(verticalLayout);

                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            encounterDetailsLayout.setVisibility(View.GONE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                            text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                            text.setTextSize(getResources().getDimension(R.dimen.small));
                        } else {

                            moreLayout.removeAllViews();
                            Object[][] obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            if (obs.length == 0) {
                                encounterDetailsLayout.removeAllViews();

                                Object[][] encounterObject = serverService.getEncounterIdByEncounterType(text.getText().toString());
                                text.setTag(String.valueOf(encounterObject[0][1]));

                                LinearLayout ll1 = new LinearLayout(context);
                                ll1.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv1 = new TextView(context);
                                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                                tv1.setTextColor(color);
                                //tv1.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv1.setLayoutParams(p);
                                ll1.addView(tv1);

                                TextView tv2 = new TextView(context);
                                tv2.setText(App.convertToTitleCase(String.valueOf(encounterObject[0][3])));
                                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv2.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv2.setLayoutParams(p1);
                                ll1.addView(tv2);

                                encounterDetailsLayout.addView(ll1);

                                LinearLayout ll2 = new LinearLayout(context);
                                ll2.setOrientation(LinearLayout.HORIZONTAL);
                                ll2.setPadding(0, 0, 0, 10);

                                TextView tv3 = new TextView(context);
                                tv3.setText(getResources().getString(R.string.location) + "  ");
                                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                                tv3.setTextColor(color);
                                tv3.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p2.weight = 1;
                                tv3.setLayoutParams(p2);
                                ll2.addView(tv3);

                                TextView tv4 = new TextView(context);
                                tv4.setText(String.valueOf(encounterObject[0][4]));
                                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv4.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p3.weight = 1;
                                tv4.setLayoutParams(p3);
                                ll2.addView(tv4);

                                encounterDetailsLayout.addView(ll2);

                                obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            } else{

                            }

                            for (int j = 0; j < obs.length; j++) {
                                LinearLayout ll5 = new LinearLayout(context);
                                ll5.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv8 = new TextView(context);
                                tv8.setText(App.convertToTitleCase(String.valueOf(obs[j][1])) + ":  ");
                                tv8.setTextSize(getResources().getDimension(R.dimen.small));
                                tv8.setTextColor(color);
                                //tv8.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv8.setLayoutParams(p);
                                ll5.addView(tv8);

                                TextView tv9 = new TextView(context);
                                tv9.setText(String.valueOf(App.convertToTitleCase(String.valueOf(obs[j][0]))));
                                tv9.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv9.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv9.setLayoutParams(p1);
                                ll5.addView(tv9);

                                moreLayout.addView(ll5);
                            }


                            for (int k = 0; k < commorbiditiesReportLayout.getChildCount(); k++) {

                                View view = commorbiditiesReportLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);

                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);
                                LinearLayout mL2 = (LinearLayout) ((LinearLayout) view).getChildAt(2);

                                mL.setVisibility(View.GONE);
                                mL2.setVisibility(View.GONE);
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                                DrawableCompat.setTint(t.getCompoundDrawables()[2], color2);
                                t.setTypeface(t.getTypeface(), Typeface.NORMAL);
                                t.setTextSize(getResources().getDimension(R.dimen.small));

                            }

                            moreLayout.setVisibility(View.VISIBLE);
                            encounterDetailsLayout.setVisibility(View.VISIBLE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setTypeface(text.getTypeface(), Typeface.BOLD);
                            text.setTextSize(getResources().getDimension(R.dimen.medium));
                        }
                    }
                });

            }

        }

        final Object[][] childhoodtbEncounters = serverService.getAllEncounterFromLocalDB(getResources().getString(R.string.childhood_tb));
        if (childhoodtbEncounters == null || childhoodtbEncounters.length == 0) {
            childhoodtb.setVisibility(View.GONE);
            childhoodtbReportLayout.setVisibility(View.GONE);
        } else {

            childhoodtb.setVisibility(View.VISIBLE);
            for (int i = 0; i < childhoodtbEncounters.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(context);
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                final LinearLayout encounterDetailsLayout = new LinearLayout(context);
                encounterDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                encounterDetailsLayout.setPadding(0, 10, 0, 0);

                final LinearLayout moreLayout = new LinearLayout(context);
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final TextView text = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(childhoodtbEncounters[i][0]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                text.setPadding(10, 0, 0, 0);
                text.setTag(String.valueOf(childhoodtbEncounters[i][1]));
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                verticalLayout.addView(encounterDetailsLayout);
                verticalLayout.addView(moreLayout);

                LinearLayout ll1 = new LinearLayout(context);
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv1 = new TextView(context);
                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                tv1.setTextColor(color);
                //tv1.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                tv1.setLayoutParams(p);
                ll1.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(App.convertToTitleCase(String.valueOf(childhoodtbEncounters[i][3])));
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                //tv2.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p1.weight = 1;
                tv2.setLayoutParams(p1);
                ll1.addView(tv2);

                encounterDetailsLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(context);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setPadding(0, 0, 0, 10);

                TextView tv3 = new TextView(context);
                tv3.setText(getResources().getString(R.string.location) + "  ");
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                tv3.setTextColor(color);
                //tv3.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p2.weight = 1;
                tv3.setLayoutParams(p2);
                ll2.addView(tv3);

                TextView tv4 = new TextView(context);
                tv4.setText(String.valueOf(childhoodtbEncounters[i][4]));
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                //tv4.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p3.weight = 1;
                tv4.setLayoutParams(p3);
                ll2.addView(tv4);

                encounterDetailsLayout.addView(ll2);

                encounterDetailsLayout.setVisibility(View.GONE);
                moreLayout.setVisibility(View.GONE);

                childhoodtbReportLayout.addView(verticalLayout);

                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            encounterDetailsLayout.setVisibility(View.GONE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                            text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                            text.setTextSize(getResources().getDimension(R.dimen.small));
                        } else {

                            moreLayout.removeAllViews();
                            Object[][] obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            if (obs.length == 0) {
                                encounterDetailsLayout.removeAllViews();

                                Object[][] encounterObject = serverService.getEncounterIdByEncounterType(text.getText().toString());
                                text.setTag(String.valueOf(encounterObject[0][1]));

                                LinearLayout ll1 = new LinearLayout(context);
                                ll1.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv1 = new TextView(context);
                                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                                tv1.setTextColor(color);
                                //tv1.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv1.setLayoutParams(p);
                                ll1.addView(tv1);

                                TextView tv2 = new TextView(context);
                                tv2.setText(App.convertToTitleCase(String.valueOf(encounterObject[0][3])));
                                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv2.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv2.setLayoutParams(p1);
                                ll1.addView(tv2);

                                encounterDetailsLayout.addView(ll1);

                                LinearLayout ll2 = new LinearLayout(context);
                                ll2.setOrientation(LinearLayout.HORIZONTAL);
                                ll2.setPadding(0, 0, 0, 10);

                                TextView tv3 = new TextView(context);
                                tv3.setText(getResources().getString(R.string.location) + "  ");
                                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                                tv3.setTextColor(color);
                                tv3.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p2.weight = 1;
                                tv3.setLayoutParams(p2);
                                ll2.addView(tv3);

                                TextView tv4 = new TextView(context);
                                tv4.setText(String.valueOf(encounterObject[0][4]));
                                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv4.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p3.weight = 1;
                                tv4.setLayoutParams(p3);
                                ll2.addView(tv4);

                                encounterDetailsLayout.addView(ll2);

                                obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            } else{

                            }

                            for (int j = 0; j < obs.length; j++) {
                                LinearLayout ll5 = new LinearLayout(context);
                                ll5.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv8 = new TextView(context);
                                tv8.setText(App.convertToTitleCase(String.valueOf(obs[j][1])) + ":  ");
                                tv8.setTextSize(getResources().getDimension(R.dimen.small));
                                tv8.setTextColor(color);
                                //tv8.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv8.setLayoutParams(p);
                                ll5.addView(tv8);

                                TextView tv9 = new TextView(context);
                                tv9.setText(String.valueOf(App.convertToTitleCase(String.valueOf(obs[j][0]))));
                                tv9.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv9.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv9.setLayoutParams(p1);
                                ll5.addView(tv9);

                                moreLayout.addView(ll5);
                            }


                            for (int k = 0; k < childhoodtbReportLayout.getChildCount(); k++) {

                                View view = childhoodtbReportLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);

                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);
                                LinearLayout mL2 = (LinearLayout) ((LinearLayout) view).getChildAt(2);

                                mL.setVisibility(View.GONE);
                                mL2.setVisibility(View.GONE);
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                                DrawableCompat.setTint(t.getCompoundDrawables()[2], color2);
                                t.setTypeface(t.getTypeface(), Typeface.NORMAL);
                                t.setTextSize(getResources().getDimension(R.dimen.small));

                            }

                            moreLayout.setVisibility(View.VISIBLE);
                            encounterDetailsLayout.setVisibility(View.VISIBLE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setTypeface(text.getTypeface(), Typeface.BOLD);
                            text.setTextSize(getResources().getDimension(R.dimen.medium));
                        }
                    }
                });

            }

        }

        Object[][] fastEncounters = serverService.getAllEncounterFromLocalDB(getResources().getString(R.string.fast));
        if (fastEncounters == null || fastEncounters.length == 0) {
            fast.setVisibility(View.GONE);
            fastReportLayout.setVisibility(View.GONE);
        } else {

            fast.setVisibility(View.VISIBLE);
            for (int i = 0; i < fastEncounters.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(context);
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                final LinearLayout encounterDetailsLayout = new LinearLayout(context);
                encounterDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                encounterDetailsLayout.setPadding(0, 10, 0, 0);

                final LinearLayout moreLayout = new LinearLayout(context);
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final TextView text = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(fastEncounters[i][0]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                text.setPadding(10, 0, 0, 0);
                text.setTag(String.valueOf(fastEncounters[i][1]));
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                verticalLayout.addView(encounterDetailsLayout);
                verticalLayout.addView(moreLayout);

                LinearLayout ll1 = new LinearLayout(context);
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv1 = new TextView(context);
                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                tv1.setTextColor(color);
                //tv1.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                tv1.setLayoutParams(p);
                ll1.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(App.convertToTitleCase(String.valueOf(fastEncounters[i][3])));
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                //tv2.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p1.weight = 1;
                tv2.setLayoutParams(p1);
                ll1.addView(tv2);

                encounterDetailsLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(context);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setPadding(0, 0, 0, 10);

                TextView tv3 = new TextView(context);
                tv3.setText(getResources().getString(R.string.location) + "  ");
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                tv3.setTextColor(color);
                //tv3.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p2.weight = 1;
                tv3.setLayoutParams(p2);
                ll2.addView(tv3);

                TextView tv4 = new TextView(context);
                tv4.setText(String.valueOf(fastEncounters[i][4]));
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                //tv4.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p3.weight = 1;
                tv4.setLayoutParams(p3);
                ll2.addView(tv4);

                encounterDetailsLayout.addView(ll2);

                encounterDetailsLayout.setVisibility(View.GONE);
                moreLayout.setVisibility(View.GONE);

                fastReportLayout.addView(verticalLayout);

                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            encounterDetailsLayout.setVisibility(View.GONE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                            text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                            text.setTextSize(getResources().getDimension(R.dimen.small));
                        } else {

                            moreLayout.removeAllViews();
                            Object[][] obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            if (obs.length == 0) {
                                encounterDetailsLayout.removeAllViews();

                                Object[][] encounterObject = serverService.getEncounterIdByEncounterType(text.getText().toString());
                                text.setTag(String.valueOf(encounterObject[0][1]));

                                LinearLayout ll1 = new LinearLayout(context);
                                ll1.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv1 = new TextView(context);
                                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                                tv1.setTextColor(color);
                                //tv1.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv1.setLayoutParams(p);
                                ll1.addView(tv1);

                                TextView tv2 = new TextView(context);
                                tv2.setText(App.convertToTitleCase(String.valueOf(encounterObject[0][3])));
                                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv2.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv2.setLayoutParams(p1);
                                ll1.addView(tv2);

                                encounterDetailsLayout.addView(ll1);

                                LinearLayout ll2 = new LinearLayout(context);
                                ll2.setOrientation(LinearLayout.HORIZONTAL);
                                ll2.setPadding(0, 0, 0, 10);

                                TextView tv3 = new TextView(context);
                                tv3.setText(getResources().getString(R.string.location) + "  ");
                                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                                tv3.setTextColor(color);
                                tv3.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p2.weight = 1;
                                tv3.setLayoutParams(p2);
                                ll2.addView(tv3);

                                TextView tv4 = new TextView(context);
                                tv4.setText(String.valueOf(encounterObject[0][4]));
                                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv4.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p3.weight = 1;
                                tv4.setLayoutParams(p3);
                                ll2.addView(tv4);

                                encounterDetailsLayout.addView(ll2);

                                obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            } else{

                            }

                            for (int j = 0; j < obs.length; j++) {
                                LinearLayout ll5 = new LinearLayout(context);
                                ll5.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv8 = new TextView(context);
                                tv8.setText(App.convertToTitleCase(String.valueOf(obs[j][1])) + ":  ");
                                tv8.setTextSize(getResources().getDimension(R.dimen.small));
                                tv8.setTextColor(color);
                                //tv8.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv8.setLayoutParams(p);
                                ll5.addView(tv8);

                                TextView tv9 = new TextView(context);
                                tv9.setText(String.valueOf(App.convertToTitleCase(String.valueOf(obs[j][0]))));
                                tv9.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv9.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv9.setLayoutParams(p1);
                                ll5.addView(tv9);

                                moreLayout.addView(ll5);
                            }


                            for (int k = 0; k < fastReportLayout.getChildCount(); k++) {

                                View view = fastReportLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);

                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);
                                LinearLayout mL2 = (LinearLayout) ((LinearLayout) view).getChildAt(2);

                                mL.setVisibility(View.GONE);
                                mL2.setVisibility(View.GONE);
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                                DrawableCompat.setTint(t.getCompoundDrawables()[2], color2);
                                t.setTypeface(t.getTypeface(), Typeface.NORMAL);
                                t.setTextSize(getResources().getDimension(R.dimen.small));

                            }

                            moreLayout.setVisibility(View.VISIBLE);
                            encounterDetailsLayout.setVisibility(View.VISIBLE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setTypeface(text.getTypeface(), Typeface.BOLD);
                            text.setTextSize(getResources().getDimension(R.dimen.medium));
                        }
                    }
                });

            }

        }

        Object[][] pmdtEncounters = serverService.getAllEncounterFromLocalDB(getResources().getString(R.string.pmdt));
        if (pmdtEncounters == null || pmdtEncounters.length == 0) {
            pmdt.setVisibility(View.GONE);
            pmdtReportLayout.setVisibility(View.GONE);
        } else {

            pmdt.setVisibility(View.VISIBLE);
            for (int i = 0; i < pmdtEncounters.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(context);
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                final LinearLayout encounterDetailsLayout = new LinearLayout(context);
                encounterDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                encounterDetailsLayout.setPadding(0, 10, 0, 0);

                final LinearLayout moreLayout = new LinearLayout(context);
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final TextView text = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(pmdtEncounters[i][0]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                text.setPadding(10, 0, 0, 0);
                text.setTag(String.valueOf(pmdtEncounters[i][1]));
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                verticalLayout.addView(encounterDetailsLayout);
                verticalLayout.addView(moreLayout);

                LinearLayout ll1 = new LinearLayout(context);
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv1 = new TextView(context);
                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                tv1.setTextColor(color);
                //tv1.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                tv1.setLayoutParams(p);
                ll1.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(App.convertToTitleCase(String.valueOf(pmdtEncounters[i][3])));
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                //tv2.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p1.weight = 1;
                tv2.setLayoutParams(p1);
                ll1.addView(tv2);

                encounterDetailsLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(context);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setPadding(0, 0, 0, 10);

                TextView tv3 = new TextView(context);
                tv3.setText(getResources().getString(R.string.location) + "  ");
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                tv3.setTextColor(color);
                //tv3.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p2.weight = 1;
                tv3.setLayoutParams(p2);
                ll2.addView(tv3);

                TextView tv4 = new TextView(context);
                tv4.setText(String.valueOf(pmdtEncounters[i][4]));
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                //tv4.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p3.weight = 1;
                tv4.setLayoutParams(p3);
                ll2.addView(tv4);

                encounterDetailsLayout.addView(ll2);

                encounterDetailsLayout.setVisibility(View.GONE);
                moreLayout.setVisibility(View.GONE);

                pmdtReportLayout.addView(verticalLayout);

                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            encounterDetailsLayout.setVisibility(View.GONE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                            text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                            text.setTextSize(getResources().getDimension(R.dimen.small));
                        } else {

                            moreLayout.removeAllViews();
                            Object[][] obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            if (obs.length == 0) {
                                encounterDetailsLayout.removeAllViews();

                                Object[][] encounterObject = serverService.getEncounterIdByEncounterType(text.getText().toString());
                                text.setTag(String.valueOf(encounterObject[0][1]));

                                LinearLayout ll1 = new LinearLayout(context);
                                ll1.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv1 = new TextView(context);
                                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                                tv1.setTextColor(color);
                                //tv1.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv1.setLayoutParams(p);
                                ll1.addView(tv1);

                                TextView tv2 = new TextView(context);
                                tv2.setText(App.convertToTitleCase(String.valueOf(encounterObject[0][3])));
                                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv2.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv2.setLayoutParams(p1);
                                ll1.addView(tv2);

                                encounterDetailsLayout.addView(ll1);

                                LinearLayout ll2 = new LinearLayout(context);
                                ll2.setOrientation(LinearLayout.HORIZONTAL);
                                ll2.setPadding(0, 0, 0, 10);

                                TextView tv3 = new TextView(context);
                                tv3.setText(getResources().getString(R.string.location) + "  ");
                                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                                tv3.setTextColor(color);
                                tv3.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p2.weight = 1;
                                tv3.setLayoutParams(p2);
                                ll2.addView(tv3);

                                TextView tv4 = new TextView(context);
                                tv4.setText(String.valueOf(encounterObject[0][4]));
                                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv4.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p3.weight = 1;
                                tv4.setLayoutParams(p3);
                                ll2.addView(tv4);

                                encounterDetailsLayout.addView(ll2);

                                obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            } else{

                            }

                            for (int j = 0; j < obs.length; j++) {
                                LinearLayout ll5 = new LinearLayout(context);
                                ll5.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv8 = new TextView(context);
                                tv8.setText(App.convertToTitleCase(String.valueOf(obs[j][1])) + ":  ");
                                tv8.setTextSize(getResources().getDimension(R.dimen.small));
                                tv8.setTextColor(color);
                                //tv8.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv8.setLayoutParams(p);
                                ll5.addView(tv8);

                                TextView tv9 = new TextView(context);
                                tv9.setText(String.valueOf(App.convertToTitleCase(String.valueOf(obs[j][0]))));
                                tv9.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv9.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv9.setLayoutParams(p1);
                                ll5.addView(tv9);

                                moreLayout.addView(ll5);
                            }


                            for (int k = 0; k < pmdtReportLayout.getChildCount(); k++) {

                                View view = pmdtReportLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);

                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);
                                LinearLayout mL2 = (LinearLayout) ((LinearLayout) view).getChildAt(2);

                                mL.setVisibility(View.GONE);
                                mL2.setVisibility(View.GONE);
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                                DrawableCompat.setTint(t.getCompoundDrawables()[2], color2);
                                t.setTypeface(t.getTypeface(), Typeface.NORMAL);
                                t.setTextSize(getResources().getDimension(R.dimen.small));

                            }

                            moreLayout.setVisibility(View.VISIBLE);
                            encounterDetailsLayout.setVisibility(View.VISIBLE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setTypeface(text.getTypeface(), Typeface.BOLD);
                            text.setTextSize(getResources().getDimension(R.dimen.medium));
                        }
                    }
                });

            }

        }

        Object[][] ccEncounters = serverService.getAllEncounterFromLocalDB(getResources().getString(R.string.cc));
        if (ccEncounters == null || ccEncounters.length == 0) {
            cc.setVisibility(View.GONE);
            ccReportLayout.setVisibility(View.GONE);
        } else {

            cc.setVisibility(View.VISIBLE);
            for (int i = 0; i < ccEncounters.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(context);
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                final LinearLayout encounterDetailsLayout = new LinearLayout(context);
                encounterDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                encounterDetailsLayout.setPadding(0, 10, 0, 0);

                final LinearLayout moreLayout = new LinearLayout(context);
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final TextView text = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(ccEncounters[i][0]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                text.setPadding(10, 0, 0, 0);
                text.setTag(String.valueOf(ccEncounters[i][1]));
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                verticalLayout.addView(encounterDetailsLayout);
                verticalLayout.addView(moreLayout);

                LinearLayout ll1 = new LinearLayout(context);
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv1 = new TextView(context);
                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                tv1.setTextColor(color);
                //tv1.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                tv1.setLayoutParams(p);
                ll1.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(App.convertToTitleCase(String.valueOf(ccEncounters[i][3])));
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                //tv2.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p1.weight = 1;
                tv2.setLayoutParams(p1);
                ll1.addView(tv2);

                encounterDetailsLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(context);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setPadding(0, 0, 0, 10);

                TextView tv3 = new TextView(context);
                tv3.setText(getResources().getString(R.string.location) + "  ");
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                tv3.setTextColor(color);
                //tv3.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p2.weight = 1;
                tv3.setLayoutParams(p2);
                ll2.addView(tv3);

                TextView tv4 = new TextView(context);
                tv4.setText(String.valueOf(ccEncounters[i][4]));
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                //tv4.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p3.weight = 1;
                tv4.setLayoutParams(p3);
                ll2.addView(tv4);

                encounterDetailsLayout.addView(ll2);

                encounterDetailsLayout.setVisibility(View.GONE);
                moreLayout.setVisibility(View.GONE);

                ccReportLayout.addView(verticalLayout);

                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            encounterDetailsLayout.setVisibility(View.GONE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                            text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                            text.setTextSize(getResources().getDimension(R.dimen.small));
                        } else {

                            moreLayout.removeAllViews();
                            Object[][] obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            if (obs.length == 0) {
                                encounterDetailsLayout.removeAllViews();

                                Object[][] encounterObject = serverService.getEncounterIdByEncounterType(text.getText().toString());
                                text.setTag(String.valueOf(encounterObject[0][1]));

                                LinearLayout ll1 = new LinearLayout(context);
                                ll1.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv1 = new TextView(context);
                                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                                tv1.setTextColor(color);
                                //tv1.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv1.setLayoutParams(p);
                                ll1.addView(tv1);

                                TextView tv2 = new TextView(context);
                                tv2.setText(App.convertToTitleCase(String.valueOf(encounterObject[0][3])));
                                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv2.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv2.setLayoutParams(p1);
                                ll1.addView(tv2);

                                encounterDetailsLayout.addView(ll1);

                                LinearLayout ll2 = new LinearLayout(context);
                                ll2.setOrientation(LinearLayout.HORIZONTAL);
                                ll2.setPadding(0, 0, 0, 10);

                                TextView tv3 = new TextView(context);
                                tv3.setText(getResources().getString(R.string.location) + "  ");
                                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                                tv3.setTextColor(color);
                                tv3.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p2.weight = 1;
                                tv3.setLayoutParams(p2);
                                ll2.addView(tv3);

                                TextView tv4 = new TextView(context);
                                tv4.setText(String.valueOf(encounterObject[0][4]));
                                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv4.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p3.weight = 1;
                                tv4.setLayoutParams(p3);
                                ll2.addView(tv4);

                                encounterDetailsLayout.addView(ll2);

                                obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            } else{

                            }

                            for (int j = 0; j < obs.length; j++) {
                                LinearLayout ll5 = new LinearLayout(context);
                                ll5.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv8 = new TextView(context);
                                tv8.setText(App.convertToTitleCase(String.valueOf(obs[j][1])) + ":  ");
                                tv8.setTextSize(getResources().getDimension(R.dimen.small));
                                tv8.setTextColor(color);
                                //tv8.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv8.setLayoutParams(p);
                                ll5.addView(tv8);

                                TextView tv9 = new TextView(context);
                                tv9.setText(String.valueOf(App.convertToTitleCase(String.valueOf(obs[j][0]))));
                                tv9.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv9.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv9.setLayoutParams(p1);
                                ll5.addView(tv9);

                                moreLayout.addView(ll5);
                            }


                            for (int k = 0; k < ccReportLayout.getChildCount(); k++) {

                                View view = ccReportLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);

                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);
                                LinearLayout mL2 = (LinearLayout) ((LinearLayout) view).getChildAt(2);

                                mL.setVisibility(View.GONE);
                                mL2.setVisibility(View.GONE);
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                                DrawableCompat.setTint(t.getCompoundDrawables()[2], color2);
                                t.setTypeface(t.getTypeface(), Typeface.NORMAL);
                                t.setTextSize(getResources().getDimension(R.dimen.small));

                            }

                            moreLayout.setVisibility(View.VISIBLE);
                            encounterDetailsLayout.setVisibility(View.VISIBLE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setTypeface(text.getTypeface(), Typeface.BOLD);
                            text.setTextSize(getResources().getDimension(R.dimen.medium));
                        }
                    }
                });

            }

        }

        Object[][] commonEncounters = serverService.getAllCommonEncounterFromLocalDB();
        if (commonEncounters == null || commonEncounters.length == 0) {
            common.setVisibility(View.GONE);
            commonReportLayout.setVisibility(View.GONE);
        } else {

            common.setVisibility(View.VISIBLE);
            for (int i = 0; i < commonEncounters.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(context);
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                final LinearLayout encounterDetailsLayout = new LinearLayout(context);
                encounterDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                encounterDetailsLayout.setPadding(0, 10, 0, 0);

                final LinearLayout moreLayout = new LinearLayout(context);
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final TextView text = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(commonEncounters[i][0]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                text.setPadding(10, 0, 0, 0);
                text.setTag(String.valueOf(commonEncounters[i][1]));
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                verticalLayout.addView(encounterDetailsLayout);
                verticalLayout.addView(moreLayout);

                LinearLayout ll1 = new LinearLayout(context);
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv1 = new TextView(context);
                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                tv1.setTextColor(color);
                //tv1.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                tv1.setLayoutParams(p);
                ll1.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(App.convertToTitleCase(String.valueOf(commonEncounters[i][3])));
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                //tv2.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p1.weight = 1;
                tv2.setLayoutParams(p1);
                ll1.addView(tv2);

                encounterDetailsLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(context);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setPadding(0, 0, 0, 10);

                TextView tv3 = new TextView(context);
                tv3.setText(getResources().getString(R.string.location) + "  ");
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                tv3.setTextColor(color);
                //tv3.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p2.weight = 1;
                tv3.setLayoutParams(p2);
                ll2.addView(tv3);

                TextView tv4 = new TextView(context);
                tv4.setText(String.valueOf(commonEncounters[i][4]));
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                //tv4.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p3.weight = 1;
                tv4.setLayoutParams(p3);
                ll2.addView(tv4);

                encounterDetailsLayout.addView(ll2);

                encounterDetailsLayout.setVisibility(View.GONE);
                moreLayout.setVisibility(View.GONE);

                commonReportLayout.addView(verticalLayout);

                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            encounterDetailsLayout.setVisibility(View.GONE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                            text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                            text.setTextSize(getResources().getDimension(R.dimen.small));
                        } else {

                            moreLayout.removeAllViews();
                            Object[][] obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            if (obs.length == 0) {
                                encounterDetailsLayout.removeAllViews();

                                Object[][] encounterObject = serverService.getEncounterIdByEncounterType(text.getText().toString());
                                text.setTag(String.valueOf(encounterObject[0][1]));

                                LinearLayout ll1 = new LinearLayout(context);
                                ll1.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv1 = new TextView(context);
                                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                                tv1.setTextColor(color);
                                //tv1.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv1.setLayoutParams(p);
                                ll1.addView(tv1);

                                TextView tv2 = new TextView(context);
                                tv2.setText(App.convertToTitleCase(String.valueOf(encounterObject[0][3])));
                                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv2.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv2.setLayoutParams(p1);
                                ll1.addView(tv2);

                                encounterDetailsLayout.addView(ll1);

                                LinearLayout ll2 = new LinearLayout(context);
                                ll2.setOrientation(LinearLayout.HORIZONTAL);
                                ll2.setPadding(0, 0, 0, 10);

                                TextView tv3 = new TextView(context);
                                tv3.setText(getResources().getString(R.string.location) + "  ");
                                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                                tv3.setTextColor(color);
                                tv3.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p2.weight = 1;
                                tv3.setLayoutParams(p2);
                                ll2.addView(tv3);

                                TextView tv4 = new TextView(context);
                                tv4.setText(String.valueOf(encounterObject[0][4]));
                                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv4.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p3.weight = 1;
                                tv4.setLayoutParams(p3);
                                ll2.addView(tv4);

                                encounterDetailsLayout.addView(ll2);

                                obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            } else{

                            }

                            for (int j = 0; j < obs.length; j++) {
                                LinearLayout ll5 = new LinearLayout(context);
                                ll5.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv8 = new TextView(context);
                                tv8.setText(App.convertToTitleCase(String.valueOf(obs[j][1])) + ":  ");
                                tv8.setTextSize(getResources().getDimension(R.dimen.small));
                                tv8.setTextColor(color);
                                //tv8.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv8.setLayoutParams(p);
                                ll5.addView(tv8);

                                TextView tv9 = new TextView(context);
                                tv9.setText(String.valueOf(App.convertToTitleCase(String.valueOf(obs[j][0]))));
                                tv9.setTextSize(getResources().getDimension(R.dimen.small));
                                //tv9.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv9.setLayoutParams(p1);
                                ll5.addView(tv9);

                                moreLayout.addView(ll5);
                            }


                            for (int k = 0; k < commonReportLayout.getChildCount(); k++) {

                                View view = commonReportLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);

                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);
                                LinearLayout mL2 = (LinearLayout) ((LinearLayout) view).getChildAt(2);

                                mL.setVisibility(View.GONE);
                                mL2.setVisibility(View.GONE);
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                                DrawableCompat.setTint(t.getCompoundDrawables()[2], color2);
                                t.setTypeface(t.getTypeface(), Typeface.NORMAL);
                                t.setTextSize(getResources().getDimension(R.dimen.small));

                            }

                            moreLayout.setVisibility(View.VISIBLE);
                            encounterDetailsLayout.setVisibility(View.VISIBLE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setTypeface(text.getTypeface(), Typeface.BOLD);
                            text.setTextSize(getResources().getDimension(R.dimen.medium));
                        }
                    }
                });

            }

        }

        if(program.equalsIgnoreCase(getResources().getString(R.string.fast)) && fast.getVisibility() == View.VISIBLE){
            fastReportLayout.setVisibility(View.VISIBLE);
            fast.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
            DrawableCompat.setTint(fast.getCompoundDrawables()[2], color1);
        } else if(program.equalsIgnoreCase(getResources().getString(R.string.childhood_tb)) && childhoodtb.getVisibility() == View.VISIBLE){
            childhoodtbReportLayout.setVisibility(View.VISIBLE);
            childhoodtb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
            DrawableCompat.setTint(childhoodtb.getCompoundDrawables()[2], color1);
        } else if(program.equalsIgnoreCase(getResources().getString(R.string.comorbidities)) && commorbidities.getVisibility() == View.VISIBLE){
            commorbiditiesReportLayout.setVisibility(View.VISIBLE);
            commorbidities.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
            DrawableCompat.setTint(commorbidities.getCompoundDrawables()[2], color1);
        } else if(program.equalsIgnoreCase(getResources().getString(R.string.pet)) && pet.getVisibility() == View.VISIBLE){
            petReportLayout.setVisibility(View.VISIBLE);
            pet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
            DrawableCompat.setTint(pet.getCompoundDrawables()[2], color1);
        } else if(program.equalsIgnoreCase(getResources().getString(R.string.pmdt)) && pmdt.getVisibility() == View.VISIBLE){
            pmdtReportLayout.setVisibility(View.VISIBLE);
            pmdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
            DrawableCompat.setTint(pmdt.getCompoundDrawables()[2], color1);
        } else if(program.equalsIgnoreCase(getResources().getString(R.string.cc)) && cc.getVisibility() == View.VISIBLE){
            ccReportLayout.setVisibility(View.VISIBLE);
            cc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
            DrawableCompat.setTint(cc.getCompoundDrawables()[2], color1);
        } else if(program.equalsIgnoreCase(getResources().getString(R.string.ztts)) && ztts.getVisibility() == View.VISIBLE){
            zttsReportLayout.setVisibility(View.VISIBLE);
            ztts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
            DrawableCompat.setTint(ztts.getCompoundDrawables()[2], color1);
        } else {
            zttsReportLayout.setVisibility(View.VISIBLE);
            ztts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
            DrawableCompat.setTint(ztts.getCompoundDrawables()[2], color1);
        }

    }

    @Override
    public void onClick(View v) {

        int color = App.getColor(context, R.attr.colorPrimaryDark);
        int color1 = App.getColor(context, R.attr.colorAccent);
        int color2 = App.getColor(context, R.attr.colorPrimary);

        if(v == fast){
            if (fastReportLayout.getVisibility() == View.VISIBLE) {
                fastReportLayout.setVisibility(View.GONE);
                fast.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(fast.getCompoundDrawables()[2], color);
            }
            else {
                fastReportLayout.setVisibility(View.VISIBLE);
                fast.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(fast.getCompoundDrawables()[2], color1);
                childhoodtbReportLayout.setVisibility(View.GONE);
                childhoodtb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(childhoodtb.getCompoundDrawables()[2], color);
                commorbiditiesReportLayout.setVisibility(View.GONE);
                commorbidities.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(commorbidities.getCompoundDrawables()[2], color);
                petReportLayout.setVisibility(View.GONE);
                pet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pet.getCompoundDrawables()[2], color);
                ccReportLayout.setVisibility(View.GONE);
                cc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(cc.getCompoundDrawables()[2], color);
                pmdtReportLayout.setVisibility(View.GONE);
                pmdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pmdt.getCompoundDrawables()[2], color);
                commonReportLayout.setVisibility(View.GONE);
                common.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(common.getCompoundDrawables()[2], color1);
            }
        } else if(v == childhoodtb){
            if (childhoodtbReportLayout.getVisibility() == View.VISIBLE) {
                childhoodtbReportLayout.setVisibility(View.GONE);
                childhoodtb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(childhoodtb.getCompoundDrawables()[2], color);
            }
            else {
                childhoodtbReportLayout.setVisibility(View.VISIBLE);
                childhoodtb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(childhoodtb.getCompoundDrawables()[2], color1);
                fastReportLayout.setVisibility(View.GONE);
                fast.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(fast.getCompoundDrawables()[2], color);
                commorbiditiesReportLayout.setVisibility(View.GONE);
                commorbidities.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(commorbidities.getCompoundDrawables()[2], color);
                petReportLayout.setVisibility(View.GONE);
                pet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pet.getCompoundDrawables()[2], color);
                ccReportLayout.setVisibility(View.GONE);
                cc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(cc.getCompoundDrawables()[2], color);
                pmdtReportLayout.setVisibility(View.GONE);
                pmdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pmdt.getCompoundDrawables()[2], color);
                commonReportLayout.setVisibility(View.GONE);
                common.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(common.getCompoundDrawables()[2], color1);
            }
        } else if(v == commorbidities){
            if (commorbiditiesReportLayout.getVisibility() == View.VISIBLE) {
                commorbiditiesReportLayout.setVisibility(View.GONE);
                commorbidities.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(commorbidities.getCompoundDrawables()[2], color);
            }
            else {
                commorbiditiesReportLayout.setVisibility(View.VISIBLE);
                commorbidities.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(commorbidities.getCompoundDrawables()[2], color1);
                childhoodtbReportLayout.setVisibility(View.GONE);
                childhoodtb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(childhoodtb.getCompoundDrawables()[2], color);
                fastReportLayout.setVisibility(View.GONE);
                fast.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(fast.getCompoundDrawables()[2], color);
                petReportLayout.setVisibility(View.GONE);
                pet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pet.getCompoundDrawables()[2], color);
                ccReportLayout.setVisibility(View.GONE);
                cc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(cc.getCompoundDrawables()[2], color);
                pmdtReportLayout.setVisibility(View.GONE);
                pmdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pmdt.getCompoundDrawables()[2], color);
                commonReportLayout.setVisibility(View.GONE);
                common.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(common.getCompoundDrawables()[2], color1);
            }
        }  else if (v == pet) {
            if (petReportLayout.getVisibility() == View.VISIBLE) {
                petReportLayout.setVisibility(View.GONE);
                pet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pet.getCompoundDrawables()[2], color);
            }
            else {
                petReportLayout.setVisibility(View.VISIBLE);
                pet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(pet.getCompoundDrawables()[2], color1);
                childhoodtbReportLayout.setVisibility(View.GONE);
                childhoodtb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(childhoodtb.getCompoundDrawables()[2], color);
                commorbiditiesReportLayout.setVisibility(View.GONE);
                commorbidities.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(commorbidities.getCompoundDrawables()[2], color);
                fastReportLayout.setVisibility(View.GONE);
                fast.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(fast.getCompoundDrawables()[2], color);
                pmdtReportLayout.setVisibility(View.GONE);
                pmdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pmdt.getCompoundDrawables()[2], color);
                ccReportLayout.setVisibility(View.GONE);
                cc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(cc.getCompoundDrawables()[2], color);
                commonReportLayout.setVisibility(View.GONE);
                common.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(common.getCompoundDrawables()[2], color1);
            }
        } else if(v == pmdt){
            if (pmdtReportLayout.getVisibility() == View.VISIBLE) {
                pmdtReportLayout.setVisibility(View.GONE);
                pmdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pmdt.getCompoundDrawables()[2], color);
            }
            else {
                pmdtReportLayout.setVisibility(View.VISIBLE);
                pmdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(pmdt.getCompoundDrawables()[2], color1);
                childhoodtbReportLayout.setVisibility(View.GONE);
                childhoodtb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(childhoodtb.getCompoundDrawables()[2], color);
                commorbiditiesReportLayout.setVisibility(View.GONE);
                commorbidities.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(commorbidities.getCompoundDrawables()[2], color);
                fastReportLayout.setVisibility(View.GONE);
                fast.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(fast.getCompoundDrawables()[2], color);
                petReportLayout.setVisibility(View.GONE);
                pet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pet.getCompoundDrawables()[2], color);
                ccReportLayout.setVisibility(View.GONE);
                cc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(cc.getCompoundDrawables()[2], color);
                commonReportLayout.setVisibility(View.GONE);
                common.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(common.getCompoundDrawables()[2], color1);
            }
        }  else if(v == cc){
            if (ccReportLayout.getVisibility() == View.VISIBLE) {
                ccReportLayout.setVisibility(View.GONE);
                cc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(cc.getCompoundDrawables()[2], color);
            }
            else {
                ccReportLayout.setVisibility(View.VISIBLE);
                cc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(cc.getCompoundDrawables()[2], color1);
                childhoodtbReportLayout.setVisibility(View.GONE);
                childhoodtb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(childhoodtb.getCompoundDrawables()[2], color);
                commorbiditiesReportLayout.setVisibility(View.GONE);
                commorbidities.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(commorbidities.getCompoundDrawables()[2], color);
                fastReportLayout.setVisibility(View.GONE);
                fast.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(fast.getCompoundDrawables()[2], color);
                petReportLayout.setVisibility(View.GONE);
                pet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pet.getCompoundDrawables()[2], color);
                pmdtReportLayout.setVisibility(View.GONE);
                pmdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pmdt.getCompoundDrawables()[2], color);
                commonReportLayout.setVisibility(View.GONE);
                common.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(common.getCompoundDrawables()[2], color1);
            }
        } else if(v == common){
            if (commonReportLayout.getVisibility() == View.VISIBLE) {
                commonReportLayout.setVisibility(View.GONE);
                common.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(common.getCompoundDrawables()[2], color);
            }
            else {
                commonReportLayout.setVisibility(View.VISIBLE);
                common.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(common.getCompoundDrawables()[2], color1);
                ccReportLayout.setVisibility(View.GONE);
                cc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(cc.getCompoundDrawables()[2], color1);
                childhoodtbReportLayout.setVisibility(View.GONE);
                childhoodtb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(childhoodtb.getCompoundDrawables()[2], color);
                commorbiditiesReportLayout.setVisibility(View.GONE);
                commorbidities.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(commorbidities.getCompoundDrawables()[2], color);
                fastReportLayout.setVisibility(View.GONE);
                fast.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(fast.getCompoundDrawables()[2], color);
                petReportLayout.setVisibility(View.GONE);
                pet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pet.getCompoundDrawables()[2], color);
                pmdtReportLayout.setVisibility(View.GONE);
                pmdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                DrawableCompat.setTint(pmdt.getCompoundDrawables()[2], color);
            }
        }


    }
}
