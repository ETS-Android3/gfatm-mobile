package com.ihsinformatics.gfatmmobile.medication;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDoseUnit;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDuration;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequency;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRoute;

import java.util.Calendar;
import java.util.List;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;
    private List<DrugModel> drugModels;
    private List<MedicationDoseUnit> doses;
    private List<MedicationDuration> durations;
    private List<MedicationFrequency> frequencies;
    private List<MedicationRoute> routes;
    MyDrugInterface myDrugInterface;

    public interface MyDrugInterface {
        void updateDrugsList();
    }

    public void onAttachToParentFragment(Fragment fragment) {
        myDrugInterface = (MyDrugInterface) fragment;
    }

    DrugAdapter(Context context, List<DrugModel> drugModels) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.drugModels = drugModels;

        this.doses = DataAccess.getInstance().getAllDoses();
        this.durations = DataAccess.getInstance().getAllDurations();
        this.frequencies = DataAccess.getInstance().getAllFrequencies();
        this.routes = DataAccess.getInstance().getAllRoutes();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.drug_item, parent, false);
        return new DrugAdapter.ViewHolder(view);
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.title.setText(drugModels.get(position).getName());
        final DrugModel drugModel = drugModels.get(position);

        holder.doseAmount.setTag(position);
        holder.doseAmount.setText(drugModel.getDoseAmount() != null ? drugModel.getDoseAmount() : "");

        holder.doseUnit.setTag(position);
        holder.doseUnit.setSelection(getIndex(holder.doseUnit, drugModel.getDoseUnit()));

        holder.frequency.setTag(position);
        holder.frequency.setSelection(getIndex(holder.frequency, drugModel.getFrequency()));

        holder.route.setTag(position);
        holder.route.setSelection(getIndex(holder.route, drugModel.getRoute()));

        holder.durationAmount.setTag(position);
        holder.durationAmount.setText(drugModel.getDurationAmount() != null ? drugModel.getDurationAmount() : "");

        holder.durationUnit.setTag(position);
        holder.durationUnit.setSelection(getIndex(holder.durationUnit, drugModel.getDurationUnit()));

        holder.startDate.setTag(position);
        holder.startDate.setText(drugModel.getStartDate() != null ? drugModel.getStartDate() : "");

        holder.instructions.setTag(position);
        holder.instructions.setText(drugModel.getInstructions() != null ? drugModel.getInstructions() : "");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage("Are you sure you want to delete this drug?");
                alertDialog.setTitle("Alert");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                drugModels.remove(position);
                                notifyDataSetChanged();
                                myDrugInterface.updateDrugsList();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.show();
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.dark_grey));

            }
        });

        holder.startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yy, int mm, int dd) {
                                calendar.set(yy, mm, dd);
                                holder.startDate.setText(DateFormat.format("MMMM dd,yyyy", calendar).toString());
                            }

                        }, year, month, dayOfMonth);

                Calendar today = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMaxDate(today.getTime().getTime());

                datePickerDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return drugModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView delete;
        private EditText doseAmount;
        private Spinner doseUnit;
        private Spinner frequency;
        private Spinner route;
        private EditText durationAmount;
        private Spinner durationUnit;
        private EditText startDate;
        private EditText instructions;
        private ArrayAdapter<String> dosesAdapter;
        private ArrayAdapter<String> durationsAdapter;
        private ArrayAdapter<String> frequenciesAdapter;
        private ArrayAdapter<String> routesAdapter;



        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            delete = itemView.findViewById(R.id.ivDelete);
            doseAmount = itemView.findViewById(R.id.etDoseAmount);
            doseUnit = itemView.findViewById(R.id.spDoseUnit);
            frequency = itemView.findViewById(R.id.spFrequency);
            route = itemView.findViewById(R.id.spRoute);
            durationAmount = itemView.findViewById(R.id.etDuration);
            durationUnit = itemView.findViewById(R.id.spDurationUnit);
            startDate = itemView.findViewById(R.id.etStartDate);
            instructions = itemView.findViewById(R.id.etInstructions);

            setAdapters();


            doseAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    int position = (int) doseAmount.getTag();
                    drugModels.get(position).setDoseAmount(s.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            doseUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = (int) doseUnit.getTag();
                    drugModels.get(position).setDoseUnit(doseUnit.getSelectedItem().toString());
                    drugModels.get(position).setDoseUnit(doseUnit.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

            });

            frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = (int) frequency.getTag();
                    drugModels.get(position).setFrequency(frequency.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

            });

            route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = (int) route.getTag();
                    drugModels.get(position).setRoute(route.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

            });

            durationAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    int position = (int) durationAmount.getTag();
                    drugModels.get(position).setDurationAmount(s.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            durationUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = (int) durationUnit.getTag();
                    drugModels.get(position).setDurationUnit(durationUnit.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

            });

            startDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    int position = (int) startDate.getTag();
                    drugModels.get(position).setStartDate(s.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            instructions.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    int position = (int) instructions.getTag();
                    drugModels.get(position).setInstructions(s.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }

        private void setAdapters() {
            String[] dosesArray = new String[doses.size()];
            String[] durationsArray = new String[durations.size()];
            String[] frequenciesArray = new String[frequencies.size()];
            String[] routesArray = new String[routes.size()];

            for(int i=0; i<doses.size(); i++) {
                dosesArray[i] = doses.get(i).getDisplay();
            }

            for(int i=0; i<durations.size(); i++) {
                durationsArray[i] = durations.get(i).getDisplay();
            }

            for(int i=0; i<frequencies.size(); i++) {
                frequenciesArray[i] = frequencies.get(i).getDisplay();
            }

            for(int i=0; i<routes.size(); i++) {
                routesArray[i] = routes.get(i).getDisplay();
            }

            dosesAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, dosesArray);
            durationsAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, durationsArray);
            frequenciesAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, frequenciesArray);
            routesAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, routesArray);

            doseUnit.setAdapter(dosesAdapter);
            durationUnit.setAdapter(durationsAdapter);
            frequency.setAdapter(frequenciesAdapter);
            route.setAdapter(routesAdapter);
        }
    }
}