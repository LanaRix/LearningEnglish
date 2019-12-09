package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditCategoryActivity extends AppCompatActivity {

    TextView titleText;
    EditText name, translate;
    String ID;
    Button button;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    int currentUserId;
    int connectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcategory_layout);

        titleText = (TextView)findViewById(R.id.titleText);
        name = (EditText)findViewById(R.id.name);
        translate = (EditText)findViewById(R.id.translate);
        button = (Button)findViewById(R.id.editOrCreate);
        Bundle arguments = getIntent().getExtras();
        databaseHelper = new DatabaseHelper(this);

        ID = arguments.getString("ID");
        if (ID.equals("0"))
        {
            //добавление нового элемента
            titleText.setText("Добавление категории");
            button.setText("Добавить категорию");
            currentUserId=arguments.getInt("UserID");
            connectId=0;
        }
        else
        {
            //редактирование существующего
            titleText.setText("Изменение категории");
            button.setText("Изменить категорию");
            currentUserId=arguments.getInt("UserID");
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.editOrCreate:
                        if (ID.equals("0"))
                            AddCategory();
                        break;
                }
            }
        };

        button.setOnClickListener(onClickListener);
    }


    private void AddCategory()
    {
        String Name="", Translate="";
        String text = "" + currentUserId;
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //выполлняем запросы на проверку совпадений
        Cursor cursor = db.rawQuery("SELECT * FROM Categories WHERE categoryName=? AND _userId=?", new String [] {name.getText().toString(), text});
        int i=cursor.getCount();
        cursor.moveToFirst();
        if (cursor.getCount()>0)
            Name=cursor.getString(1);
        //выполлняем запросы на проверку совпадений
        cursor = db.rawQuery("SELECT * FROM CategoriesTranslations WHERE categoryTranslationName=? AND _userId=?",
                new String [] {translate.getText().toString(), text});
        cursor.moveToFirst();
        if (cursor.getCount()>0)
            Translate=cursor.getString(1);


        if (Name.equals("") & Translate.equals("")) {
            if (name.getText().toString().equals("") | translate.getText().toString().equals("")) {
                Toast toast= Toast.makeText(EditCategoryActivity.this, "Поля ввода не должны быть пустыми", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                // создаем объект для данных
                ContentValues cv = new ContentValues();
                cv.put("categoryName", name.getText().toString()); //назваие категории
                cv.put("_userId", currentUserId); //id пользователя
                db.insert("Categories", null, cv);

                cursor=db.rawQuery("SELECT * FROM  Categories WHERE categoryName=? AND _userId=?",
                        new String [] {name.getText().toString(), text});
                cursor.moveToFirst();
                String categoryId = cursor.getString(0); //id категории

                cv=new ContentValues();
                cv.put("categoryTranslationName", translate.getText().toString()); //перевод
                cv.put("_userId", currentUserId); //id пользователя
                db.insert("CategoriesTranslations", null, cv);

                cursor=db.rawQuery("SELECT * FROM  CategoriesTranslations WHERE categoryTranslationName=? AND _userId=?",
                        new String [] {translate.getText().toString(), text});
                cursor.moveToFirst();
                String translateId=cursor.getString(0); //id категории

                cv=new ContentValues();
                cv.put("_categoryId", Integer.parseInt(categoryId)); //id категории
                cv.put("_categoryTranslationId", Integer.parseInt(translateId)); //id перевода
                db.insert("ConnectCategoriesWithTranslates", null, cv);
                cursor.close();

                Toast toast= Toast.makeText(EditCategoryActivity.this, "Категория добавлена", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(EditCategoryActivity.this, CategoriesActivity.class);
                intent.putExtra("ID", currentUserId);
                cursor.close();
                // переход к категориям
                startActivity(intent);
            }
        }
        else{
            if (!Name.equals("")) {
                Toast toast = Toast.makeText(EditCategoryActivity.this, "Категория с таким названием уже существует", Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                Toast toast = Toast.makeText(EditCategoryActivity.this, "Перевод с таким названием уже существует", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }
}
