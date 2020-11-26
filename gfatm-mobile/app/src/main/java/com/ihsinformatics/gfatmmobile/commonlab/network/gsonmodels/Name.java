package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Name implements Serializable {

@SerializedName("display")
@Expose
private String display;
@SerializedName("uuid")
@Expose
private String uuid;
@SerializedName("name")
@Expose
private String name;
@SerializedName("locale")
@Expose
private String locale;
@SerializedName("localePreferred")
@Expose
private Boolean localePreferred;
@SerializedName("conceptNameType")
@Expose
private String conceptNameType;
@SerializedName("resourceVersion")
@Expose
private String resourceVersion;

public String getDisplay() {
return display;
}

public void setDisplay(String display) {
this.display = display;
}

public String getUuid() {
return uuid;
}

public void setUuid(String uuid) {
this.uuid = uuid;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getLocale() {
return locale;
}

public void setLocale(String locale) {
this.locale = locale;
}

public Boolean getLocalePreferred() {
return localePreferred;
}

public void setLocalePreferred(Boolean localePreferred) {
this.localePreferred = localePreferred;
}

public String getConceptNameType() {
return conceptNameType;
}

public void setConceptNameType(String conceptNameType) {
this.conceptNameType = conceptNameType;
}

public String getResourceVersion() {
return resourceVersion;
}

public void setResourceVersion(String resourceVersion) {
this.resourceVersion = resourceVersion;
}

}