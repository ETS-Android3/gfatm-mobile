package com.ihsinformatics.gfatmmobile.medication;

import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;

public interface DrugRenewListener {
    void onRenew(DrugOrderEntity drug);
    void onRevise(DrugOrderEntity drug);
}
