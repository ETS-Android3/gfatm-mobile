package com.ihsinformatics.gfatmmobile;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.util.ServerService;

/**
 * A login screen that offers login via email/password.
 */
public class SavedFormActivity extends AppCompatActivity implements View.OnTouchListener {

    protected ImageView submitIcon;
    protected ImageView emailIcon;
    protected ImageView deleteIcon;

    protected TextView pmdtTextView;
    protected LinearLayout pmdtLinearLayout;
    protected TextView petTextView;
    protected LinearLayout petLinearLayout;
    protected TextView fastTextView;
    protected LinearLayout fastLinearLayout;
    protected TextView comorbiditiesTextView;
    protected LinearLayout comorbiditiesLinearLayout;
    protected TextView childhoodtbTextView;
    protected LinearLayout childhoodtbLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        submitIcon = (ImageView) findViewById(R.id.submitIcon);
        emailIcon = (ImageView) findViewById(R.id.emailIcon);
        deleteIcon = (ImageView) findViewById(R.id.deleteIcon);

        int color = App.getColor(this, R.attr.colorAccent);
        DrawableCompat.setTint(submitIcon.getDrawable(), color);
        DrawableCompat.setTint(emailIcon.getDrawable(), color);
        DrawableCompat.setTint(deleteIcon.getDrawable(), color);

        submitIcon.setOnTouchListener(this);
        emailIcon.setOnTouchListener(this);
        deleteIcon.setOnTouchListener(this);

        pmdtTextView = (TextView) findViewById(R.id.pmdt);
        pmdtLinearLayout = (LinearLayout) findViewById(R.id.pmdtlayout);
        petTextView = (TextView) findViewById(R.id.pet);
        petLinearLayout = (LinearLayout) findViewById(R.id.petlayout);
        fastTextView = (TextView) findViewById(R.id.fast);
        fastLinearLayout = (LinearLayout) findViewById(R.id.fastlayout);
        comorbiditiesTextView = (TextView) findViewById(R.id.comorbidities);
        comorbiditiesLinearLayout = (LinearLayout) findViewById(R.id.comorbiditieslayout);
        childhoodtbTextView = (TextView) findViewById(R.id.childhoodtb);
        childhoodtbLinearLayout = (LinearLayout) findViewById(R.id.childhoodtblayout);

        fillList();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillList() {

        ServerService serverService = new ServerService(getApplicationContext());
        final Object[][] forms = serverService.getSavedForms(App.getUsername());

        pmdtLinearLayout.setVisibility(View.GONE);
        pmdtLinearLayout.removeAllViews();
        petLinearLayout.setVisibility(View.GONE);
        petLinearLayout.removeAllViews();
        fastLinearLayout.setVisibility(View.GONE);
        fastLinearLayout.removeAllViews();
        comorbiditiesLinearLayout.setVisibility(View.GONE);
        comorbiditiesLinearLayout.removeAllViews();
        childhoodtbLinearLayout.setVisibility(View.GONE);
        childhoodtbLinearLayout.removeAllViews();

        for (int i = 0; i < forms.length; i++) {

            LinearLayout verticalLayout = new LinearLayout(getApplicationContext());
            verticalLayout.setOrientation(LinearLayout.VERTICAL);
            verticalLayout.setPadding(10, 20, 10, 20);

            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

            final LinearLayout moreLayout = new LinearLayout(getApplicationContext());
            moreLayout.setOrientation(LinearLayout.VERTICAL);

            final int color = App.getColor(this, R.attr.colorPrimaryDark);

            if (String.valueOf(forms[i][1]).equalsIgnoreCase(getResources().getString(R.string.pmdt))) {

            } else if (String.valueOf(forms[i][1]).equalsIgnoreCase(getResources().getString(R.string.pet))) {

                CheckBox selection = new CheckBox(this);
                linearLayout.addView(selection);

                final TextView text = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(forms[i][2]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_complete_form, 0, R.drawable.ic_more, 0);
                text.setPadding(10, 0, 0, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                DrawableCompat.setTint(text.getCompoundDrawables()[0], color);
                linearLayout.addView(text);

                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            DrawableCompat.setTint(text.getCompoundDrawables()[0], color);
                            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_complete_form, 0, R.drawable.ic_more, 0);
                        } else {
                            moreLayout.setVisibility(View.VISIBLE);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            DrawableCompat.setTint(text.getCompoundDrawables()[0], color);
                            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_complete_form, 0, R.drawable.ic_less, 0);

                        }
                    }
                });
                final Object obj = forms[i][6];
                final String id = String.valueOf(forms[i][0]);
                text.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        Intent i = new Intent();
                        i.putExtra("form_id", id);
                        i.putExtra("form_object", (byte[]) obj);
                        setResult(RESULT_OK, i);
                        onBackPressed();
                        return true;
                    }

                });

                verticalLayout.addView(linearLayout);

                LinearLayout ll1 = new LinearLayout(this);
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv = new TextView(this);
                tv.setText(getResources().getString(R.string.patient_id));
                tv.setTextSize(getResources().getDimension(R.dimen.small));
                tv.setTextColor(color);
                ll1.addView(tv);

                TextView tv1 = new TextView(this);
                tv1.setText(String.valueOf(forms[i][3]));
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                ll1.addView(tv1);

                moreLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(this);
                ll2.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv2 = new TextView(this);
                tv2.setText(getResources().getString(R.string.form_date));
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                tv2.setTextColor(color);
                ll2.addView(tv2);

                TextView tv3 = new TextView(this);
                tv3.setText(String.valueOf(forms[i][4]));
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                ll2.addView(tv3);

                moreLayout.addView(ll2);

                LinearLayout ll3 = new LinearLayout(this);
                ll3.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv4 = new TextView(this);
                tv4.setText(getResources().getString(R.string.time_stamp));
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                tv4.setTextColor(color);
                ll3.addView(tv4);

                TextView tv5 = new TextView(this);
                tv5.setText(String.valueOf(forms[i][5]));
                tv5.setTextSize(getResources().getDimension(R.dimen.small));
                ll3.addView(tv5);

                moreLayout.addView(ll3);

                moreLayout.setPadding(80, 0, 0, 0);
                moreLayout.setVisibility(View.GONE);
                verticalLayout.addView(moreLayout);

                petTextView.setVisibility(View.VISIBLE);
                petLinearLayout.setVisibility(View.VISIBLE);
                petLinearLayout.addView(verticalLayout);

            } else if (String.valueOf(forms[i][1]).equalsIgnoreCase(getResources().getString(R.string.fast))) {

            } else if (String.valueOf(forms[i][1]).equalsIgnoreCase(getResources().getString(R.string.comorbidities))) {

            } else if (String.valueOf(forms[i][1]).equalsIgnoreCase(getResources().getString(R.string.childhood_tb))) {

            }

        }

        if (pmdtLinearLayout.getVisibility() == View.GONE) {

            final TextView text = new TextView(this);
            text.setText("No saved form found.");
            text.setTextSize(getResources().getDimension(R.dimen.tiny));
            pmdtLinearLayout.addView(text);
            pmdtLinearLayout.setVisibility(View.VISIBLE);

        }
        if (petLinearLayout.getVisibility() == View.GONE) {

            final TextView text = new TextView(this);
            text.setText("No saved form found.");
            text.setTextSize(getResources().getDimension(R.dimen.tiny));
            petLinearLayout.addView(text);
            petLinearLayout.setVisibility(View.VISIBLE);
        }
        if (fastLinearLayout.getVisibility() == View.GONE) {

            final TextView text = new TextView(this);
            text.setText("No saved form found.");
            text.setTextSize(getResources().getDimension(R.dimen.tiny));
            fastLinearLayout.addView(text);
            fastLinearLayout.setVisibility(View.VISIBLE);
        }
        if (childhoodtbLinearLayout.getVisibility() == View.GONE) {

            final TextView text = new TextView(this);
            text.setText("No saved form found.");
            text.setTextSize(getResources().getDimension(R.dimen.tiny));
            childhoodtbLinearLayout.addView(text);
            childhoodtbLinearLayout.setVisibility(View.VISIBLE);

        }
        if (comorbiditiesLinearLayout.getVisibility() == View.GONE) {

            final TextView text = new TextView(this);
            text.setText("No saved form found.");
            text.setTextSize(getResources().getDimension(R.dimen.tiny));
            comorbiditiesLinearLayout.addView(text);
            comorbiditiesLinearLayout.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
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
}


