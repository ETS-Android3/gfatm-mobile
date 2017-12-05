package com.ihsinformatics.gfatmmobile.shared;

/**
 * Created by Rabbia on 12/4/2017.
 */


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.util.ServerService;

public class ListViewAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, Object[]>> data;
    private static LayoutInflater inflater=null;

    public ListViewAdapter(Activity a, ArrayList<HashMap<String, Object[]>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item, null);

        LinearLayout mainContent = (LinearLayout) vi.findViewById(R.id.mainContent);

        final int color = App.getColor(vi.getContext(), R.attr.colorAccent);
        int color1 = App.getColor(vi.getContext(), R.attr.colorAccent);
        ServerService serverService = new ServerService(vi.getContext());

        HashMap<String, Object[]> item = new HashMap<String, Object[]>();
        item = data.get(position);
        Object[] form = item.get("main");

        LinearLayout verticalLayout = new LinearLayout(vi.getContext());
        verticalLayout.setOrientation(LinearLayout.VERTICAL);
        verticalLayout.setPadding(10, 20, 10, 20);

        LinearLayout linearLayout = new LinearLayout(vi.getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(vi.getContext().getDrawable(R.drawable.divider));

        final LinearLayout moreLayout = new LinearLayout(vi.getContext());
        moreLayout.setOrientation(LinearLayout.VERTICAL);

        Object[] checkboxeObject = item.get("checkbox");
        linearLayout.addView((CheckBox)checkboxeObject[0]);

        Object[] textViewObject = item.get("textview");
        final TextView text = (TextView) textViewObject[0];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        text.setLayoutParams(params);
        text.setTextSize(vi.getResources().getDimension(R.dimen.small));
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

        if (!(form[2].equals("CREATE PATIENT") || form[2].equals(""))) {

            verticalLayout.addView(linearLayout);

            if (!(form[3] == null || form[3].equals("") || form[3].equals("null"))) {

                LinearLayout ll1 = new LinearLayout(vi.getContext());
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv = new TextView(vi.getContext());
                tv.setText(vi.getResources().getString(R.string.patient_id) + " ");
                tv.setTextSize(vi.getResources().getDimension(R.dimen.small));
                tv.setTextColor(color1);
                ll1.addView(tv);

                String identifier = serverService.getPatientIdentifierBySystemIdLocalDB(String.valueOf(form[3]));
                TextView tv1 = new TextView(vi.getContext());
                tv1.setText(identifier);
                tv1.setTextSize(vi.getResources().getDimension(R.dimen.small));
                ll1.addView(tv1);

                moreLayout.addView(ll1);
            }

            LinearLayout ll2 = new LinearLayout(vi.getContext());
            ll2.setOrientation(LinearLayout.HORIZONTAL);

            TextView tv2 = new TextView(vi.getContext());
            tv2.setText(vi.getResources().getString(R.string.form_date) + " ");
            tv2.setTextSize(vi.getResources().getDimension(R.dimen.small));
            tv2.setTextColor(color1);
            ll2.addView(tv2);

            TextView tv3 = new TextView(vi.getContext());
            tv3.setText(String.valueOf(form[4]));
            tv3.setTextSize(vi.getResources().getDimension(R.dimen.small));
            ll2.addView(tv3);

            moreLayout.addView(ll2);

            LinearLayout ll3 = new LinearLayout(vi.getContext());
            ll3.setOrientation(LinearLayout.HORIZONTAL);

            TextView tv4 = new TextView(vi.getContext());
            tv4.setText(vi.getResources().getString(R.string.location) + " ");
            tv4.setTextSize(vi.getResources().getDimension(R.dimen.small));
            tv4.setTextColor(color1);
            ll3.addView(tv4);

            TextView tv5 = new TextView(vi.getContext());
            tv5.setText(String.valueOf(form[7]));
            tv5.setTextSize(vi.getResources().getDimension(R.dimen.small));
            ll3.addView(tv5);

            moreLayout.addView(ll3);

        } else {

            verticalLayout.addView(linearLayout);

            OfflineForm offlineForm = serverService.getOfflineFormById(Integer.parseInt(String.valueOf(form[0])));
            ArrayList<String[][]> array = offlineForm.getObsValue();

            for (int k = 0; k < array.size(); k++) {
                String[][] obs = array.get(k);

                LinearLayout ll1 = new LinearLayout(vi.getContext());
                ll1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv = new TextView(vi.getContext());
                tv.setText(App.convertToTitleCase(obs[0][0] + ": "));
                tv.setTextSize(vi.getResources().getDimension(R.dimen.small));
                tv.setTextColor(color1);
                ll1.addView(tv);

                TextView tv1 = new TextView(vi.getContext());
                tv1.setText(obs[0][1]);
                tv1.setTextSize(vi.getResources().getDimension(R.dimen.small));
                ll1.addView(tv1);

                moreLayout.addView(ll1);

            }

        }

        moreLayout.setPadding(80, 0, 0, 0);
        moreLayout.setVisibility(View.GONE);
        verticalLayout.addView(moreLayout);

        mainContent.addView(verticalLayout);

        return vi;
    }
}
