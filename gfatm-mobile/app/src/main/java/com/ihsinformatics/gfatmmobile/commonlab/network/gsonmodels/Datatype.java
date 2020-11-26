package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datatype implements Serializable {

@SerializedName("uuid")
@Expose
private String uuid;
@SerializedName("display")
@Expose
private String display;

public String getUuid() {
return uuid;
}

public void setUuid(String uuid) {
this.uuid = uuid;
}

public String getDisplay() {
return display;
}

public void setDisplay(String display) {
this.display = display;
}

}