package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class WordlistActivity extends AppCompatActivity {

    Button AddBtn;
    EditText TxtSearch;
    Spinner ChooseCategory;
    ListView WordsView, TranslatesView;

    ArrayList<String> CategoriesListName, CategoriesListId, WordsListName,
            WordsListId, TranslateLisrname, TranslateListId, PartsName;
    int currentUserId, chooseCategoryIdI;
    String chooseCategoryId="", chooseWordId="";

    //Переменная для работы с БД
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordslist);

        AddBtn =(Button)findViewById(R.id.addBtn);
        TxtSearch = (EditText)findViewById(R.id.txtsearch);
        ChooseCategory = (Spinner)findViewById(R.id.chooseCategory);
        TranslatesView = (ListView)findViewById(R.id.translateview);

        //полготовительные действия перед работой с базой
        Bundle arguments = getIntent().getExtras();
        currentUserId=arguments.getInt("ID");
        //chooseCategoryId=arguments.getString("CategoryID");
        chooseCategoryId="2";
        databaseHelper = new DatabaseHelper(this);
        chooseCategoryIdI=0;

        //сдаптер списка категорий
        createCategroiesList();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CategoriesListName);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        ChooseCategory.setPrompt("Категории");
        if (CategoriesListName!=null) {
            ChooseCategory.setAdapter(adapter);
            // выделяем элемент
            ChooseCategory.setSelection(chooseCategoryIdI);
        }

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                chooseCategoryId=CategoriesListName.get(position);
                createWordsList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        ChooseCategory.setOnItemSelectedListener(itemSelectedListener);

        if (CategoriesListId !=null) {
            createWordsList();
        }

        WordsView = (ListView)findViewById(R.id.wordsview);
        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, WordsListName);
        // устанавливаем для списка адаптер
        if (WordsListName !=null)
            WordsView.setAdapter(adapter);

        // добвляем для списка слушатель
        WordsView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                int i =position;
                ArrayList<String> s=WordsListName;
                chooseWordId=WordsListId.get(position);
                createTranslatessList();
            }
        });
    }

    private void createCategroiesList()
    {
        db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM  Categories WHERE _userId =?", new String[]{Integer.toString(currentUserId)});
        CategoriesListName = new  ArrayList<String>();
        CategoriesListId = new  ArrayList<String>();
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CategoriesListId.add(cursor.getString(0));
                CategoriesListName.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        else {
            CategoriesListName = null;
            CategoriesListId = null;
        }
        cursor.close();
        for (int i = CategoriesListId.size() - 1; i >= 0; i--)
        {
            if (CategoriesListId.get(i).equals(chooseCategoryId))
            {
                chooseCategoryIdI=i;
                break;
            }
        }
    }

    private void createWordsList()
    {
        if (!chooseCategoryId.equals(""))
        {
            WordsListName = new ArrayList<String>();
            WordsListId = new ArrayList<String>();
            PartsName = new ArrayList<String>();
            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM  Words WHERE _userId =? AND _categoryId=?", new String[]{Integer.toString(currentUserId), chooseCategoryId});

            if (cursor.getCount()>0)
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    WordsListId.add(cursor.getString(0));
                    WordsListName.add(cursor.getString(1));
                    PartsName.add(cursor.getString(3));
                    cursor.moveToNext();
                }
            }
            else
            {
                WordsListName = null;
                WordsListId = null;
                PartsName = null;
            }
            cursor.close();
        }
    }
    private void createTranslatessList()
    {
        if (chooseWordId.equals("")) {
            TranslateLisrname = null;
            TranslateListId = null;
        }
        else {
            TranslateLisrname = new ArrayList<String>();
            TranslateListId = new ArrayList<String>();

            ArrayList connect = new ArrayList();

            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM  ConnectWordsWithTranslates WHERE _wordId=?", new String[]{chooseWordId});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Cursor cursor1;
                while (!cursor.isAfterLast()) {
                    cursor1 = db.rawQuery("SELECT * FROM  WordsTranslations WHERE _wordTranslationId=?", new String[]{cursor.getString(2)});
                    TranslateLisrname.add(cursor1.getString(1));
                    TranslateListId.add(cursor1.getString(0));
                }
            }
            cursor.close();
        }
    }
}
