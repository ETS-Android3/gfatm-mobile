package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.gfatmmobile.MyLabInterface;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Attribute;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestOrder;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;

import java.util.List;

public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.ViewHolder> {

    private List<TestOrderEntity> data;
    private final LayoutInflater mInflater;
    private final boolean isCompleted;
    Context context;

    MyLabInterface myLabInterface;

    LabTestAdapter(Context context, List<TestOrderEntity> data, String testType, Fragment fragment) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
        isCompleted = testType.equals(context.getString(R.string.complete));
        myLabInterface = (MyLabInterface) fragment;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lab_test_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TestOrderEntity testOrder = data.get(position);
        if (!isCompleted)
            holder.ibCheck.setVisibility(View.GONE);
        Object[][] encounter = DataAccess.getInstance().getEncountersByUUID(context, testOrder.getEncounterUUID());
        final String encounterName = encounter[0][0].toString();
        String encounterDate = encounter[0][3].toString();
        holder.tvTestName.setText(testOrder.getLabTestType().getName());
        holder.tvEncounterName.setText("Encounter Name: " + encounterName);
        holder.tvEncounterDate.setText("Encounter Date: " + encounterDate);

        holder.btnAddResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLabInterface.onAddResultButtonClick(position, isCompleted);
            }
        });

        holder.btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSummaryDialog(testOrder, encounterName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton ibCheck;
        Button btnAddResult;
        Button btnSummary;
        TextView tvTestName;
        TextView tvEncounterName;
        TextView tvEncounterDate;

        ViewHolder(View itemView) {
            super(itemView);
            ibCheck = itemView.findViewById(R.id.ibCheck);
            btnAddResult = itemView.findViewById(R.id.btnAddResult);
            btnSummary = itemView.findViewById(R.id.btnSummary);
            tvTestName = itemView.findViewById(R.id.tvTestName);
            tvEncounterName = itemView.findViewById(R.id.tvEncounterName);
            tvEncounterDate = itemView.findViewById(R.id.tvEncounterDate);
        }
    }

    private void showSummaryDialog(TestOrderEntity testOrder, String encounterName) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = mInflater.inflate(R.layout.lab_dialog_titled, null);

        dialogView.findViewById(R.id.layoutButtons).setVisibility(View.GONE);
        ((TextView) dialogView.findViewById(R.id.tvTitle)).setText(context.getString(R.string.summary));

        LinearLayout mainContent = dialogView.findViewById(R.id.mainContent);

        String[][] orderDetails = {
                {"Test Order ID", testOrder.getOrderNumber()==null?"":testOrder.getOrderNumber()},
                {"Test Group", testOrder.getLabTestType().getTestGroup()},
                {"Test Type", testOrder.getLabTestType().getName()},
                {"Encounter Type", encounterName.substring(0, encounterName.length()-10)},
                {"Reference Number", testOrder.getLabReferenceNumber()},
                {"Require Specimen", testOrder.getLabTestType().getRequiresSpecimen()?"Yes":"No"},
                {"Created By", testOrder.getCreator()==null?"not available":testOrder.getCreator()},
                {"Date Created", testOrder.getDateCreated()==null?"not available":testOrder.getDateCreated()},
                {"UUID", testOrder.getUuid()}
        };

        testOrder.setAttributes(DataAccess.getInstance().getAttributesByTestOrder(testOrder.getId()));
        List<AttributeEntity> attributesList = testOrder.getAttributes();
        String[][] attributes = new String[attributesList.size()][2];
        int i=0;
        for(AttributeEntity attribute: attributesList) {
            String question = attribute.getDisplay();
            String answer = attribute.getValueReference();

            attributes[i][0] = question;
            attributes[i][1] = answer;

            i++;
        }

        ExpandableLayout[] expandableLayouts = {new ExpandableLayout(context, "Test Order Detail", orderDetails),
                new ExpandableLayout(context, "Test Result Detail", attributes)};

        for (ExpandableLayout expandableLayout : expandableLayouts)
            mainContent.addView(expandableLayout);

        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        final ImageButton ibClose = dialogView.findViewById(R.id.ibClose);
        ibClose.setVisibility(View.VISIBLE);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}