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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.ArrayList;
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class OfflineFormActivity extends AppCompatActivity implements View.OnTouchListener {

    protected static ProgressDialog loading;

    protected ImageView submitIcon;
    protected ImageView emailIcon;
    protected ImageView deleteIcon;

    protected TextView programName;
    protected LinearLayout contentLinearLayout;

    ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    ServerService serverService;

    final ArrayList<String> errorEncounterFormsId = new ArrayList<>();
    final ArrayList<String> errorCreatePatientFormsId = new ArrayList<>();

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

        int color = App.getColor(this, R.attr.colorAccent);
        DrawableCompat.setTint(submitIcon.getDrawable(), color);
        DrawableCompat.setTint(emailIcon.getDrawable(), color);
        DrawableCompat.setTint(deleteIcon.getDrawable(), color);

        submitIcon.setOnTouchListener(this);
        emailIcon.setOnTouchListener(this);
        deleteIcon.setOnTouchListener(this);

        programName = (TextView) findViewById(R.id.program);
        contentLinearLayout = (LinearLayout) findViewById(R.id.content);

        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            submitIcon.setVisibility(View.GONE);
            emailIcon.setVisibility(View.GONE);
            deleteIcon.setVisibility(View.GONE);
        } else {
            submitIcon.setVisibility(View.VISIBLE);
            emailIcon.setVisibility(View.VISIBLE);
            deleteIcon.setVisibility(View.VISIBLE);
        }

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

        final Object[][] forms = serverService.getSavedForms(App.getUsername(), App.getProgram());

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
                text.setText(String.valueOf(forms[i][1]) + " - " + forms[i][2]);
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
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                        } else {
                            moreLayout.setVisibility(View.VISIBLE);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                        }
                    }
                });
                final Object obj = forms[i][6];
                final String id = String.valueOf(forms[i][0]);

                if (!forms[i][2].equals("CREATE PATIENT")) {

                    text.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {

                            Intent i = new Intent();
                            i.putExtra("form_id", id);
                            i.putExtra("open", true);
                            i.putExtra("form_object", (byte[]) obj);
                            setResult(RESULT_OK, i);
                            onBackPressed();
                            return true;
                        }

                    });

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

                        String identifier = serverService.getPatientIdentifierBySystemIdLocalDB(String.valueOf(forms[i][3]));
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

        fillList();
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

                Boolean errorFlag  = false;
                for (int i = 0; i < checkedTag.size(); i++) {
                    String returnString = serverService.submitOfflineForm(checkedTag.get(i), true);
                    if (!returnString.equals("SUCCESS")) {

                        /*if(returnString.contains("PATIENT ALREADY EXISTS")) {
                            returnString = returnString + checkedTag.get(i);
                            errorCreatePatientFormsId.add(returnString);
                        }
                        else
                            errorEncounterFormsId.add(checkedTag.get(i));*/

                        errorFlag = true;
                    }
                }

                if(errorFlag)
                    return "COMPLETE_WITH_ERROR";

                return "SUCCESS";

            }

            @Override
            protected void onProgressUpdate(String... values) {

                final AlertDialog alertDialog = new AlertDialog.Builder(OfflineFormActivity.this, R.style.dialog).create();
                String result = values[0];
                result = result.replace("PATIENT ALREADY EXISTS ; ", "");
                final String[] resultArray = result.split(" ; ");
                String message = getResources().getString(R.string.patient_id)  + resultArray[0] + " " + getResources().getString(R.string.patient_already_exists_error) + "<br><br>";
                message = message + getResources().getString(R.string.patient_id) + " <b>" + resultArray[0] + "</b><br>";
                message = message + getResources().getString(R.string.name) + " <b>" + resultArray[1] + "</b><br>";
                String gender = resultArray[2];
                if(gender.equalsIgnoreCase("M")) gender = "Male";
                else if(gender.equalsIgnoreCase("F")) gender = "Female";
                message = message + getResources().getString(R.string.gender) + " <b>" + gender + "</b><br>";
                if(resultArray[3].equals("0")){
                    Date birthDate = App.stringToDate(resultArray[4].substring(0,10), "yyyy-MM-dd");
                    int age = App.getDiffMonths(birthDate, new Date());
                    message = message + getResources().getString(R.string.age) + " <b>" + age + " month(s)</b><br>";
                }
                else message = message + getResources().getString(R.string.age) + " <b>" + resultArray[3] + " year(s)</b><br>";
                message = message + getResources().getString(R.string.dob) + " <b>" + resultArray[4].substring(0,10) + "</b><br><br>";
                message = message + getResources().getString(R.string.merge_patient);
                alertDialog.setMessage(Html.fromHtml(message));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.patient_already_exists));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.merge),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                errorCreatePatientFormsId.add(resultArray[5]);
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

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

                    Toast toast = Toast.makeText(OfflineFormActivity.this, getResources().getString(R.string.forms_submitted_with_error), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();

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

                fillList();

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


}


