package com.vytrack.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class RegionResponse {

    @SerializedName("region_id")
    @Expose
    private Integer regionId;
    @SerializedName("region_name")
    @Expose
    private String regionName;

    public ArrayList<Map<String, String>> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Map<String, String>> links) {
        this.links = links;
    }

    private ArrayList<Map<String, String>> links;


    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
