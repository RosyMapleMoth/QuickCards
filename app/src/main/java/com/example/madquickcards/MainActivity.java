package com.example.madquickcards;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {



    private boolean showing = true;
    private FlashcardDatabase flashcardDatabase;
    private List<Flashcard> allFlashcards;
    private int currentCardDisplayedIndex = 0;



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

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();



        findViewById(R.id.ivNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex++;

                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                    currentCardDisplayedIndex = 0;
                }

                // set the question and answer TextViews with data from the database
                ((TextView) findViewById(R.id.tvQuestion)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                ((TextView) findViewById(R.id.tvAnswer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            }
        });


        findViewById(R.id.ivPrev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex--;

                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex < 0) {
                    currentCardDisplayedIndex = allFlashcards.size() - 1;
                }

                // set the question and answer TextViews with data from the database
                ((TextView) findViewById(R.id.tvQuestion)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                ((TextView) findViewById(R.id.tvAnswer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            }
        });


        findViewById(R.id.ivTrash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.tvQuestion)).getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();
Fi
            }
        });


        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.tvQuestion)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.tvAnswer)).setText(allFlashcards.get(0).getAnswer());
            Log.d("MADMain","adding card " + allFlashcards.get(0).getQuestion());
        }
        else
        {
            Log.d("MADMain","unable to add " + (String) allFlashcards.get(0).getQuestion());

        }


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
            findViewById(R.id.tvAnswer).setVisibility(View.INVISIBLE);


            // set up multiple choice questions
            ((TextView)findViewById(R.id.tvAOne)).setText(data.getExtras().getString("answer"));
            ((TextView)findViewById(R.id.tvAOne)).setBackgroundResource(R.drawable.card_a_drawable);
            ((TextView)findViewById(R.id.tvAOne2)).setText(data.getExtras().getString("answerWrong"));
            ((TextView)findViewById(R.id.tvAOne2)).setBackgroundResource(R.drawable.card_a_drawable);
            ((TextView)findViewById(R.id.tvAOne3)).setText(data.getExtras().getString("answerWrongTwo"));
            ((TextView)findViewById(R.id.tvAOne3)).setBackgroundResource(R.drawable.card_a_drawable);


            // add to current database
            flashcardDatabase.insertCard(new Flashcard(data.getExtras().getString("question"), data.getExtras().getString("answer")));
            allFlashcards = flashcardDatabase.getAllCards();



            // Make card made notifaction on bottom of screen
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
