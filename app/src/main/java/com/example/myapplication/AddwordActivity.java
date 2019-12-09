package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AddwordActivity extends AppCompatActivity {

    EditText Name;
    Spinner PartsSpinner, CategoriesSpinner;
    Button AddPartBtn, AddWordBtn;
    EditText FirstTranslate;
    ArrayList PartCollection, PartCollectionIds, CategoriesCollection, CategoriesCollectionIds;
    int currentUserId;
    //Переменная для работы с БД
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    ArrayAdapter<String> adapter, adapter1;
    int choosePart, chooseCategiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addword_layout);

        Name = (EditText) findViewById(R.id.inputWordName);
        PartsSpinner = (Spinner)findViewById(R.id.spinner);
        CategoriesSpinner = (Spinner)findViewById(R.id.spinner1);
        AddPartBtn = (Button)findViewById(R.id.addpartBtn);
        AddWordBtn = (Button)findViewById(R.id.addWord);
        FirstTranslate = (EditText) findViewById(R.id.wordTranslate);
        Bundle arguments = getIntent().getExtras();

        //полготовительные действия перед работой с базой
        currentUserId=arguments.getInt("ID");
        databaseHelper = new DatabaseHelper(this);

        createSpinnersLists();

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CategoriesCollection);
        // Определяем разметку для использования при выборе элемента
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        if (CategoriesCollection!=null)
            CategoriesSpinner.setAdapter(adapter1);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PartCollection);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        if (PartCollection != null)
            PartsSpinner.setAdapter(adapter);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.addWord:
                        AddWordToList();
                        break;
                    case R.id.addpartBtn:
                        Intent intent = new Intent(AddwordActivity.this, AddPartSpeechActivity.class);
                        intent.putExtra("ID", currentUserId);
                        startActivity(intent);
                        break;
                }
            }
        };

        AddPartBtn.setOnClickListener(onClickListener);
        AddWordBtn.setOnClickListener(onClickListener);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                choosePart=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        PartsSpinner.setOnItemSelectedListener(itemSelectedListener);

        AdapterView.OnItemSelectedListener itemSelectedListener1 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                chooseCategiry=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        CategoriesSpinner.setOnItemSelectedListener(itemSelectedListener1);
    }

    private void AddWordToList()
    {
        if (Name.getText().toString().equals("") | CategoriesCollection==null | PartCollection==null | FirstTranslate.getText().toString().equals(""))
        {
            Toast toast= Toast.makeText(AddwordActivity.this, "Поля не должны быть пустыми", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            db = databaseHelper.getReadableDatabase();
            String partOfSpeechId = PartCollectionIds.get(choosePart).toString();
            String categoryId = CategoriesCollectionIds.get(chooseCategiry).toString();
            Cursor cursor = db.rawQuery("SELECT * FROM Words WHERE wordName=? AND _userId=? AND _partOfSpeechId=? AND _categoryId=?",
                    new String[]{Name.getText().toString(), Integer.toString(currentUserId), partOfSpeechId, categoryId});

            if (cursor.getCount()==0){
                //добавляем слово
                ContentValues cv = new ContentValues();
                cv.put("wordName", Name.getText().toString()); //назваие категории
                cv.put("_userId", currentUserId); //id пользователя
                cv.put("_partOfSpeechId", Integer.parseInt(partOfSpeechId));
                cv.put("_categoryId", Integer.parseInt(categoryId));
                db.insert("Words", null, cv);
                cursor = db.rawQuery("SELECT * FROM Words WHERE wordName=? AND _userId=? AND _partOfSpeechId=? AND _categoryId=?",
                        new String[]{Name.getText().toString(), Integer.toString(currentUserId), partOfSpeechId, categoryId});
                cursor.moveToFirst();
                int wordId=Integer.parseInt(cursor.getString(0));

                //добавляем перевод
                cv = new ContentValues();
                cv.put("wordTranclationName", FirstTranslate.getText().toString()); //перевод
                cv.put("_userId", currentUserId); //id пользователя
                db.insert("WordsTranslations", null, cv);
                cursor=db.rawQuery("SELECT * FROM WordsTranslations WHERE wordTranclationName=? AND _userId =?",
                        new String[]{FirstTranslate.getText().toString(),Integer.toString(currentUserId)});
                cursor.moveToLast();
                int translateId=Integer.parseInt(cursor.getString(0));
                //int translateId=Integer.parseInt(cursor.getString(0));

                cv = new ContentValues();
                cv.put("_wordId", wordId); //перевод
                cv.put("_wordTranslationId", translateId); //id пользователя
                db.insert("ConnectWordsWithTranslates", null, cv);

                Toast toast= Toast.makeText(AddwordActivity.this, "Слово добавлено", Toast.LENGTH_SHORT);
                toast.show();

                Intent intent = new Intent(AddwordActivity.this, WordlistActivity.class);
                intent.putExtra("ID", currentUserId);
                intent.putExtra("CategoryID", categoryId);
                startActivity(intent);
            }
            else {
                Toast toast= Toast.makeText(AddwordActivity.this, "Такое слово уже существует", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void createSpinnersLists()
    {
        db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PartsOfSpeech WHERE _userId =?", new String[]{Integer.toString(currentUserId)});

        PartCollection=new ArrayList();
        PartCollectionIds=new ArrayList();
        CategoriesCollectionIds=new ArrayList();
        CategoriesCollection=new ArrayList();

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                PartCollectionIds.add(cursor.getString(0));
                PartCollection.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        else
        {
            PartCollection=null;
            PartCollectionIds=null;
        }

        cursor = db.rawQuery("SELECT * FROM  Categories WHERE _userId =?", new String[]{Integer.toString(currentUserId)});

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CategoriesCollectionIds.add(cursor.getString(0));
                CategoriesCollection.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        else{
            CategoriesCollection=null;
            CategoriesCollectionIds=null;
        }
        cursor.close();


    }
}
