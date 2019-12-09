package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPartSpeechActivity extends AppCompatActivity {
    Button AddPart;
    EditText Text;
    //Переменная для работы с БД
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpartofspeech);

        Text = (EditText)findViewById(R.id.name);
        databaseHelper = new DatabaseHelper(this);

        AddPart=(Button)findViewById(R.id.AddPart);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.AddPart:
                        AddPartCpeech();
                        break;
                }
            }
        };
        AddPart.setOnClickListener(onClickListener);
    }

    private void AddPartCpeech()
    {
        if (Text.getText().toString().equals("")) {
            Toast toast= Toast.makeText(AddPartSpeechActivity.this, "Название не может быть пустым", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            Bundle arguments = getIntent().getExtras();
            int currentUserId = arguments.getInt("ID");

            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM PartsOfSpeech WHERE _userId =? AND partOfSpeechName=?", new String[]{Integer.toString(currentUserId), Text.getText().toString()});
            if (cursor.getCount()==0)
            {
                // создаем объект для данных
                ContentValues cv = new ContentValues();
                cv.put("partOfSpeechName", Text.getText().toString()); //назваие категории
                cv.put("_userId", currentUserId); //id пользователя
                db.insert("PartsOfSpeech", null, cv);
                cursor.close();
                Toast toast= Toast.makeText(AddPartSpeechActivity.this, "Часть речи добавлена", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(AddPartSpeechActivity.this, AddwordActivity.class);
                intent.putExtra("ID", currentUserId);
                startActivity(intent);
            }
            else
            {
                Toast toast= Toast.makeText(AddPartSpeechActivity.this, "Такая часть речи уже есть в списке", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
