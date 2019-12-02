package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AuthandregistrActivity extends AppCompatActivity {

    Button inputBtn, registrationBtn;
    EditText inputName, inputPassword, createName, createPassword, doubleCreatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authandregistr_layuot);

        //инициализация кнопок
        inputBtn= (Button) findViewById(R.id.inputBtn);
        registrationBtn= (Button) findViewById(R.id.registrationBtn);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.inputBtn:
                        break;
                    case R.id.registrationBtn:
                        break;
                }
            }
        };

    }
}
