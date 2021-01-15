package com.ihsinformatics.gfatmmobile.medication.gson_pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreviousOrder {

@SerializedName("uuid")
@Expose
private String uuid;

public String getUuid() {
return uuid;
}

public void setUuid(String uuid) {
this.uuid = uuid;
}

}