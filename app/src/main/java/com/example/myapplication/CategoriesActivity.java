package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {
    int currentUserId;
    String currentUserName;
    ListView CategoriesList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryStruct> categoryStruct;
    ArrayAdapter<CategoryStruct> adapter;
    EditText editText;
    Button addBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_layout);
        Bundle arguments = getIntent().getExtras();
        currentUserId = arguments.getInt("ID");
        //currentUserName=arguments.getString("NAME");


        CategoriesList = (ListView) findViewById(R.id.listview);

        databaseHelper = new DatabaseHelper(this);
        InitList();

        CategoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int position, long arg3)
            {
                // по позиции получаем выбранный элемент
                Object selectedItem = categoryAdapter.getItem(position);
            }
        });

        editText = (EditText)findViewById(R.id.txtsearch);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //ничего
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    //сброс listview
                    InitList();
                }
                else {
                    // perform search
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //ничего
            }
        });
    }

    public void AddCategory(View view)
    {
        Intent intent = new Intent(CategoriesActivity.this, EditCategoryActivity.class);
        intent.putExtra("ID", "0");
        intent.putExtra("NAME", "");
        intent.putExtra("TRANSLATE", "");
        intent.putExtra("UserID", currentUserId);
        startActivity(intent);
    }

    public void searchItem(String textToSearch){
        for ( int i = categoryStruct.size() - 1; i >= 0; i--)
        {
            String g=categoryStruct.get(i).getCategoryName();
            if (g.contains(textToSearch)==false) {
                categoryStruct.remove(i);
            }
        }
        categoryAdapter = new CategoryAdapter(this, categoryStruct);
        CategoriesList.setAdapter(categoryAdapter);
        //adapter.notifyDataSetChanged(); //не работает для созданных вручную адаптеров
    }
    public void InitList()
    {
        // открываем подключение
        db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories WHERE _userId=?", new String [] {Integer.toString(currentUserId)});

        Cursor cursor1;
        if (cursor.getCount()>0) {
            categoryStruct = new ArrayList<CategoryStruct>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //поиск id перевода
                cursor1=db.rawQuery("SELECT * FROM ConnectCategoriesWithTranslates WHERE _categoryId=?", new String [] {cursor.getString(0)});
                cursor1.moveToFirst();
                String translateID=cursor1.getString(2);
                String connectId=cursor1.getString(0);

                //поиск перевода
                cursor1=db.rawQuery("SELECT *FROM CategoriesTranslations WHERE _categoryTranslationId=?", new String [] {translateID});
                cursor1.moveToFirst();
                categoryStruct.add(new CategoryStruct(cursor.getString(0),cursor.getString(1), cursor1.getString(1), connectId));
                cursor.moveToNext();
            }
            cursor.close();
            //создаем адаптер
            categoryAdapter = new CategoryAdapter(this, categoryStruct);
            CategoriesList.setAdapter(categoryAdapter);
        }
        else
            categoryStruct=null;
        //создаем адаптер
        //categoryAdapter = new CategoryAdapter(this, categoryStruct);
        //CategoriesList.setAdapter(categoryAdapter);
    }
}
