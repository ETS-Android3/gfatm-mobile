package com.ihsinformatics.gfatmmobile.medication;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;

import java.util.Calendar;
import java.util.List;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;
    List<Drug> drugs;

    public interface MyDrugInterface {
        void updateDrugsList();
    }

    MyDrugInterface myDrugInterface;

    public void onAttachToParentFragment(Fragment fragment) {
        myDrugInterface = (MyDrugInterface) fragment;
    }

    DrugAdapter(Context context, List<Drug> drugs) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.drugs = drugs;
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

        holder.title.setText(drugs.get(position).getName());
        final Drug drug = drugs.get(position);

        holder.doseAmount.setTag(position);
        holder.doseAmount.setText(drug.getDoseAmount() != null ? drug.getDoseAmount() : "");

        holder.doseUnit.setTag(position);
        holder.doseUnit.setSelection(getIndex(holder.doseUnit, drug.getDoseUnit()));

        holder.frequency.setTag(position);
        holder.frequency.setSelection(getIndex(holder.frequency, drug.getFrequency()));

        holder.route.setTag(position);
        holder.route.setSelection(getIndex(holder.route, drug.getRoute()));

        holder.durationAmount.setTag(position);
        holder.durationAmount.setText(drug.getDurationAmount() != null ? drug.getDurationAmount() : "");

        holder.durationUnit.setTag(position);
        holder.durationUnit.setSelection(getIndex(holder.durationUnit, drug.getDurationUnit()));

        holder.startDate.setTag(position);
        holder.startDate.setText(drug.getStartDate() != null ? drug.getStartDate() : "");

        holder.instructions.setTag(position);
        holder.instructions.setText(drug.getInstructions() != null ? drug.getInstructions() : "");

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
                                drugs.remove(position);
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
        return drugs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView delete;
        EditText doseAmount;
        Spinner doseUnit;
        Spinner frequency;
        Spinner route;
        EditText durationAmount;
        Spinner durationUnit;
        EditText startDate;
        EditText instructions;

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

            doseAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    int position = (int) doseAmount.getTag();
                    drugs.get(position).setDoseAmount(s.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            doseUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = (int) doseUnit.getTag();
                    drugs.get(position).setDoseUnit(doseUnit.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

            });

            frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = (int) frequency.getTag();
                    drugs.get(position).setFrequency(frequency.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

            });

            route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = (int) route.getTag();
                    drugs.get(position).setRoute(route.getSelectedItem().toString());
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
                    drugs.get(position).setDurationAmount(s.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            durationUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = (int) durationUnit.getTag();
                    drugs.get(position).setDurationUnit(durationUnit.getSelectedItem().toString());
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
                    drugs.get(position).setStartDate(s.toString());
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
                    drugs.get(position).setInstructions(s.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
    }
}
