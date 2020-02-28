package com.example.madquickcards;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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
        final ImageView ViewToggle = findViewById(R.id.toggle_choices_visibility);
        final ImageView ivCompose = findViewById(R.id.ivCompose);
        final ImageView ivEdit = findViewById(R.id.ivEdit);



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

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddCardActivity.class);
                i.putExtra("question", tvQuestion.getText());
                i.putExtra("answer", tvAnswer.getText());
                i.putExtra("correct answer", tvAnswerOne.getText());
                i.putExtra("wrong answer", tvAnswerTwo.getText());
                i.putExtra("wrong answer two", tvAnswerThree.getText());


                MainActivity.this.startActivityForResult(i, 100);
            }
        });


        ivCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddCardActivity.class);
                MainActivity.this.startActivityForResult(i, 100);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) { // this 100 needs to match the 100 we used when we called startActivityForResult!
            ((TextView)findViewById(R.id.tvQuestion)).setText(data.getExtras().getString("question"));
            findViewById(R.id.tvQuestion).setVisibility(View.VISIBLE);// 'string1' needs to match the key we used when we put the string in the Intent
            ((TextView)findViewById(R.id.tvAnswer)).setText(data.getExtras().getString("answer")); // 'string1' needs to match the key we used when we put the string in the Intent
            ((TextView)findViewById(R.id.tvAOne)).setText(data.getExtras().getString("answer"));
            ((TextView)findViewById(R.id.tvAOne)).setBackgroundResource(R.drawable.card_a_drawable);
            ((TextView)findViewById(R.id.tvAOne2)).setText(data.getExtras().getString("answerWrong"));
            ((TextView)findViewById(R.id.tvAOne2)).setBackgroundResource(R.drawable.card_a_drawable);
            ((TextView)findViewById(R.id.tvAOne3)).setText(data.getExtras().getString("answerWrongTwo"));
            ((TextView)findViewById(R.id.tvAOne3)).setBackgroundResource(R.drawable.card_a_drawable);




            Snackbar.make(findViewById(R.id.rellay),
                    "Flashcard successfully made!",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }



    public void LoadNextCard()
    {
        //TODO
    }

}
