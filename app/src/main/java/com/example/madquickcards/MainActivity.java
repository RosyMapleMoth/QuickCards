package com.example.madquickcards;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.plattysoft.leonids.ParticleSystem;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private static final int EDIT_CARD_REQUEST_CODE = 120;
    private static final int ADD_CARD_REQUEST_CODE = 100;
    private static final int EMPTY_ADD_CARD_REQUEST_CODE = 110;

    private boolean animating = false;
    private boolean showing = true;
    private FlashcardDatabase flashcardDatabase;
    private List<Flashcard> allFlashcards;
    private int currentCardDisplayedIndex;
    private boolean empty;
    private CountDownTimer returnToNormal;
    private ValueAnimator awnOne, awnTwo, awnThree;
    private CountDownTimer countDownTimer;




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


        countDownTimer = new CountDownTimer(16000, 1000) {
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.tvTimer)).setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
            }
        };

        final GradientDrawable backgroundOne = (GradientDrawable) ((TextView) findViewById(R.id.tvAOne)).getBackground();
        awnOne = ValueAnimator.ofObject(new ArgbEvaluator(),
                                        getResources().getColor(R.color.Nutrel,null),
                                        getResources().getColor(R.color.Right,null));
        awnOne.setDuration(350); // milliseconds
        awnOne.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                backgroundOne.setColor((int) animator.getAnimatedValue());
            }
        });

        final GradientDrawable backgroundTwo = (GradientDrawable) ((TextView) findViewById(R.id.tvAOne2)).getBackground();
        awnTwo = ValueAnimator.ofObject(new ArgbEvaluator(),
                                        getResources().getColor(R.color.Nutrel,null),
                                        getResources().getColor(R.color.Wrong,null));
        awnTwo.setDuration(350); // milliseconds
        awnTwo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                backgroundTwo.setColor((int) animator.getAnimatedValue());
            }
        });

        final GradientDrawable backgroundThree = (GradientDrawable) ((TextView) findViewById(R.id.tvAOne3)).getBackground();
        awnThree = ValueAnimator.ofObject(new ArgbEvaluator(),
                                          getResources().getColor(R.color.Nutrel,null),
                                          getResources().getColor(R.color.Wrong,null));
        awnThree.setDuration(350); // milliseconds
        awnThree.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                backgroundThree.setColor((int) animator.getAnimatedValue());
            }
        });




        if (allFlashcards.size() == 0)
        {
            EnterEmptyState();
        }
        else
        {
            currentCardDisplayedIndex = getRandomNumber(0, allFlashcards.size() - 1);
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




                    final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                    final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                    if (tvQuestion.getVisibility() == View.VISIBLE) {

                        leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                int random_num;
                                do {
                                    random_num = getRandomNumber(0, allFlashcards.size() - 1);
                                }
                                while (random_num == currentCardDisplayedIndex);
                                currentCardDisplayedIndex = random_num;
                                findViewById(R.id.tvQuestion).startAnimation(rightInAnim);
                                LoadCard(allFlashcards.get(currentCardDisplayedIndex).getQuestion(),
                                        allFlashcards.get(currentCardDisplayedIndex).getAnswer(),
                                        allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1(),
                                        allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // we don't need to worry about this method
                            }
                        });
                        findViewById(R.id.tvQuestion).startAnimation(leftOutAnim);
                    }
                    else
                    {
                        leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                // this method is called when the animation first starts
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                findViewById(R.id.tvQuestion).startAnimation(rightInAnim);
                                LoadCard(allFlashcards.get(currentCardDisplayedIndex).getQuestion(),
                                        allFlashcards.get(currentCardDisplayedIndex).getAnswer(),
                                        allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1(),
                                        allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // we don't need to worry about this method
                            }
                        });
                        findViewById(R.id.tvAnswer).startAnimation(leftOutAnim);
                    }
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


        findViewById(R.id.tvQuestion).setCameraDistance(25000);
        findViewById(R.id.tvAnswer).setCameraDistance(25000);


        tvQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*
                if (!animating) {
                    animating = true;
                    int cx = tvAnswer.getWidth() / 2;
                    int cy = tvAnswer.getHeight() / 2;
                    tvAnswer.bringToFront();

                    float finalRadius = (float) Math.hypot(cx, cy);

                    Animator anim = ViewAnimationUtils.createCircularReveal(tvAnswer, cx, cy, 0f, finalRadius);


                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            tvQuestion.setVisibility(View.INVISIBLE);
                            animating = false;

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            animating = false;

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    tvAnswer.setVisibility(View.VISIBLE);

                    anim.setDuration(1500);
                    anim.start();
                }*/


                tvQuestion.animate()
                        .rotationY(90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        tvQuestion.setVisibility(View.INVISIBLE);
                                        tvAnswer.setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        tvAnswer.setRotationY(-90);
                                        tvAnswer.animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                    }
                                }
                        ).start();
            }
        });


        tvAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (!animating) {

                    animating = true;
                    int cx = tvAnswer.getWidth() / 2;
                    int cy = tvAnswer.getHeight() / 2;
                    tvQuestion.setVisibility(View.VISIBLE);
                    tvQuestion.bringToFront();

                    float finalRadius = (float) Math.hypot(cx, cy);

                    // create the animator for this view (the start radius is zero)
                    Animator anim = ViewAnimationUtils.createCircularReveal(tvQuestion, cx, cy, 0f, finalRadius);

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            tvAnswer.setVisibility(View.INVISIBLE);
                            animating = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            animating = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    tvQuestion.setVisibility(View.VISIBLE);


                    anim.setDuration(1500);
                    anim.start();
                }*/

                tvAnswer.animate()
                        .rotationY(90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        tvAnswer.setVisibility(View.INVISIBLE);
                                        findViewById(R.id.tvQuestion).setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        findViewById(R.id.tvQuestion).setRotationY(-90);
                                        findViewById(R.id.tvQuestion).animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                    }
                                }
                        ).start();
            }
        });




        tvAnswerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (returnToNormal == null && (awnOne == null || !awnOne.isRunning()))
                {
                    awnOne.start();
                    new ParticleSystem(MainActivity.this, 100, R.drawable.confetti, 3000)
                            .setSpeedRange(0.2f, 0.5f)
                            .oneShot(tvAnswerOne, 100);

                    returnToNormal = new CountDownTimer(3000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            awnOne.reverse();
                            returnToNormal = null;
                        }
                    }.start();
                }
            }
        });



        tvAnswerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnToNormal == null && (awnOne == null || !awnOne.isRunning())) {
                    awnOne.start();
                    awnTwo.start();
                    returnToNormal = new CountDownTimer(3000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            awnOne.reverse();
                            awnTwo.reverse();
                            returnToNormal = null;
                        }
                    }.start();
                }
            }
        });



        tvAnswerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnToNormal == null && (awnOne == null || !awnOne.isRunning())) {
                    awnOne.start();
                    awnThree.start();
                    returnToNormal = new CountDownTimer(3000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            awnOne.reverse();
                            awnThree.reverse();
                            returnToNormal = null;
                        }
                    }.start();
                }
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
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });



        findViewById(R.id.ivEmptyCompose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddCardActivity.class);
                MainActivity.this.startActivityForResult(i, EMPTY_ADD_CARD_REQUEST_CODE);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });



        ivCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddCardActivity.class);
                MainActivity.this.startActivityForResult(i, ADD_CARD_REQUEST_CODE);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);


            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
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



    private void startTimer()
    {
        countDownTimer.cancel();
        countDownTimer.start();
    }





    private void LoadCard(String Question, String Answer, String WrongAnswerOne, String WrongAnswerTwo)
    {
        ((TextView)findViewById(R.id.tvQuestion)).setText(Question);
        findViewById(R.id.tvQuestion).setVisibility(View.VISIBLE);// 'string1' needs to match the key we used when we put the string in the Intent
        ((TextView)findViewById(R.id.tvAnswer)).setText(Answer); // 'string1' needs to match the key we used when we put the string in the Intent
        findViewById(R.id.tvAnswer).setVisibility(View.INVISIBLE);


        if (awnOne.isRunning() || returnToNormal != null)
        {
            awnOne.start();
            awnOne.cancel();
        }
        if (awnTwo.isRunning() || returnToNormal != null)
        {
            awnTwo.start();
            awnTwo.cancel();
        }
        if (awnThree.isRunning() || returnToNormal != null)
        {
            awnThree.start();
            awnThree.cancel();
        }
        if (returnToNormal != null)
        {
            returnToNormal.cancel();
            returnToNormal = null;
        }

        // set up multiple choice questions
        ((TextView)findViewById(R.id.tvAOne)).setText(Answer);
        ((TextView)findViewById(R.id.tvAOne2)).setText(WrongAnswerOne);
        ((TextView)findViewById(R.id.tvAOne3)).setText(WrongAnswerTwo);

        startTimer();
    }



    private void LoadCard(String Question, String Answer)
    {
        ((TextView)findViewById(R.id.tvQuestion)).setText(Question);
        findViewById(R.id.tvQuestion).setVisibility(View.VISIBLE);// 'string1' needs to match the key we used when we put the string in the Intent
        ((TextView)findViewById(R.id.tvAnswer)).setText(Answer); // 'string1' needs to match the key we used when we put the string in the Intent
        findViewById(R.id.tvAnswer).setVisibility(View.INVISIBLE);
    }



    // Enters the empty state by making all things INVISIBLE except for empty state.
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


    // Leaves empty state by making all normal UI elements viable
    public void leaveEmptyState()
    {
        empty = false;

        findViewById(R.id.ivEmptyCompose).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivEmpty).setVisibility(View.INVISIBLE);
        findViewById(R.id.tvEmpty).setVisibility(View.INVISIBLE);

        findViewById(R.id.tvAnswer).setVisibility(View.INVISIBLE);
        findViewById(R.id.tvQuestion).setVisibility(View.VISIBLE);
        findViewById(R.id.toggle_choices_visibility).setVisibility(View.VISIBLE);
        findViewById(R.id.ivCompose).setVisibility(View.VISIBLE);
        findViewById(R.id.ivEdit).setVisibility(View.VISIBLE);
        findViewById(R.id.ivNext).setVisibility(View.VISIBLE);
        findViewById(R.id.ivTrash).setVisibility(View.VISIBLE);


        if (showing)
        {
            findViewById(R.id.tvAOne3).setVisibility(View.VISIBLE);
            findViewById(R.id.tvAOne).setVisibility(View.VISIBLE);
            findViewById(R.id.tvAOne2).setVisibility(View.VISIBLE);
        }
    }



    public void setUpMultipleChoiceQuestion(ValueAnimator awnAnimator, TextView tvCur, Color finalColor)
    {
        final GradientDrawable backgroundOne = (GradientDrawable) tvCur.getBackground();
        awnAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                getResources().getColor(R.color.Nutrel, null),
                getResources().getColor(R.color.Right, null));
        awnOne.setDuration(350); // milliseconds
        awnOne.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                backgroundOne.setColor((int) animator.getAnimatedValue());
            }
        });
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
