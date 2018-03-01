package edu.nyp.projecthelp;

import java.io.Serializable;

/**
 * Created by student on 31/1/18.
 */

public class ElderlyRequest implements Serializable {

    private static final long serialVersionUID = 1;

    public int requestId;
    public String requestee;
    public String gender;
    public String type;
    public String address;
    public String unitno;
    public String locationLat;
    public String locationLong;
    public String requestDate;
    public String requestTime;
    public String status;

    public ElderlyRequest(int requestId, String requestee, String gender, String type, String address, String unitno, String locationLat, String locationLong, String requestDate, String requestTime, String status) {
        this.requestId = requestId;
        this.requestee = requestee;
        this.gender = gender;
        this.type = type;
        this.address = address;
        this.unitno = unitno;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.requestDate = requestDate;
        this.requestTime = requestTime;
        this.status = status;
    }

    public ElderlyRequest() {

    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getRequestee() {
        return requestee;
    }

    public void setRequestee(String requestee) {
        this.requestee = requestee;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
