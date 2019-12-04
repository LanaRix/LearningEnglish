package com.example.myapplication;

public class CategoryStruct {
    private String CategoryId;
    private String CategoryName;
    private String Translates;

    public CategoryStruct(String CategoryId, String CategoryName, String Translates)
    {
        this.CategoryId=CategoryId;
        this.CategoryName=CategoryName;
        this.Translates=Translates;
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
}
