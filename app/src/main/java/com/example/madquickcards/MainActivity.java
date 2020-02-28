package com.example.madquickcards;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {



    private boolean showing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvQuestion = findViewById(R.id.tvQuestion);
        final TextView tvAnswer = findViewById(R.id.tvAnswer);
        final TextView tvAnswerOne = findViewById(R.id.tvAOne); // correct
        final TextView tvAnswerTwo = findViewById(R.id.tvAOne2);
        final TextView tvAnswerThree = findViewById(R.id.tvAOne3);
        final ImageView ViewToggle = (ImageView) findViewById(R.id.toggle_choices_visibility);


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

        tvAnswerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAnswerOne.setBackgroundColor(getResources().getColor(R.color.Right, null));
            }
        });

        tvAnswerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAnswerOne.setBackgroundColor(getResources().getColor(R.color.Right, null));
                tvAnswerTwo.setBackgroundColor(getResources().getColor(R.color.Wrong, null));

            }
        });

        tvAnswerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAnswerOne.setBackgroundColor(getResources().getColor(R.color.Right, null));
                tvAnswerThree.setBackgroundColor(getResources().getColor(R.color.Wrong, null));

            }
        });

        ViewToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showing)
                {
                    ViewToggle.setImageResource(R.drawable.ic_iconmonstr_eye_8);
                    tvAnswerOne.setVisibility(View.INVISIBLE);
                    tvAnswerTwo.setVisibility(View.INVISIBLE);
                    tvAnswerThree.setVisibility(View.INVISIBLE);
                    showing = false;
                }
                else
                {
                    ViewToggle.setImageResource(R.drawable.ic_iconmonstr_eye_5);
                    tvAnswerOne.setVisibility(View.VISIBLE);
                    tvAnswerTwo.setVisibility(View.VISIBLE);
                    tvAnswerThree.setVisibility(View.VISIBLE);
                    showing = true;
                }
            }
        });

    }




    public void LoadNextCard()
    {
        //TODO
    }

}
