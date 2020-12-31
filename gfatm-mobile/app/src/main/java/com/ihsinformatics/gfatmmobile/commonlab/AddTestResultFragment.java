package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MyLabInterface;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledEditText;
import com.ihsinformatics.gfatmmobile.commonlab.TitledHeader;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledSearchableSpinner;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Attribute;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestOrder;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.ConceptEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;

import java.util.ArrayList;
import java.util.List;

public class AddTestResultFragment extends Fragment {

    private LinearLayout mainLayout;
    private TitledHeader header;
    private MyTitledEditText testLabId;
    private MyTitledSearchableSpinner mediumType;
    private MyTitledEditText otherMediumType;
    private MyTitledSearchableSpinner result;
    private TestOrderEntity testOrder;
    private List<AttributeTypeEntity> attributeTypeEntities;

    View[] views;
    Button btnCancel;
    Button btnSubmit;

    public void setTestOrder(TestOrderEntity testOrder) {
        this.testOrder = testOrder;
    }

    MyLabInterface myLabInterface;

    public void onAttachToParentFragment(Fragment fragment) {
        myLabInterface = (MyLabInterface) fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.lab_add_test_fragment, container, false);
        mainLayout = mainContent.findViewById(R.id.mainLayout);
        btnCancel = mainContent.findViewById(R.id.btnCancel);
        btnSubmit = mainContent.findViewById(R.id.btnSubmit);

        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String testTitle = testOrder==null?"Test Title": testOrder.getLabTestType().getName();
        header = new TitledHeader(getActivity(), "Add Test Result", testOrder.getLabTestType().getName());
        setListeners();

        attributeTypeEntities = DataAccess.getInstance().getAttributeTypesByTestType(testOrder.getTestTypeId());
        views = new View[attributeTypeEntities.size()+1];
        views[0] = header;
        int i = 1;
        for(AttributeTypeEntity a: attributeTypeEntities) {
            if(a.getDatatypeClassname().contains("Concept")) {

                ConceptEntity e = DataAccess.getInstance().getConceptByUUID(a.getDatatypeConfig());
                if(e!=null) {
                    List<ConceptEntity> options = e.getChildren();
                    String[] optionsArray = new String[options.size()];
                    String[] optionsValuesArray = new String[options.size()];
                    for(int j=0; j<options.size(); j++) {
                        optionsArray[j] = options.get(j).getDisplay();
                        optionsValuesArray[j] = options.get(j).getUuid();
                    }
                    views[i] = new MyTitledSearchableSpinner(getActivity(), a.getName(), optionsArray, optionsValuesArray, null, true);
                } else {
                    views[i] = new MyTitledSearchableSpinner(getActivity(), a.getName(), getResources().getStringArray(R.array.dummy_items), null, true);
                }
            } else if(a.getDatatypeClassname().contains("ProviderDatatype")) {
                String[] optionsArray = new String[]{App.getUsername()};
                String[] optionsValuesArray = new String[]{App.getProviderUUid()};
                views[i] = new MyTitledSearchableSpinner(getActivity(), a.getName(), optionsArray, optionsValuesArray, null, true);
            } else if(a.getDatatypeClassname().contains("FreeTextDatatype")) {
                views[i] = new MyTitledEditText(getActivity(), a.getName(), true, a.getDatatypeClassname());
            } else if(a.getDatatypeClassname().contains("LongFreeTextDatatype")) {
                views[i] = new MyTitledEditText(getActivity(), a.getName(), true, a.getDatatypeClassname());
            } else if(a.getDatatypeClassname().contains("FloatDatatype")) {
                views[i] = new MyTitledEditText(getActivity(), a.getName(), true, a.getDatatypeClassname());
            } else if(a.getDatatypeClassname().contains("DateDatatype")) {
                views[i] = new MyTitledDatePicker(getActivity(), a.getName(), true, a.getDatatypeClassname());
            } else if(a.getDatatypeClassname().contains("BooleanDatatype")) {
                views[i] = new MyTitledRadioGroup(getActivity(), a.getName(), true, a.getDatatypeClassname());
            }
            i++;
        }

        View attachmentView = getActivity().getLayoutInflater().inflate(R.layout.lab_layout_attachment, null);
        attachmentView.findViewById(R.id.btnAttachFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Attach file", Toast.LENGTH_SHORT).show();
            }
        });

        for (View v : views)
            mainLayout.addView(v);
    }

    public void setListeners() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLabInterface.onCancelButtonClick();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                int i=0;
                List<AttributeEntity> attributeEntities = new ArrayList<>();
                for(View view: views) {
                    if(i > 0) {
                        AttributeEntity attributeEntity = new AttributeEntity();
                        AttributeTypeEntity attributeTypeEntity = attributeTypeEntities.get(i-1);
                        attributeEntity.setTestOrder(testOrder);
                        attributeEntity.setAttributeType(attributeTypeEntity);
                        if(view instanceof MyTitledSearchableSpinner) {

                            attributeEntity.setValueReference(((MyTitledSearchableSpinner) view).getSpinnerSelectedItem());
                        } else if (view instanceof MyTitledEditText) {
                            MyTitledEditText widget = ((MyTitledEditText) view);
                            if(attributeTypeEntity.getDatatypeConfig()!=null && attributeTypeEntity.getDatatypeConfig().startsWith("Length")) {
                                int maxLength = Integer.valueOf(attributeTypeEntity.getDatatypeConfig().split("=")[1]);
                                if(widget.getText().length()>maxLength) {
                                    widget.showError("Length can not exceed "+maxLength+" characters");
                                    isValid = false;
                                }
                            }
                            if(widget.getText().isEmpty()) {
                                i++;
                                continue;
                            }
                            attributeEntity.setValueReference(widget.getText());
                        } else if(view instanceof MyTitledRadioGroup) {
                            String text = ((MyTitledRadioGroup) view).getText();
                            attributeEntity.setValueReference(text.equalsIgnoreCase("yes")?"True":"False");
                            if(text.isEmpty()) {
                                i++;
                                continue;
                            }
                        } else if(view instanceof MyTitledDatePicker) {
                            String text = ((MyTitledDatePicker) view).getText();
                            attributeEntity.setValueReference(text);
                            if(text.isEmpty()) {
                                i++;
                                continue;
                            }
                        }

                        attributeEntities.add(attributeEntity);
                    }
                    i++;
                }
                if(isValid) {
                    DataAccess.getInstance().insertAllAttributes(attributeEntities);
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    myLabInterface.onResultSaved();
                }
            }
        });
    }

}
