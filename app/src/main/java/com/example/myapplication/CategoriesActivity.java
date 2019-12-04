package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class CategoriesActivity extends AppCompatActivity {
    int currentUserId;
    String currentUserName;
    ListView CategoriesList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryStruct> categoryStruct;
    ArrayAdapter<CategoryStruct> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_layout);
        Bundle arguments = getIntent().getExtras();
        currentUserId = arguments.getInt("ID");
        currentUserName=arguments.getString("NAME");

        CategoriesList = (ListView) findViewById(R.id.listview);

        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories WHERE _userId=?", new String [] {Integer.toString(currentUserId)});

        Cursor cursor1;
        if (cursor.getCount()!=0) {
            categoryStruct = new ArrayList<CategoryStruct>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //поиск id перевода
                cursor1=db.rawQuery("SELECT * FROM ConnectCategoriesWithTranslates WHERE _categoryId=?", new String [] {cursor.getString(0)});
                cursor1.moveToFirst();
                String translateID=cursor1.getString(2);

                //поиск перевода
                cursor1=db.rawQuery("SELECT *FROM CategoriesTranslations WHERE _categoryTranslationId=?", new String [] {translateID});
                cursor1.moveToFirst();
                categoryStruct.add(new CategoryStruct(cursor.getString(0),cursor.getString(1), cursor1.getString(1)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        else
            categoryStruct=null;
        //создаем адаптер
        categoryAdapter = new CategoryAdapter(this, categoryStruct);
        CategoriesList.setAdapter(categoryAdapter);
    }
}
