package com.example.madquickcards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvQuestion = findViewById(R.id.tvQuestion);
        final TextView tvAnswer = findViewById(R.id.tvAnswer);


        tvQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvQuestion.setVisibility(View.INVISIBLE);
                tvAnswer.setVisibility(View.VISIBLE);
            }
        });
        tvAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadNextCard();
                Toast.makeText(getApplicationContext(),"Switching card",Toast.LENGTH_LONG).show();
            }
        });


    }




    public void LoadNextCard()
    {
        //TODO
    }

}
