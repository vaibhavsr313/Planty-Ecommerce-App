package com.example.planty30;

public class DataClass5 {
    private String dataName;
    private String dataDesc;
    private String dataPrice;
    private String dataTemp;
    private String dataWater;
    private String dataImg;

    public String getDataName() {
        return dataName;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataPrice() {
        return dataPrice;
    }

    public String getDataTemp() {
        return dataTemp;
    }

    public String getDataWater() {
        return dataWater;
    }

    public String getDataImg() {
        return dataImg;
    }

    public DataClass5(String dataName, String dataDesc, String dataPrice, String dataTemp, String dataWater, String dataImg) {
        this.dataName = dataName;
        this.dataDesc = dataDesc;
        this.dataPrice = dataPrice;
        this.dataTemp = dataTemp;
        this.dataWater = dataWater;
        this.dataImg = dataImg;
    }

    public DataClass5(){

    }
}
