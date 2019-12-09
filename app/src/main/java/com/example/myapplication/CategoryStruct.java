package com.example.myapplication;

public class CategoryStruct {
    private String CategoryId;
    private String ConnectId;
    private String CategoryName;
    private String Translates;

    public CategoryStruct(String CategoryId, String CategoryName, String Translates, String ConnectId)
    {
        this.CategoryId=CategoryId;
        this.CategoryName=CategoryName;
        this.Translates=Translates;
        this.ConnectId=ConnectId;
    }

    public String getCategoryId()
    {
        return CategoryId;
    }

    public String getCategoryName()
    {
        return CategoryName;
    }

    public String getTranslates()
    {
        return Translates;
    }
    public String getConnectId() {return ConnectId;}

}
