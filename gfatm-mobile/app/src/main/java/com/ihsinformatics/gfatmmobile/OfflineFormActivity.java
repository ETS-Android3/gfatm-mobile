package com.ihsinformatics.gfatmmobile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.model.Patient;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * A login screen that offers login via email/password.
 */
public class OfflineFormActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    protected static ProgressDialog loading;

    protected ImageView submitIcon;
    protected ImageView emailIcon;
    protected ImageView deleteIcon;

    protected LinearLayout contentLinearLayout;
    ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    ServerService serverService;
    Button btnLoadMore;

    int color;
    int color1;

    int chunkSize = 20;
    int errorNumber = 0;
    int successNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_form);

        serverService = new ServerService(getApplicationContext());
        loading = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        submitIcon = (ImageView) findViewById(R.id.submitIcon);
        emailIcon = (ImageView) findViewById(R.id.emailIcon);
        deleteIcon = (ImageView) findViewById(R.id.deleteIcon);

        color = App.getColor(this, R.attr.colorAccent);
        color1 = App.getColor(this, R.attr.colorAccent);

        DrawableCompat.setTint(submitIcon.getDrawable(), color);
        DrawableCompat.setTint(emailIcon.getDrawable(), color);
        DrawableCompat.setTint(deleteIcon.getDrawable(), color);

        submitIcon.setOnTouchListener(this);
        emailIcon.setOnTouchListener(this);
        deleteIcon.setOnTouchListener(this);

        contentLinearLayout = (LinearLayout) findViewById(R.id.content);
        btnLoadMore = (Button) findViewById(R.id.loadMoreButton);

        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            submitIcon.setVisibility(View.GONE);
            emailIcon.setVisibility(View.GONE);
            deleteIcon.setVisibility(View.GONE);
        } else {
            submitIcon.setVisibility(View.VISIBLE);
            emailIcon.setVisibility(View.VISIBLE);
            deleteIcon.setVisibility(View.VISIBLE);
        }

        fillOfflineFormList();

        btnLoadMore.setOnClickListener(this);

    }



    public void loadMore(){

        Object[][] forms = serverService.getSavedFormsByLimits(App.getUsername(),contentLinearLayout.getChildCount(),chunkSize);
        for (int i = 0; i < forms.length; i++) {
            HashMap<String, Object[]> map = new HashMap<String, Object[]>();

            LinearLayout verticalLayout = new LinearLayout(this);
            verticalLayout.setOrientation(LinearLayout.VERTICAL);
            verticalLayout.setPadding(10, 20, 10, 20);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            linearLayout.setDividerDrawable(this.getDrawable(R.drawable.divider));

            final LinearLayout moreLayout = new LinearLayout(this);
            moreLayout.setOrientation(LinearLayout.VERTICAL);

            CheckBox selection = new CheckBox(this);
            selection.setTag(String.valueOf(forms[i][0]));
            checkBoxes.add(selection);
            linearLayout.addView(selection);

            final TextView text = new TextView(this);
            text.setText(String.valueOf(forms[i][2]));
            final Object obj = forms[i][6];
            final String id = String.valueOf(forms[i][0]);
            final String pid = String.valueOf(forms[i][3]);
            if(obj != null) {
                text.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        if(!(pid == null || pid.equals("") || pid.equals("null"))){

                            Patient patient = serverService.getPatientBySystemIdFromLocalDB(pid);

                            if (patient.getUuid() == null || patient.getUuid().equals("")) {

                                final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                                alertDialog.setMessage(getResources().getString(R.string.offline_patient));
                                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                                alertDialog.setIcon(clearIcon);
                                alertDialog.setTitle(getResources().getString(R.string.title_error));
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();

                                return true;
                            }
                        }

                        Intent i = new Intent();
                        i.putExtra("form_id", id);
                        i.putExtra("open", true);
                        i.putExtra("form_object", (byte[]) obj);
                        setResult(RESULT_OK, i);
                        onBackPressed();
                        return true;
                    }

                });
            }

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (moreLayout.getVisibility() == View.VISIBLE) {
                        moreLayout.setVisibility(View.GONE);
                        DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                        text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                    } else {
                        moreLayout.setVisibility(View.VISIBLE);
                        DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                        text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                    }
                }
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            text.setLayoutParams(params);
            text.setTextSize(getResources().getDimension(R.dimen.small));
            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
            text.setPadding(10, 0, 0, 0);
            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
            linearLayout.addView(text);

            if (!(forms[i][2].equals("CREATE PATIENT") || forms[i][2].equals(""))) {

                verticalLayout.addView(linearLayout);

                if (!(forms[i][3] == null || forms[i][3].equals("") || forms[i][3].equals("null"))) {

                    LinearLayout ll1 = new LinearLayout(this);
                    ll1.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv = new TextView(this);
                    tv.setText(getResources().getString(R.string.patient_id) + " ");
                    tv.setTextSize(getResources().getDimension(R.dimen.small));
                    tv.setTextColor(color1);
                    ll1.addView(tv);

                    String identifier = serverService.getPatientIdentifierBySystemIdLocalDB(String.valueOf(forms[i][3]));
                    TextView tv1 = new TextView(this);
                    tv1.setText(identifier);
                    tv1.setTextSize(getResources().getDimension(R.dimen.small));
                    ll1.addView(tv1);

                    moreLayout.addView(ll1);
                }

                LinearLayout ll2 = new LinearLayout(this);
                ll2.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv2 = new TextView(this);
                tv2.setText(getResources().getString(R.string.form_date) + " ");
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                tv2.setTextColor(color1);
                ll2.addView(tv2);

                TextView tv3 = new TextView(this);
                tv3.setText(String.valueOf(forms[i][4]));
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                ll2.addView(tv3);

                moreLayout.addView(ll2);

                LinearLayout ll3 = new LinearLayout(this);
                ll3.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv4 = new TextView(this);
                tv4.setText(getResources().getString(R.string.location) + " ");
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                tv4.setTextColor(color1);
                ll3.addView(tv4);

                TextView tv5 = new TextView(this);
                tv5.setText(String.valueOf(forms[i][7]));
                tv5.setTextSize(getResources().getDimension(R.dimen.small));
                ll3.addView(tv5);

                moreLayout.addView(ll3);

            } else {

                verticalLayout.addView(linearLayout);

                OfflineForm offlineForm = serverService.getOfflineFormById(Integer.parseInt(String.valueOf(forms[i][0])));
                ArrayList<String[][]> array = offlineForm.getObsValue();

                for (int k = 0; k < array.size(); k++) {
                    String[][] obs = array.get(k);

                    LinearLayout ll1 = new LinearLayout(this);
                    ll1.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv = new TextView(this);
                    tv.setText(App.convertToTitleCase(obs[0][0] + ": "));
                    tv.setTextSize(getResources().getDimension(R.dimen.small));
                    tv.setTextColor(color1);
                    ll1.addView(tv);

                    TextView tv1 = new TextView(this);
                    tv1.setText(obs[0][1]);
                    tv1.setTextSize(getResources().getDimension(R.dimen.small));
                    ll1.addView(tv1);

                    moreLayout.addView(ll1);

                }

            }

            moreLayout.setPadding(80, 0, 0, 0);
            moreLayout.setVisibility(View.GONE);
            verticalLayout.addView(moreLayout);

            contentLinearLayout.addView(verticalLayout);

            map.put("verticalayout", new Object[]{verticalLayout, forms[i][0]});

        }

        if(serverService.getPendingSavedFormsCount(App.getUsername()) == contentLinearLayout.getChildCount())
            btnLoadMore.setVisibility(View.GONE);
        else
            btnLoadMore.setVisibility(View.VISIBLE);

    }

    public void fillOfflineFormList(){

        contentLinearLayout.removeAllViews();

        Object[][] forms = serverService.getSavedFormsByLimits(App.getUsername(),0,chunkSize);

        for (int i = 0; i < forms.length; i++) {
            HashMap<String, Object[]> map = new HashMap<String, Object[]>();

            LinearLayout verticalLayout = new LinearLayout(this);
            verticalLayout.setOrientation(LinearLayout.VERTICAL);
            verticalLayout.setPadding(10, 20, 10, 20);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            linearLayout.setDividerDrawable(this.getDrawable(R.drawable.divider));

            final LinearLayout moreLayout = new LinearLayout(this);
            moreLayout.setOrientation(LinearLayout.VERTICAL);

            CheckBox selection = new CheckBox(this);
            selection.setTag(String.valueOf(forms[i][0]));
            checkBoxes.add(selection);
            linearLayout.addView(selection);

            final TextView text = new TextView(this);
            text.setText(String.valueOf(forms[i][2]));
            final Object obj = forms[i][6];
            final String id = String.valueOf(forms[i][0]);
            final String pid = String.valueOf(forms[i][3]);
            if(obj != null) {
                text.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        if(!(pid == null || pid.equals("") || pid.equals("null"))){

                            Patient patient = serverService.getPatientBySystemIdFromLocalDB(pid);

                            if (patient.getUuid() == null || patient.getUuid().equals("")) {

                                final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                                alertDialog.setMessage(getResources().getString(R.string.offline_patient));
                                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                                alertDialog.setIcon(clearIcon);
                                alertDialog.setTitle(getResources().getString(R.string.title_error));
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();

                                return true;
                            }
                        }


                        Intent i = new Intent();
                        i.putExtra("form_id", id);
                        i.putExtra("open", true);
                        i.putExtra("form_object", (byte[]) obj);
                        setResult(RESULT_OK, i);
                        onBackPressed();
                        return true;

                    }

                });
            }

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (moreLayout.getVisibility() == View.VISIBLE) {
                        moreLayout.setVisibility(View.GONE);
                        DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                        text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                    } else {
                        moreLayout.setVisibility(View.VISIBLE);
                        DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                        text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                    }
                }
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            text.setLayoutParams(params);
            text.setTextSize(getResources().getDimension(R.dimen.small));
            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
            text.setPadding(10, 0, 0, 0);
            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
            linearLayout.addView(text);

            if (!(forms[i][2].equals("CREATE PATIENT") || forms[i][2].equals(""))) {

                verticalLayout.addView(linearLayout);

                if (!(forms[i][3] == null || forms[i][3].equals("") || forms[i][3].equals("null"))) {

                    LinearLayout ll1 = new LinearLayout(this);
                    ll1.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv = new TextView(this);
                    tv.setText(getResources().getString(R.string.patient_id) + " ");
                    tv.setTextSize(getResources().getDimension(R.dimen.small));
                    tv.setTextColor(color1);
                    ll1.addView(tv);

                    String identifier = serverService.getPatientIdentifierBySystemIdLocalDB(String.valueOf(forms[i][3]));
                    TextView tv1 = new TextView(this);
                    tv1.setText(identifier);
                    tv1.setTextSize(getResources().getDimension(R.dimen.small));
                    ll1.addView(tv1);

                    moreLayout.addView(ll1);
                }

                LinearLayout ll2 = new LinearLayout(this);
                ll2.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv2 = new TextView(this);
                tv2.setText(getResources().getString(R.string.form_date) + " ");
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                tv2.setTextColor(color1);
                ll2.addView(tv2);

                TextView tv3 = new TextView(this);
                tv3.setText(String.valueOf(forms[i][4]));
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                ll2.addView(tv3);

                moreLayout.addView(ll2);

                LinearLayout ll3 = new LinearLayout(this);
                ll3.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv4 = new TextView(this);
                tv4.setText(getResources().getString(R.string.location) + " ");
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                tv4.setTextColor(color1);
                ll3.addView(tv4);

                TextView tv5 = new TextView(this);
                tv5.setText(String.valueOf(forms[i][7]));
                tv5.setTextSize(getResources().getDimension(R.dimen.small));
                ll3.addView(tv5);

                moreLayout.addView(ll3);

            } else {

                verticalLayout.addView(linearLayout);

                OfflineForm offlineForm = serverService.getOfflineFormById(Integer.parseInt(String.valueOf(forms[i][0])));
                ArrayList<String[][]> array = offlineForm.getObsValue();

                for (int k = 0; k < array.size(); k++) {
                    String[][] obs = array.get(k);

                    LinearLayout ll1 = new LinearLayout(this);
                    ll1.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv = new TextView(this);
                    tv.setText(App.convertToTitleCase(obs[0][0] + ": "));
                    tv.setTextSize(getResources().getDimension(R.dimen.small));
                    tv.setTextColor(color1);
                    ll1.addView(tv);

                    TextView tv1 = new TextView(this);
                    tv1.setText(obs[0][1]);
                    tv1.setTextSize(getResources().getDimension(R.dimen.small));
                    ll1.addView(tv1);

                    moreLayout.addView(ll1);

                }

            }

            moreLayout.setPadding(80, 0, 0, 0);
            moreLayout.setVisibility(View.GONE);
            verticalLayout.addView(moreLayout);

            map.put("verticalayout", new Object[]{verticalLayout, forms[i][0]});
            contentLinearLayout.addView(verticalLayout);

        }

        if(serverService.getPendingSavedFormsCount(App.getUsername()) == contentLinearLayout.getChildCount())
            btnLoadMore.setVisibility(View.GONE);
        else
            btnLoadMore.setVisibility(View.VISIBLE);


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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(getResources().getColor(R.color.dark_grey), PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                Boolean selected = false;
                for (CheckBox cb : checkBoxes) {
                    if (cb.isChecked())
                       selected = true;
                }

                if(!selected){

                    final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.error_no_selection));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else {
                    if (v == deleteIcon) {

                        int color = App.getColor(OfflineFormActivity.this, R.attr.colorAccent);

                        final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                        alertDialog.setMessage(getString(R.string.warning_before_delete));
                        Drawable clearIcon = getResources().getDrawable(R.drawable.ic_delete);
                        DrawableCompat.setTint(clearIcon, color);
                        alertDialog.setIcon(clearIcon);
                        alertDialog.setTitle(getResources().getString(R.string.title_delete));
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

                    } else if (v == submitIcon) {

                        int color = App.getColor(OfflineFormActivity.this, R.attr.colorAccent);

                        final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                        alertDialog.setMessage(getString(R.string.warning_before_sync));
                        Drawable clearIcon = getResources().getDrawable(R.drawable.ic_submit);
                        DrawableCompat.setTint(clearIcon, color);
                        alertDialog.setIcon(clearIcon);
                        alertDialog.setTitle(getResources().getString(R.string.title_sync));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        submitForms();
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

                    } else if (v == emailIcon) {

                        int color = App.getColor(OfflineFormActivity.this, R.attr.colorAccent);

                        final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                        alertDialog.setMessage(getString(R.string.warning_before_email));
                        Drawable clearIcon = getResources().getDrawable(R.drawable.ic_submit);
                        DrawableCompat.setTint(clearIcon, color);
                        alertDialog.setIcon(clearIcon);
                        alertDialog.setTitle(getResources().getString(R.string.title_email));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        emailForms();
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

        fillOfflineFormList();
    }

    public void submitForms() {

        final ArrayList<String> checkedTag = new ArrayList<>();
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                checkedTag.add(cb.getTag().toString());
            }
        }


        AsyncTask<String, String, String> submissionTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.submitting_form));
                        loading.show();
                    }
                });

                final Boolean[] errorFlag = {false};
                errorNumber = 0;
                successNumber = 0;
                for (int i = 0; i < checkedTag.size(); i++) {
                    String returnString = serverService.submitOfflineForm(checkedTag.get(i), true);
                    if (!returnString.equals("SUCCESS")) {
                        final String[] retStr = {""};

                        if(returnString.contains("PATIENT ALREADY EXISTS")) {
                            returnString = returnString + checkedTag.get(i);
                            final String result = returnString.replace("PATIENT ALREADY EXISTS ; ", "");

                            final Semaphore dialogSemaphore = new Semaphore(0, true);
                            Runnable uiRunnable = new Runnable() {
                                public void run() {

                                    final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                                    final String[] resultArray = result.split(" ; ");
                                    String message = getResources().getString(R.string.patient_id) + resultArray[0] + " " + getResources().getString(R.string.patient_already_exists_error) + "<br><br>";
                                    message = message + getResources().getString(R.string.patient_id) + " <b>" + resultArray[0] + "</b><br>";
                                    message = message + getResources().getString(R.string.name) + " <b>" + resultArray[1] + "</b><br>";
                                    String gender = resultArray[2];
                                    if (gender.equalsIgnoreCase("M")) gender = "Male";
                                    else if (gender.equalsIgnoreCase("F")) gender = "Female";
                                    message = message + getResources().getString(R.string.gender) + " <b>" + gender + "</b><br>";
                                    if (resultArray[3].equals("0")) {
                                        Date birthDate = App.stringToDate(resultArray[4].substring(0, 10), "yyyy-MM-dd");
                                        int age = App.getDiffMonths(birthDate, new Date());
                                        message = message + getResources().getString(R.string.age) + " <b>" + age + " month(s)</b><br>";
                                    } else
                                        message = message + getResources().getString(R.string.age) + " <b>" + resultArray[3] + " year(s)</b><br>";
                                    message = message + getResources().getString(R.string.dob) + " <b>" + resultArray[4].substring(0, 10) + "</b><br><br>";
                                    message = message + getResources().getString(R.string.merge_patient);
                                    alertDialog.setMessage(Html.fromHtml(message));
                                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                                    alertDialog.setIcon(clearIcon);
                                    alertDialog.setTitle(getResources().getString(R.string.patient_already_exists));
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.merge),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                    retStr[0] = serverService.mergePatient(String.valueOf(resultArray[0]), String.valueOf(resultArray[5]));
                                                    dialogSemaphore.release();

                                                }
                                            });
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialogSemaphore.release();
                                                    errorFlag[0] = true;
                                                    errorNumber = errorNumber + 1;
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

                                }
                            };

                            runOnUiThread(uiRunnable);
                            try {
                                dialogSemaphore.acquire();
                            }
                            catch (InterruptedException e ) {
                                e.printStackTrace();
                            }

                        }
                        else if(returnString.equals("CONNECTION_ERROR"))
                            return returnString;

                        else if(!returnString.equals("SUCCESS")) {

                            if(!(returnString.contains("PATIENT ALREADY EXISTS") && retStr[0].equals("SUCCESS"))) {
                                errorFlag[0] = true;
                                errorNumber = errorNumber + 1;
                            } else
                                successNumber = successNumber + 1;

                        }
                    }

                    else
                        successNumber = successNumber + 1;

                }

                if(errorFlag[0])
                    return "COMPLETE_WITH_ERROR";

                return "SUCCESS";

            }

            @Override
            protected void onProgressUpdate(String... values) {
            };

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                loading.dismiss();
                if (result.equals("SUCCESS")) {

                    Toast toast = Toast.makeText(OfflineFormActivity.this, getResources().getString(R.string.forms_submitted), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();

                } else if (result.equals("COMPLETE_WITH_ERROR")) {

                    String msg = getResources().getString(R.string.forms_submitted_with_error) + "\n" +
                            successNumber + " forms successfully submitted. \n" +
                            errorNumber + " forms failed due to error while submitting to openmrs.";

                    final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                    alertDialog.setMessage(msg);
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                } else if (result.equals("CONNECTION_ERROR")) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.data_connection_error) + "\n\n (" + result + ")");
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                } else {

                    final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                    String message = getResources().getString(R.string.insert_error) + "\n\n (" + result + ")";
                    alertDialog.setMessage(message);
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }

                fillOfflineFormList();

            }
        };
        submissionTask.execute("");

    }

    public void emailForms(){

        final ArrayList<String> checkedTag = new ArrayList<>();
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                checkedTag.add(cb.getTag().toString());
            }
        }


        AsyncTask<String, String, String> submissionTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.submitting_form));
                        loading.show();
                    }
                });

                StringBuilder formsData = new StringBuilder ();

                for (int i = 0; i < checkedTag.size(); i++) {
                    formsData.append(serverService.emailOfflineForm(checkedTag.get(i)));
                }

                return formsData.toString();

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                String[] emailAddreses = {App.getSupportEmail ()};
                Intent emailIntent = new Intent (Intent.ACTION_SEND);
                emailIntent.putExtra (Intent.EXTRA_EMAIL, emailAddreses);
                StringBuilder subject = new StringBuilder ();
                subject.append (getResources ().getString (R.string.app_name));
                subject.append (" : ");
                subject.append (App.getUsername ());
                emailIntent.putExtra (Intent.EXTRA_SUBJECT, subject.toString ());
                emailIntent.setType ("plain/text");
                emailIntent.putExtra (Intent.EXTRA_TEXT, result.toString ());

                startActivity (emailIntent);

            }
        };
        submissionTask.execute("");

    }


    @Override
    public void onClick(View v) {

        if(v == btnLoadMore)
            loadMore();

    }
}


