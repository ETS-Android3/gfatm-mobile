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


public class ReportFragment extends Fragment implements View.OnTouchListener {

    Context context;

    LinearLayout reportLayout;
    TextView programName;
    ImageView refersh;

    ServerService serverService;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View mainContent = inflater.inflate(
                R.layout.report_fragment, container, false);

        context = mainContent.getContext();

        serverService = new ServerService(context.getApplicationContext());

        reportLayout = (LinearLayout) mainContent.findViewById(R.id.reportFragment);
        programName = (TextView) mainContent.findViewById(R.id.program);
        refersh = (ImageView) mainContent.findViewById(R.id.refresh);

        refersh.setOnTouchListener(this);

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

        reportLayout.removeAllViews();

        Object[][] encounters = serverService.getAllEncounterFromLocalDB(App.getProgram());
        programName.setText(App.getProgram());

        if (encounters == null || encounters.length == 0) {
            final TextView text = new TextView(context);
            text.setText(getResources().getString(R.string.no_location));
            text.setTextSize(getResources().getDimension(R.dimen.small));
            reportLayout.addView(text);
        } else {
            for (int i = 0; i < encounters.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(context);
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final LinearLayout moreLayout = new LinearLayout(context);
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                final int color = App.getColor(context, R.attr.colorPrimaryDark);
                final int color1 = App.getColor(context, R.attr.colorAccent);

                final TextView text = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(encounters[i][0]));
                text.setTextSize(getResources().getDimension(R.dimen.medium));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                text.setPadding(10, 0, 0, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                text.setTag(String.valueOf(encounters[i][1]));
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                verticalLayout.addView(moreLayout);

                LinearLayout ll1 = new LinearLayout(context);
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv1 = new TextView(context);
                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                tv1.setTextColor(color1);
                tv1.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                tv1.setLayoutParams(p);
                ll1.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(App.convertToTitleCase(String.valueOf(encounters[i][3])));
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                tv2.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p1.weight = 1;
                tv2.setLayoutParams(p1);
                ll1.addView(tv2);

                moreLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(context);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setPadding(0, 0, 0, 10);

                TextView tv3 = new TextView(context);
                tv3.setText(getResources().getString(R.string.location) + "  ");
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                tv3.setTextColor(color1);
                tv3.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p2.weight = 1;
                tv3.setLayoutParams(p2);
                ll2.addView(tv3);

                TextView tv4 = new TextView(context);
                tv4.setText(String.valueOf(encounters[i][4]));
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                tv4.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p3.weight = 1;
                tv4.setLayoutParams(p3);
                ll2.addView(tv4);

                moreLayout.addView(ll2);

                moreLayout.setVisibility(View.GONE);

                reportLayout.addView(verticalLayout);

                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                        } else {

                            Object[][] obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            if (obs.length == 0) {
                                moreLayout.removeAllViews();
                                Object[][] encounterObject = serverService.getEncounterIdByEncounterType(text.getText().toString());
                                text.setTag(String.valueOf(encounterObject[0][1]));

                                LinearLayout ll1 = new LinearLayout(context);
                                ll1.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv1 = new TextView(context);
                                tv1.setText(getResources().getString(R.string.form_date) + "  ");
                                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                                tv1.setTextColor(color1);
                                tv1.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv1.setLayoutParams(p);
                                ll1.addView(tv1);

                                TextView tv2 = new TextView(context);
                                tv2.setText(App.convertToTitleCase(String.valueOf(encounterObject[0][3])));
                                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                                tv2.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv2.setLayoutParams(p1);
                                ll1.addView(tv2);

                                moreLayout.addView(ll1);

                                LinearLayout ll2 = new LinearLayout(context);
                                ll2.setOrientation(LinearLayout.HORIZONTAL);
                                ll2.setPadding(0, 0, 0, 10);

                                TextView tv3 = new TextView(context);
                                tv3.setText(getResources().getString(R.string.location) + "  ");
                                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                                tv3.setTextColor(color1);
                                tv3.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p2.weight = 1;
                                tv3.setLayoutParams(p2);
                                ll2.addView(tv3);

                                TextView tv4 = new TextView(context);
                                tv4.setText(String.valueOf(encounterObject[0][4]));
                                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                                tv4.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p3.weight = 1;
                                tv4.setLayoutParams(p3);
                                ll2.addView(tv4);

                                moreLayout.addView(ll2);

                                obs = serverService.getAllObsFromEncounterId(Integer.parseInt(String.valueOf(text.getTag())));
                            }

                            for (int j = 0; j < obs.length; j++) {
                                LinearLayout ll5 = new LinearLayout(context);
                                ll5.setOrientation(LinearLayout.HORIZONTAL);

                                TextView tv8 = new TextView(context);
                                tv8.setText(App.convertToTitleCase(String.valueOf(obs[j][1])) + ":  ");
                                tv8.setTextSize(getResources().getDimension(R.dimen.small));
                                tv8.setTextColor(color1);
                                tv8.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p.weight = 1;
                                tv8.setLayoutParams(p);
                                ll5.addView(tv8);

                                TextView tv9 = new TextView(context);
                                tv9.setText(String.valueOf(App.convertToTitleCase(String.valueOf(obs[j][0]))));
                                tv9.setTextSize(getResources().getDimension(R.dimen.small));
                                tv9.setTypeface(null, Typeface.BOLD);
                                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                p1.weight = 1;
                                tv9.setLayoutParams(p1);
                                ll5.addView(tv9);

                                moreLayout.addView(ll5);
                            }


                            for (int k = 0; k < reportLayout.getChildCount(); k++) {

                                View view = reportLayout.getChildAt(k);

                                LinearLayout mL1 = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                                TextView t = (TextView) mL1.getChildAt(0);
                                LinearLayout mL = (LinearLayout) ((LinearLayout) view).getChildAt(1);

                                mL.setVisibility(View.GONE);
                                DrawableCompat.setTint(t.getCompoundDrawables()[2], color);
                                t.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);

                            }

                            moreLayout.setVisibility(View.VISIBLE);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                        }
                    }
                });
            }
        }

    }
}
