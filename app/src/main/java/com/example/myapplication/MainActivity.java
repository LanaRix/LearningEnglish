package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int currentUserId;
    TextView text;
    Button newWordBtn, exitBtn, openListBtn, learningBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentUserId=0;
        text = (TextView) findViewById(R.id.userString);
        newWordBtn = (Button) findViewById(R.id.newWordBtn);
        exitBtn = (Button) findViewById(R.id.exitBtn);
        openListBtn = (Button) findViewById(R.id.openListBtn);
        learningBtn = (Button) findViewById(R.id.learningBtn);
        Bundle arguments = getIntent().getExtras();
        if (arguments !=null)
        {
            currentUserId=arguments.getInt("ID");
            text.setText(arguments.getString("NAME"));
        }
        else
        {
            currentUserId=0;
            text.setText("Вход не выполнен");
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.newWordBtn:
                        if (currentUserId==0)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("НЕ ВПОЛНЕН ВХОД")
                                    .setMessage("Для выполнения данного действия необходимо авторизоваться в системе. Перейти на странцу авторизации?")
                                    .setCancelable(false)
                                    .setPositiveButton("Да",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent(MainActivity.this, AuthandregistrActivity.class);
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
                            //Intent intent = new Intent(MainActivity.this, AddwordActivity.class);
                            Intent intent = new Intent(MainActivity.this, AddwordActivity.class);
                            intent.putExtra("ID", currentUserId);
                            intent.putExtra("NAME", text.getText().toString());
                            startActivity(intent);
                        }
                        break;
                    case R.id.openListBtn:
                        if (currentUserId==0)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("НЕ ВПОЛНЕН ВХОД")
                                    .setMessage("Для выполнения данного действия необходимо авторизоваться в системе. Перейти на странцу авторизации?")
                                    .setCancelable(false)
                                    .setPositiveButton("Да",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent(MainActivity.this, AuthandregistrActivity.class);
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
                            Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                            intent.putExtra("ID", currentUserId);
                            intent.putExtra("NAME", text.getText().toString());
                            startActivity(intent);
                        }
                        break;
                    case R.id.learningBtn: break;
                    case R.id.exitBtn:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("ЗАКРЫТИЕ ПРИЛОЖЕНИЯ")
                                .setMessage("Вы хотите закрыть приложение?")
                                .setCancelable(false)
                                .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
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
                        break;
                }
            }
        };

        newWordBtn.setOnClickListener(onClickListener);
        openListBtn.setOnClickListener(onClickListener);
        learningBtn.setOnClickListener(onClickListener);
        exitBtn.setOnClickListener(onClickListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.action_autorizregistr:
                Intent intent = new Intent(MainActivity.this, AuthandregistrActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                if (currentUserId==0){
                    Toast toast= Toast.makeText(MainActivity.this, "Вы не вошли в систему", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast= Toast.makeText(MainActivity.this, "Вы вышли из учетной записи", Toast.LENGTH_SHORT);
                    toast.show();
                    currentUserId=0;
                    text.setText("Вход не выполнен");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
