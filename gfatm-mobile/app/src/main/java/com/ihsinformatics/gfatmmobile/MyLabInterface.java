package com.ihsinformatics.gfatmmobile;

public interface MyLabInterface {
    void onAddResultButtonClick(int position, boolean isCompleted);
    void onCancelButtonClick();
    String getEncounterName();
    void onResultSaved();
}
