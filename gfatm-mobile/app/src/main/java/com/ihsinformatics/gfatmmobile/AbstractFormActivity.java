package com.ihsinformatics.gfatmmobile;

/**
 * Created by Rabbia on 11/10/2016.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
import com.ihsinformatics.gfatmmobile.custom.MyEditText;
import com.ihsinformatics.gfatmmobile.custom.MyRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public abstract class AbstractFormActivity extends Fragment
        implements
        View.OnClickListener,
        ViewPager.OnPageChangeListener,
        SeekBar.OnSeekBarChangeListener,
        AdapterView.OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener,
        View.OnLongClickListener
   {

    protected View mainContent;
    protected ServerService serverService;

    protected Calendar formDateCalendar;
    protected DialogFragment formDateFragment;
    public static final int	DATE_DIALOG_ID	= 1;

    protected Calendar secondDateCalendar;
    protected DialogFragment secondDateFragment;
    public static final int SECOND_DATE_DIALOG_ID = 2;

    protected ArrayList<ViewGroup>	groups;
    protected View[][] viewGroups;

    protected int			        PAGE_COUNT		= 0;
    protected String                FORM_NAME       = "";
    protected View[]				views;
    protected ViewPager             pager;
    protected SeekBar               navigationSeekbar;
    protected LinearLayout          navigatorLayout;
    protected Button				firstButton;
    protected Button				lastButton;
    protected Button				nextButton;
    protected Button                submitButton;
    protected Button                saveButton;
    protected Button				clearButton;
    protected TextView				pageButton;
    protected TextView              formName;

    protected TextView              editTextView;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        mainContent = inflater.inflate(R.layout.form_fragment_template, container, false);

        serverService = new ServerService (mainContent.getContext());

        formDateCalendar = Calendar.getInstance ();
        formDateFragment = new SelectDateFragment();

        secondDateCalendar = Calendar.getInstance ();
        secondDateFragment = new SelectDateFragment();

        editTextView = (TextView) mainContent.findViewById(R.id.edit);
        editTextView.setPaintFlags(editTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        navigationSeekbar = (SeekBar) mainContent.findViewById (R.id.navigationSeekbar);
        pageButton = (TextView) mainContent.findViewById (R.id.pageCount);
        formName = (TextView) mainContent.findViewById(R.id.formName);
        navigatorLayout = (LinearLayout) mainContent.findViewById (R.id.navigatorLayout);
        firstButton = (Button) mainContent.findViewById (R.id.first_button);
        lastButton = (Button) mainContent.findViewById (R.id.last_button);
        nextButton = (Button) mainContent.findViewById (R.id.next_button);
        clearButton = (Button) mainContent.findViewById (R.id.clearButton);
        submitButton = (Button) mainContent.findViewById (R.id.submitButton);
        saveButton = (Button) mainContent.findViewById(R.id.saveButton);

        if(App.getMode().equalsIgnoreCase("Offline"))
            submitButton.setVisibility(View.GONE);
        else
            submitButton.setVisibility(View.VISIBLE);

        View[] setListener = new View[]{firstButton, lastButton, clearButton, submitButton, nextButton, saveButton};

        for (View v : setListener) {
            if (v instanceof Spinner) {
                ((Spinner) v).setOnItemSelectedListener(this);
            } else if (v instanceof CheckBox) {
                ((CheckBox) v).setOnCheckedChangeListener(this);
            } else if (v instanceof RadioGroup) {
                ((RadioGroup) v).setOnClickListener(this);
            } else if (v instanceof Button) {
                ((Button) v).setOnClickListener(this);
            }  else if (v instanceof RadioButton) {
                ((RadioButton) v).setOnClickListener(this);
            } else if (v instanceof ImageButton) {
                ((ImageButton) v).setOnClickListener(this);
            }
        }
        navigationSeekbar.setOnSeekBarChangeListener (this);

        if(App.isLanguageRTL())
            navigationSeekbar.setRotation( 180 );

        return mainContent;
    }

    /**
     * Initialize views
     */
    public abstract void initViews ();

    /**
     * Updates data in form views
     */
    public abstract void updateDisplay ();

    /**
     * Goto first view in the pager
     */
    public void gotoNextPage ()
    {

        int cp = pager.getCurrentItem();

        if (App.isLanguageRTL ())
        {
            if(pager.getCurrentItem()-1 >= 0)
                gotoPage(pager.getCurrentItem()-1);
        }
        else{
            if(pager.getCurrentItem()+1 != PAGE_COUNT)
                gotoPage(pager.getCurrentItem()+1);
        }
    }


    /**
     * Goto first view in the pager
     */
    public void gotoFirstPage ()
    {
        if (App.isLanguageRTL ())
        {
            gotoPage (PAGE_COUNT - 1);
        }
        else
        {
            gotoPage (0);
        }
    }

    /**
     * Goto last view in the pager
     */
    public void gotoLastPage ()
    {

        if (App.isLanguageRTL ())
        {
            gotoPage (0);
        }
        else
        {
            gotoPage (PAGE_COUNT - 1);
        }
    }

    /**
     * Goto view at given location in the pager
     */
    protected void gotoPage (int pageNo)
    {

        pager.setCurrentItem(pageNo);
        navigationSeekbar.setProgress(pageNo);

    }

    /**
     * Validate form views and values
     *
     * @return
     */
    public abstract boolean validate ();
    /**
     * Submit the form to the server
     *
     * @return
     */
    public abstract boolean submit ();

    /**
     * Save the form to the server
     *
     * @return
     */
    public abstract boolean save ();

    @Override
    public void onNothingSelected (AdapterView<?> view)
    {
        // Not implemented
    }

    @Override
    public void onStopTrackingTouch (SeekBar seekbar)
    {
        // Not implemented
    }

    @Override
    public void onStartTrackingTouch (SeekBar seekbar)
    {
        // Not implemented
    }

    @Override
    public void onProgressChanged (SeekBar seekbar, int progress, boolean isByUser)
    {
        // Move to page at the index of progress
        pager.setCurrentItem (progress);
        setPageCountStatus();
    }

    @Override
    public void onPageSelected (int pageNo)
    {
        gotoPage (pageNo);

    }

    @Override
    public void onPageScrolled (int arg0, float arg1, int arg2)
    {

    }

    @Override
    public void onPageScrollStateChanged (int arg0)
    {
        // Not implemented
    }

    public void setPageCountStatus ()
    {

        String currentPage = "";
        String totalPage = "";

        if(App.isLanguageRTL()){

            int count = PAGE_COUNT - pager.getCurrentItem();

            if(count < 10)
                currentPage = "0" + (count);
            else
                currentPage = "" + (count);


            if(PAGE_COUNT < 10)
                totalPage = "0" + PAGE_COUNT;
            else
                totalPage = "" + PAGE_COUNT;

        }
        else{

            if(pager.getCurrentItem()+1 < 10)
                currentPage = "0" + (pager.getCurrentItem()+1);
            else
                currentPage = "" + (pager.getCurrentItem()+1);

            if(PAGE_COUNT < 10)
                totalPage = "0" + PAGE_COUNT;
            else
                totalPage = "" + PAGE_COUNT;

        }

        String page = currentPage + "/" + totalPage;
        pageButton.setText(page);

    }

    @Override
    public void onClick(View view) {

        if (view == firstButton)
        {
            gotoFirstPage ();
        }
        else if (view == lastButton)
        {
            gotoLastPage ();
        }
        else if (view == nextButton){
            gotoNextPage();
        }
        else if (view == clearButton){

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.warning_before_clear));
            Drawable clearIcon = getResources().getDrawable(R.drawable.ic_clear);
            DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_clear));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           resetViews();
                            try {
                                InputMethodManager imm = (InputMethodManager)mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
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
        else if (view == submitButton){
            if(validate()){

                int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
                alertDialog.setMessage(getString(R.string.warning_before_submit));
                Drawable clearIcon = getResources().getDrawable(R.drawable.ic_submit);
                DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_submit));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                submit();
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
        else if (view == saveButton){
            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.warning_before_save));
            Drawable clearIcon = getResources().getDrawable(R.drawable.ic_save);
            DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_save));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            save();
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

    public void resetViews(){
        for (View v : views)
        {
            if (v instanceof MySpinner)
            {
                ((MySpinner) v).selectDefaultValue();
            }
            else  if (v instanceof TitledSpinner)
            {
                ((TitledSpinner) v).getSpinner().selectDefaultValue();
            }
            else if (v instanceof MyEditText)
            {
                ((MyEditText) v).setDefaultValue();
            }
            else if (v instanceof TitledEditText)
            {
                ((TitledEditText) v).getEditText().setDefaultValue();
            }
            else if (v instanceof MyRadioGroup)
            {
                ((MyRadioGroup) v).selectDefaultValue();
            }
            else if (v instanceof TitledRadioGroup)
            {
                ((TitledRadioGroup) v).getRadioGroup().selectDefaultValue();
            }
            else if (v instanceof TitledCheckBoxes)
            {
                ((TitledCheckBoxes) v).selectDefaultValue();
            }
            else if (v instanceof MyCheckBox)
            {
                ((MyCheckBox) v).setDefaultValue();
            }

        }

        formDateCalendar = Calendar.getInstance();
        secondDateCalendar = Calendar.getInstance();

        gotoFirstPage();

    }


       public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

           @Override
           public Dialog onCreateDialog(Bundle savedInstanceState) {
               final Calendar calendar;
               if(getArguments().getInt("type") == DATE_DIALOG_ID)
                    calendar = formDateCalendar;
               else if(getArguments().getInt("type") == SECOND_DATE_DIALOG_ID)
                   calendar = secondDateCalendar;
               else
                   return null;

               int yy = calendar.get(Calendar.YEAR);
               int mm = calendar.get(Calendar.MONTH);
               int dd = calendar.get(Calendar.DAY_OF_MONTH);
               DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
               dialog.getDatePicker().setTag(getArguments().getInt("type"));
               dialog.getDatePicker().setMaxDate(new Date().getTime());
               return dialog;
           }

           @Override
           public void onDateSet(DatePicker view, int yy, int mm, int dd) {

               if(((int)view.getTag()) == DATE_DIALOG_ID)
                    formDateCalendar.set(yy,mm,dd);
               else if(((int)view.getTag()) == SECOND_DATE_DIALOG_ID)
                    secondDateCalendar.set(yy,mm,dd);

               updateDisplay();
           }
       }

}


