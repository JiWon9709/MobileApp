package com.cookandroid.myloginsns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    Button btnSong, btnCul, btnMap, btnDiary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Button btnSong=(Button)findViewById(R.id.BtnSong);
        Button btnCul=(Button)findViewById(R.id.BtnCul); // 제주도 문화 버튼
        Button btnMap=(Button)findViewById(R.id.BtnMap);
        Button btnDiary=(Button)findViewById(R.id.BtnDiary);

        btnSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Song.class);
                startActivity(intent);
                finish();
            }
        });
        btnCul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Culture.class);
                startActivity(intent);
                finish();
            }
        });
        btnDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Diary.class);
                startActivity(intent);
                finish();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}