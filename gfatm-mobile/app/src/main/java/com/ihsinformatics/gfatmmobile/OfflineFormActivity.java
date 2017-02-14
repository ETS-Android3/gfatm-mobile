package com.ihsinformatics.gfatmmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;

/**
 * A login screen that offers login via email/password.
 */
public class OfflineFormActivity extends AppCompatActivity implements View.OnTouchListener {

    protected ImageView submitIcon;
    protected ImageView emailIcon;
    protected ImageView deleteIcon;

    protected TextView programName;
    protected LinearLayout contentLinearLayout;

    ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    ServerService serverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_form);

        serverService = new ServerService(getApplicationContext());

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

        programName = (TextView) findViewById(R.id.program);
        contentLinearLayout = (LinearLayout) findViewById(R.id.content);

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

        checkBoxes.clear();
        contentLinearLayout.removeAllViews();

        final Object[][] forms = serverService.getOfflineForms(App.getProgram());

        programName.setText(App.getProgram());

        if (forms == null || forms.length == 0) {
            final TextView text = new TextView(this);
            text.setText(getResources().getString(R.string.no_saved_form));
            text.setTextSize(getResources().getDimension(R.dimen.small));
            contentLinearLayout.addView(text);
        } else {

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
                final int color1 = App.getColor(this, R.attr.colorAccent);

                CheckBox selection = new CheckBox(this);
                linearLayout.addView(selection);
                selection.setTag(String.valueOf(forms[i][0]));
                checkBoxes.add(selection);

                final TextView text = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(forms[i][1]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                text.setPadding(10, 0, 0, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
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
                        i.putExtra("encounter_id", id);
                        i.putExtra("open", true);
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
                tv.setText(getResources().getString(R.string.patient_id) + " ");
                tv.setTextSize(getResources().getDimension(R.dimen.small));
                tv.setTextColor(color1);
                ll1.addView(tv);

                TextView tv1 = new TextView(this);
                tv1.setText(String.valueOf(forms[i][4]));
                tv1.setTextSize(getResources().getDimension(R.dimen.small));
                ll1.addView(tv1);

                moreLayout.addView(ll1);

                LinearLayout ll2 = new LinearLayout(this);
                ll2.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv2 = new TextView(this);
                tv2.setText(getResources().getString(R.string.form_date) + " ");
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                tv2.setTextColor(color1);
                ll2.addView(tv2);

                TextView tv3 = new TextView(this);
                tv3.setText(String.valueOf(forms[i][2]));
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                ll2.addView(tv3);

                moreLayout.addView(ll2);

                LinearLayout ll3 = new LinearLayout(this);
                ll3.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv4 = new TextView(this);
                tv4.setText(getResources().getString(R.string.time_stamp) + " ");
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                tv4.setTextColor(color1);
                ll3.addView(tv4);

                TextView tv5 = new TextView(this);
                tv5.setText(String.valueOf(forms[i][5]));
                tv5.setTextSize(getResources().getDimension(R.dimen.small));
                ll3.addView(tv5);

                moreLayout.addView(ll3);

                moreLayout.setPadding(80, 0, 0, 0);
                moreLayout.setVisibility(View.GONE);
                verticalLayout.addView(moreLayout);

                contentLinearLayout.addView(verticalLayout);


            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(getResources().getColor(R.color.dark_grey), PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                if (v == deleteIcon) {

                    int color = App.getColor(OfflineFormActivity.this, R.attr.colorAccent);

                    final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.warning_before_submit));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.ic_submit);
                    DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_submit));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteForms();
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

    public void deleteForms() {
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                serverService.deleteForms(cb.getTag().toString());
            }
        }

        fillList();
    }
}

