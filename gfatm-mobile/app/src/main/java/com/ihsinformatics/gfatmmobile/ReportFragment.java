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

import com.ihsinformatics.gfatmmobile.shared.Metadata;
import com.ihsinformatics.gfatmmobile.util.ServerService;


public class ReportFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

    Context context;

    ServerService serverService;

    ImageView refershEncounters;
    TextView noEncounters;
    LinearLayout encountersLayout;

    ImageView refershResults;
    TextView noResults;
    LinearLayout resultsLayout;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View mainContent = inflater.inflate(
                R.layout.report_fragment, container, false);

        context = mainContent.getContext();

        serverService = new ServerService(context.getApplicationContext());

        int color = App.getColor(context, R.attr.colorPrimaryDark);
        int color1 = App.getColor(context, R.attr.colorAccent);

        refershEncounters = (ImageView) mainContent.findViewById(R.id.refresh);
        refershEncounters.setOnTouchListener(this);
        noEncounters = (TextView) mainContent.findViewById(R.id.common);
        encountersLayout = (LinearLayout) mainContent.findViewById(R.id.commonReportFragment);

        refershResults = (ImageView) mainContent.findViewById(R.id.refreshTest);
        refershResults.setOnTouchListener(this);
        noResults = (TextView) mainContent.findViewById(R.id.commonTest);
        resultsLayout = (LinearLayout) mainContent.findViewById(R.id.commonTestFragment);

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

        //String program = App.getProgram();
        final int color = App.getColor(context, R.attr.colorPrimaryDark);
        final int color2 = App.getColor(context, R.attr.colorPrimary);

        resultsLayout.setVisibility(View.GONE);
        resultsLayout.removeAllViews();

        final Object[][] commonLabResults = serverService.getPatientLabTestFromLocalDB();
        if (commonLabResults == null || commonLabResults.length == 0) {
            noResults.setVisibility(View.VISIBLE);
            resultsLayout.setVisibility(View.GONE);
        } else {

            for (int i = 0; i < commonLabResults.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(context);
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                final LinearLayout moreLayout = new LinearLayout(context);
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final TextView text = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(commonLabResults[i][0]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                text.setPadding(10, 0, 0, 0);
                text.setTag(String.valueOf(commonLabResults[i][1]));
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                verticalLayout.addView(moreLayout);

                resultsLayout.addView(verticalLayout);

                LinearLayout ll = new LinearLayout(context);
                ll.setOrientation(LinearLayout.HORIZONTAL);

                TextView tvText = new TextView(context);
                tvText.setText("Lab Reference Number" + ":  ");
                tvText.setTextSize(getResources().getDimension(R.dimen.small));
                tvText.setTextColor(color);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                tvText.setLayoutParams(p);
                ll.addView(tvText);

                TextView tvLabReferenceNumber = new TextView(context);
                tvLabReferenceNumber.setText(String.valueOf(commonLabResults[i][1]));
                tvLabReferenceNumber.setTextSize(getResources().getDimension(R.dimen.small));
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p1.weight = 1;
                tvLabReferenceNumber.setLayoutParams(p1);
                ll.addView(tvLabReferenceNumber);

                moreLayout.addView(ll);
                moreLayout.setVisibility(View.GONE);

                final Object[][] commonLabAttributes = serverService.getLabAttributesFromLocalDB(String.valueOf(commonLabResults[i][2]));
                for(int j = 0; j < commonLabAttributes.length; j++){

                    LinearLayout ll1 = new LinearLayout(context);
                    ll1.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tvAttr = new TextView(context);
                    tvAttr.setText(String.valueOf(commonLabAttributes[j][0]) + ":  ");
                    tvAttr.setTextSize(getResources().getDimension(R.dimen.small));
                    tvAttr.setTextColor(color);
                    LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    p.weight = 1;
                    tvAttr.setLayoutParams(p);
                    ll1.addView(tvAttr);

                    TextView tvValue = new TextView(context);
                    tvValue.setText(String.valueOf(commonLabAttributes[j][1]));
                    tvValue.setTextSize(getResources().getDimension(R.dimen.small));
                    LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    p1.weight = 1;
                    tvValue.setLayoutParams(p1);
                    ll1.addView(tvValue);

                    if(serverService.getLabTestAttributeType(String.valueOf(commonLabAttributes[j][0])) != null && serverService.getLabTestAttributeType(String.valueOf(commonLabAttributes[j][0])).equals("org.openmrs.customdatatype.datatype.ConceptDatatype")){
                        String name = serverService.getConceptNameForConceptId(String.valueOf(commonLabAttributes[j][1]));
                        if(name != null)
                            tvValue.setText(name);
                    }



                    moreLayout.addView(ll1);

                }

                text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color2);
                            text.setTypeface(text.getTypeface(), Typeface.NORMAL);
                            text.setTextSize(getResources().getDimension(R.dimen.small));
                        } else {

                            for (int k = 0; k < resultsLayout.getChildCount(); k++) {

                                View view = resultsLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);

                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);

                                mL.setVisibility(View.GONE);
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
                                DrawableCompat.setTint(t.getCompoundDrawables()[2], color2);
                                t.setTypeface(t.getTypeface(), Typeface.NORMAL);
                                t.setTextSize(getResources().getDimension(R.dimen.small));

                            }

                            moreLayout.setVisibility(View.VISIBLE);

                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setTypeface(text.getTypeface(), Typeface.BOLD);
                            text.setTextSize(getResources().getDimension(R.dimen.medium));

                            for (int k = 0; k < encountersLayout.getChildCount(); k++) {

                                View view = encountersLayout.getChildAt(k);

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

                        }

                    }
                });


                noResults.setVisibility(View.GONE);
                resultsLayout.setVisibility(View.VISIBLE);
            }
        }

        encountersLayout.setVisibility(View.GONE);
        encountersLayout.removeAllViews();

        Object[][] commonEncounters = serverService.getEncounterFromLocalDB();
        if (commonEncounters == null || commonEncounters.length == 0) {
            noEncounters.setVisibility(View.VISIBLE);
            encountersLayout.setVisibility(View.GONE);
        } else {

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

                encountersLayout.addView(verticalLayout);

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


                            for (int k = 0; k < encountersLayout.getChildCount(); k++) {

                                View view = encountersLayout.getChildAt(k);

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

                            for (int k = 0; k < resultsLayout.getChildCount(); k++) {

                                View view = resultsLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);

                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);

                                mL.setVisibility(View.GONE);
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
            noEncounters.setVisibility(View.GONE);
            encountersLayout.setVisibility(View.VISIBLE);

        }



    }

    @Override
    public void onClick(View v) {



    }
}
