package com.example.findit;

public class ItemsModel {
    private String itemName;
    private String itemPlace;
    private  String itemMessege;
    private Boolean itemReport;
    private  String itemImageUri;
    private String itemId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemImageUri() {
        return itemImageUri;
    }

    public void setItemImageUri(String itemImageUri) {
        this.itemImageUri = itemImageUri;
    }

    public ItemsModel(){
//        Constructor kosong untuk firebase
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPlace() {
        return itemPlace;
    }

    public void setItemPlace(String itemPlace) {
        this.itemPlace = itemPlace;
    }

    public String getItemMessege() {
        return itemMessege;
    }

    public void setItemMessege(String itemMessege) {
        this.itemMessege = itemMessege;
    }

    public Boolean getItemReport() {
        return itemReport;
    }

    public void setItemReport(Boolean itemReport) {
        this.itemReport = itemReport;
    }

    public ItemsModel(String itemName, String itemPlace, String itemMessege, Boolean itemReport, String itemImageUri, String itemId) {
        this.itemName = itemName;
        this.itemPlace = itemPlace;
        this.itemMessege = itemMessege;
        this.itemReport = itemReport;
        this.itemImageUri = itemImageUri;
        this.itemId = itemId;
    }
}
