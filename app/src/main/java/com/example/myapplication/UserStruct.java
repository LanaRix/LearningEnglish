package com.example.myapplication;

public class UserStruct {
    private String ID;
    private String NAME;

    public UserStruct(String id, String name)
    {
        ID=id;
        NAME=name;
    }

    public String getId()
    {
        return ID;
    }

    public String getNane()
    {
        return NAME;
    }
}
