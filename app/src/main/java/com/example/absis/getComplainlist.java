package com.example.absis;

public class getComplainlist {

    private int id;
    private String name;
    private String complaint;
    private String address;
    private String mobile;
    private String lat;
    private String lng;
    private String image_name;
    private String ip;
    private String up_date;

    public getComplainlist(int id,String name,String complaint,String address,String mobile,String lat, String lng,String image_name) {
        this.id=id;
        this.name=name;
        this.complaint=complaint;
        this.address=address;
        this.mobile=mobile;
        this.lat=lat;
        this.lng=lng;
        this.image_name=image_name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getComplaint() {
        return complaint;
    }
    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getImage_name() {
        return image_name;
    }
    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIp() {
        return ip;
    }
    public String getLat() {
        return lat;
    }
    public String getLng() {
        return lng;
    }
    public String getUp_date() {
        return up_date;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
    public void setUp_date(String up_date) {
        this.up_date = up_date;
    }
}
