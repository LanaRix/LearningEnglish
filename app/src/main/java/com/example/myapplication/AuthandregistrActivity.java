package com.example.myapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AuthandregistrActivity extends AppCompatActivity {

    Button inputBtn, registrationBtn;
    EditText inputName, inputPassword, createName, createPassword, doubleCreatePassword;

    //Переменная для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authandregistr_layuot);

        //полготовительные действия перед работой с базой
        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        //инициализация кнопок
        inputBtn= (Button) findViewById(R.id.inputBtn);
        registrationBtn= (Button) findViewById(R.id.registrationBtn);
        inputName = (EditText) findViewById(R.id.inputName);
        inputPassword=(EditText) findViewById(R.id.inputPassword);
        createName = (EditText) findViewById(R.id.createName);
        createPassword= (EditText) findViewById(R.id.createPassword);
        doubleCreatePassword=(EditText) findViewById(R.id.doubleCreatePassword);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.inputBtn:
                        //Toast toast= Toast.makeText(AuthandregistrActivity.this, "тут", Toast.LENGTH_LONG);
                        //toast.show();
                        String ex="";
                        try {
                            Cursor cursor = mDb.rawQuery("SELECT * FROM Users WHERE userName =?", new String[]{inputName.getText().toString()});
                            cursor.moveToFirst();
                            if (cursor.getCount()>0) {
                                ex = cursor.getString(1);
                                if (cursor.getString(2).equals(inputPassword.getText().toString()))
                                {
                                    // создание объекта Intent для запуска SecondActivity
                                    Intent intent = new Intent(AuthandregistrActivity.this, MainActivity.class);
                                    // передача объекта с ключом "User" и значением user
                                    intent.putExtra("ID", cursor.getInt(0));
                                    intent.putExtra("NAME", cursor.getString(1));
                                    // запуск MainActivity
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast toast= Toast.makeText(AuthandregistrActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                            else {
                                Toast toast = Toast.makeText(AuthandregistrActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            cursor.close();

                        }
                        catch (SQLException mSQLException)
                        {
                            Toast toast= Toast.makeText(AuthandregistrActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        break;
//регистрация
                    case R.id.registrationBtn:
                        if (createName.getText().toString().equals("") && createPassword.getText().toString().equals(""))
                        {
                            Toast toast= Toast.makeText(AuthandregistrActivity.this, "Поля Логин и Пароль не могут быть пустыми", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else
                        {
                            if (createPassword.getText().toString().equals(doubleCreatePassword.getText().toString()))
                            {
                                try
                                {
                                    Cursor cursor = mDb.rawQuery("SELECT * FROM Users WHERE userName =?", new String[]{createName.getText().toString()});
                                    if (cursor.getCount()==0)
                                    {
                                        // открываем подключение
                                        //private DatabaseHelper mDBHelper;
                                        //private SQLiteDatabase mDb;

                                        // создаем объект для данных
                                        ContentValues cv = new ContentValues();
                                        cv.put("userName", createName.getText().toString());
                                        cv.put("userPassword", createPassword.getText().toString());
                                        //db.insert(DatabaseHelper.TABLE, null, cv);

                                        mDb.insert("Users", null, cv);
                                        Toast toast= Toast.makeText(AuthandregistrActivity.this, "Учетная запись создана", Toast.LENGTH_SHORT);
                                        toast.show();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(AuthandregistrActivity.this);
                                        builder.setTitle("ВХОД ПОД СОЗДАННОЙ УЧЕТНОЙ ЗАПИСЬЮ")
                                                .setMessage("Вы хотите зайти под созданной учетной записью?")
                                                .setCancelable(false)
                                                .setPositiveButton("Да",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                Cursor cursor = mDb.rawQuery("SELECT * FROM Users WHERE userName =?", new String[]{createName.getText().toString()});
                                                                cursor.moveToFirst();
                                                                // создание объекта Intent для запуска SecondActivity
                                                                Intent intent = new Intent(AuthandregistrActivity.this, MainActivity.class);
                                                                // передача объекта с ключом "User" и значением user
                                                                intent.putExtra("ID", cursor.getInt(0));
                                                                intent.putExtra("NAME", cursor.getString(1));
                                                                cursor.close();
                                                                // запуск MainActivity
                                                                startActivity(intent);
                                                            }
                                                        })
                                                .setNegativeButton("Нет",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                            }
                                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();

                                    }
                                    else
                                    {
                                        Toast toast= Toast.makeText(AuthandregistrActivity.this, "Такой пользователь уже существует", Toast.LENGTH_SHORT);
                                        toast.show();
                                        cursor.close();
                                    }
                                }
                                catch (SQLException mSQLException)
                                {
                                    Toast toast= Toast.makeText(AuthandregistrActivity.this, "Произошла ошибка при попытке соединиться с базой", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                            else
                            {
                                Toast toast= Toast.makeText(AuthandregistrActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        break;
                }
            }
        };
        inputBtn.setOnClickListener(onClickListener);
        registrationBtn.setOnClickListener(onClickListener);
    }
}
