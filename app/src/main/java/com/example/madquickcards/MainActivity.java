package com.example.madquickcards;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private static final int EDIT_CARD_REQUEST_CODE = 120;
    private static final int ADD_CARD_REQUEST_CODE = 100;
    private static final int EMPTY_ADD_CARD_REQUEST_CODE = 110;

    private boolean showing = true;
    private FlashcardDatabase flashcardDatabase;
    private List<Flashcard> allFlashcards;
    private int currentCardDisplayedIndex;
    private boolean empty;



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

        if (allFlashcards.size() == 0)
        {
            EnterEmptyState();
        }
        else
        {
            int currentCardDisplayedIndex = getRandomNumber(0, allFlashcards.size() - 1);
            LoadCard(allFlashcards.get(currentCardDisplayedIndex).getQuestion(),
                    allFlashcards.get(currentCardDisplayedIndex).getAnswer(),
                    allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1(),
                    allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
        }


        findViewById(R.id.ivNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // advance our pointer index so we can show the next card
                if (allFlashcards.size() - 1 > 0) {
                    int random_num;
                    do {
                        random_num = getRandomNumber(0, allFlashcards.size() - 1);
                    }
                    while (random_num == currentCardDisplayedIndex);

                    currentCardDisplayedIndex = random_num;
                    LoadCard(allFlashcards.get(currentCardDisplayedIndex).getQuestion(),
                            allFlashcards.get(currentCardDisplayedIndex).getAnswer(),
                            allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1(),
                            allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
                }
            }
        });


        findViewById(R.id.ivTrash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove a card
                flashcardDatabase.deleteCard( tvQuestion.getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();

                // make Index go down and correct if we go to low

                if (allFlashcards.size() > 0) {
                    currentCardDisplayedIndex = getRandomNumber(0, allFlashcards.size() - 1);
                    LoadCard(allFlashcards.get(currentCardDisplayedIndex).getQuestion(),
                            allFlashcards.get(currentCardDisplayedIndex).getAnswer(),
                            allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1(),
                            allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
                } else {
                    EnterEmptyState();
                }
            }
        });


        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.tvQuestion)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.tvAnswer)).setText(allFlashcards.get(0).getAnswer());
            Log.d("MADMain","adding card " + allFlashcards.get(0).getQuestion());
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
                tvQuestion.setVisibility(View.VISIBLE);
                tvAnswer.setVisibility(View.INVISIBLE);
            }
        });

        tvAnswerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAnswerOne.setBackground(getResources().getDrawable(R.drawable.small_answer_correct, null));
            }
        });

        tvAnswerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAnswerOne.setBackground(getResources().getDrawable(R.drawable.small_answer_correct, null));
                tvAnswerTwo.setBackground(getResources().getDrawable(R.drawable.small_answer_wrong, null));

            }
        });

        tvAnswerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAnswerOne.setBackground(getResources().getDrawable(R.drawable.small_answer_correct, null));
                tvAnswerThree.setBackground(getResources().getDrawable(R.drawable.small_answer_wrong, null));

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


                MainActivity.this.startActivityForResult(i, EDIT_CARD_REQUEST_CODE );
            }
        });

        findViewById(R.id.ivEmptyCompose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddCardActivity.class);
                MainActivity.this.startActivityForResult(i, EMPTY_ADD_CARD_REQUEST_CODE);
            }
        });


        ivCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddCardActivity.class);
                MainActivity.this.startActivityForResult(i, ADD_CARD_REQUEST_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) { // this 100 needs to match the 100 we used when we called startActivityForResult!

            /* TODO This should be their for students who are fallowing along.

            ((TextView)findViewById(R.id.tvQuestion)).setText(data.getExtras().getString("question"));
            findViewById(R.id.tvQuestion).setVisibility(View.VISIBLE);// 'string1' needs to match the key we used when we put the string in the Intent
            ((TextView)findViewById(R.id.tvAnswer)).setText(data.getExtras().getString("answer")); // 'string1' needs to match the key we used when we put the string in the Intent
            findViewById(R.id.tvAnswer).setVisibility(View.INVISIBLE);


            // set up multiple choice questions
            ((TextView)findViewById(R.id.tvAOne)).setText(data.getExtras().getString("answer"));
            ((TextView)findViewById(R.id.tvAOne)).setBackgroundResource(R.drawable.small_answer_neutral);
            ((TextView)findViewById(R.id.tvAOne2)).setText(data.getExtras().getString("answerWrong"));
            ((TextView)findViewById(R.id.tvAOne2)).setBackgroundResource(R.drawable.small_answer_neutral);
            ((TextView)findViewById(R.id.tvAOne3)).setText(data.getExtras().getString("answerWrongTwo"));
            ((TextView)findViewById(R.id.tvAOne3)).setBackgroundResource(R.drawable.small_answer_neutral);
            */

            LoadCard(data.getExtras().getString("question"),
                     data.getExtras().getString("answer"),
                     data.getExtras().getString("answerWrong"),
                     data.getExtras().getString("answerWrongTwo"));

            // add to current database
            flashcardDatabase.insertCard(new Flashcard(data.getExtras().getString("question"),
                                                       data.getExtras().getString("answer"),
                                                       data.getExtras().getString("answerWrong"),
                                                       data.getExtras().getString("answerWrongTwo")));
            allFlashcards = flashcardDatabase.getAllCards();


            // Make card made notifaction on bottom of screen
            Snackbar.make(findViewById(R.id.rellay),
                    "Flashcard successfully made!",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
        else if (requestCode == EMPTY_ADD_CARD_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            leaveEmptyState();

            LoadCard(data.getExtras().getString("question"),
                     data.getExtras().getString("answer"),
                     data.getExtras().getString("answerWrong"),
                     data.getExtras().getString("answerWrongTwo"));

            // add to current database
            flashcardDatabase.insertCard(new Flashcard(data.getExtras().getString("question"),
                                                       data.getExtras().getString("answer"),
                                                       data.getExtras().getString("answerWrong"),
                                                       data.getExtras().getString("answerWrongTwo")));
            allFlashcards = flashcardDatabase.getAllCards();


            // Make card made notifaction on bottom of screen
            Snackbar.make(findViewById(R.id.rellay),
                    "Flashcard successfully made!",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
        else if (requestCode == EDIT_CARD_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            Flashcard cardToEdit = allFlashcards.get(currentCardDisplayedIndex);
            cardToEdit.setQuestion(data.getExtras().getString("question"));
            cardToEdit.setAnswer(data.getExtras().getString("answer"));
            cardToEdit.setWrongAnswer1(data.getExtras().getString("answerWrong"));
            cardToEdit.setWrongAnswer2(data.getExtras().getString("answerWrongTwo"));


            flashcardDatabase.updateCard(cardToEdit);
            allFlashcards = flashcardDatabase.getAllCards();


            LoadCard(data.getExtras().getString("question"),
                    data.getExtras().getString("answer"),
                    data.getExtras().getString("answerWrong"),
                    data.getExtras().getString("answerWrongTwo"));

            // Make card made notifaction on bottom of screen
            Snackbar.make(findViewById(R.id.rellay),
                    "Flashcard successfully edited!",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }




    private void LoadCard(String Question, String Answer, String WrongAnswerOne, String WrongAnswerTwo)
    {
        ((TextView)findViewById(R.id.tvQuestion)).setText(Question);
        findViewById(R.id.tvQuestion).setVisibility(View.VISIBLE);// 'string1' needs to match the key we used when we put the string in the Intent
        ((TextView)findViewById(R.id.tvAnswer)).setText(Answer); // 'string1' needs to match the key we used when we put the string in the Intent
        findViewById(R.id.tvAnswer).setVisibility(View.INVISIBLE);


        // set up multiple choice questions
        ((TextView)findViewById(R.id.tvAOne)).setText(Answer);
        ((TextView)findViewById(R.id.tvAOne)).setBackgroundResource(R.drawable.small_answer_neutral);
        ((TextView)findViewById(R.id.tvAOne2)).setText(WrongAnswerOne);
        ((TextView)findViewById(R.id.tvAOne2)).setBackgroundResource(R.drawable.small_answer_neutral);
        ((TextView)findViewById(R.id.tvAOne3)).setText(WrongAnswerTwo);
        ((TextView)findViewById(R.id.tvAOne3)).setBackgroundResource(R.drawable.small_answer_neutral);
    }
    private void LoadCard(String Question, String Answer)
    {
        ((TextView)findViewById(R.id.tvQuestion)).setText(Question);
        findViewById(R.id.tvQuestion).setVisibility(View.VISIBLE);// 'string1' needs to match the key we used when we put the string in the Intent
        ((TextView)findViewById(R.id.tvAnswer)).setText(Answer); // 'string1' needs to match the key we used when we put the string in the Intent
        findViewById(R.id.tvAnswer).setVisibility(View.INVISIBLE);
    }



    public void EnterEmptyState()
    {
        empty = true;
        findViewById(R.id.ivEmptyCompose).setVisibility(View.VISIBLE);
        findViewById(R.id.ivEmpty).setVisibility(View.VISIBLE);
        findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);

        findViewById(R.id.tvAnswer).setVisibility(View.INVISIBLE);
        findViewById(R.id.tvAOne3).setVisibility(View.INVISIBLE);
        findViewById(R.id.tvAOne).setVisibility(View.INVISIBLE);
        findViewById(R.id.tvAOne2).setVisibility(View.INVISIBLE);
        findViewById(R.id.tvQuestion).setVisibility(View.INVISIBLE);
        findViewById(R.id.toggle_choices_visibility).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivCompose).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivEdit).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivNext).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivTrash).setVisibility(View.INVISIBLE);

    }

    public void leaveEmptyState()
    {
        empty = false;

        findViewById(R.id.ivEmptyCompose).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivEmpty).setVisibility(View.INVISIBLE);
        findViewById(R.id.tvEmpty).setVisibility(View.INVISIBLE);

        findViewById(R.id.tvAnswer).setVisibility(View.INVISIBLE);
        findViewById(R.id.tvAOne3).setVisibility(View.VISIBLE);
        findViewById(R.id.tvAOne).setVisibility(View.VISIBLE);
        findViewById(R.id.tvAOne2).setVisibility(View.VISIBLE);
        findViewById(R.id.tvQuestion).setVisibility(View.VISIBLE);
        findViewById(R.id.toggle_choices_visibility).setVisibility(View.VISIBLE);
        findViewById(R.id.ivCompose).setVisibility(View.VISIBLE);
        findViewById(R.id.ivEdit).setVisibility(View.VISIBLE);
        findViewById(R.id.ivNext).setVisibility(View.VISIBLE);
        findViewById(R.id.ivTrash).setVisibility(View.VISIBLE);


    }


    public int getRandomNumber(int minNumber, int maxNumber) {
        Random rand = new Random();
        return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
    }

    public void LoadNextCard()
    {
        //TODOasdad
    }

}
