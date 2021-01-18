package com.ihsinformatics.gfatmmobile.medication.utils;

import com.ihsinformatics.gfatmmobile.commonlab.network.CustomCallback;
import com.ihsinformatics.gfatmmobile.commonlab.network.CustomCallbackHelper;
import com.ihsinformatics.gfatmmobile.commonlab.network.RetrofitClientFactory;
import com.ihsinformatics.gfatmmobile.commonlab.network.Utils;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;
import com.ihsinformatics.gfatmmobile.medication.MyMedicationInterface;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.DrugOrder;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.DrugOrderPostModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MedicationUtils {

    private OnDownloadCompleteListener myMedicationInterface;

    public MedicationUtils(OnDownloadCompleteListener myMedicationInterface) {
        this.myMedicationInterface = myMedicationInterface;
    }

    public static boolean isCurrentActive(DrugOrderEntity entity) {
        try {
            if (entity.getDateStopped() != null) {
                String stopDate = entity.getDateStopped().substring(0, 10);
                Date dateStopped = new SimpleDateFormat("yyy-MM-dd").parse(stopDate);
                if (new Date().getTime() > dateStopped.getTime()) {
                    return false;
                }
                return false;
            }

            if (entity.getAutoExpireDate() != null) {
                String autoExp = entity.getAutoExpireDate().substring(0, 10);
                Date autoExpireDate = new SimpleDateFormat("yyy-MM-dd").parse(autoExp);
                if (new Date().getTime() > autoExpireDate.getTime()) {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }

        return true;
    }


    private int totalUploaded = 0;
    private int actualSize = 0;

    public void upload(List<DrugOrderEntity> drugOrders) {
        totalUploaded = 0;
        actualSize = drugOrders.size();
        for(DrugOrderEntity e: drugOrders) {
            if (e.getUuid() != null && !e.getUuid().isEmpty()) {
                e.setUuid(null);
                e.setDateStopped(null);
            }
            uploadOne(e);
        }
    }

    private void uploadOne(DrugOrderEntity e) {
        DrugOrderPostModel p = new DrugOrderPostModel().inflateWithDbEntity(e);

        Call<DrugOrder> call = RetrofitClientFactory.createCommonLabApiClient().uploadDrugOrder("custom:(uuid,orderNumber)", p, Utils.getBasicAuth());

        call.enqueue(new CustomCallbackHelper<DrugOrder>(new CustomCallback<DrugOrder>() {
            @Override
            public void onResponse(Call<DrugOrder> call, Response<DrugOrder> response, long requestID) {
                if(response.isSuccessful()) {
                    DrugOrderEntity entity = DataAccess.getInstance().getDrugOrderByID(requestID);
                    entity.setUuid(response.body().getUuid());
                    entity.setOrderNumber(response.body().getOrderNumber());
                    entity.setToUpload(false);
                    DataAccess.getInstance().updateDrugOrder(entity);
                }
                afterUpload();
            }

            @Override
            public void onFailure(Call<DrugOrder> call, Throwable t, long requestID) {
                t.printStackTrace();
                afterUpload();
            }

        }, e.getId()));
    }

    private void afterUpload() {
        totalUploaded++;
        if(totalUploaded == actualSize) {
            myMedicationInterface.onCompleted();
        }
    }

    public interface OnDownloadCompleteListener {
        public void onCompleted();
    }
}
