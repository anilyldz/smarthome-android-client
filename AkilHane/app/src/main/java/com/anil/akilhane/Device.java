package com.anil.akilhane;

import org.json.JSONObject;

/**
 * Created by Anil on 17.03.2016.
 */
public class Device {

    private static Device instance = new Device();

    private Device() {
    }

    public static Device getInstance() {
        return instance;
    }

    private Integer id;
    private String location;
    private String name;
    private String status;
    private String offer;
    private Boolean active;
    JSONObject deviceStatus;
    JSONObject report;
    JSONObject tasks;

    public Device(Integer id, Boolean active, String location, String name, String status) {
        super();
        this.id = id;
        this.location = location;
        this.name = name;
        this.status = status;
        this.active = active;
    }

    public void setDeviceStatus(JSONObject deviceStatus){
        this.deviceStatus = deviceStatus;
    }

    public JSONObject getDeviceStatus(){return deviceStatus;}





    public void setOffer(String offer){
        this.offer = offer;
    }

    public String getOffer(){return offer;}





    public void seTtasks(JSONObject tasks){
        this.tasks = tasks;
    }

    public JSONObject getTasks(){return tasks;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


}
