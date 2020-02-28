package com.example.madquickcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        final ImageView ivCancel = findViewById(R.id.ivCancel);
        final ImageView ivSave = findViewById(R.id.ivSave);
        final EditText etAnswer = ((EditText)findViewById(R.id.etAnswer));
        final EditText etQuestion = ((EditText)findViewById(R.id.etQuestion));


        etAnswer.setText(getIntent().getStringExtra("correct answer")); // this string will be 'harry potter`
        ((EditText)findViewById(R.id.etWrongAnswer)).setText(getIntent().getStringExtra("wrong answer"));
        ((EditText)findViewById(R.id.etWrongAnswerTwo)).setText(getIntent().getStringExtra("wrong answer two"));
        etQuestion.setText(getIntent().getStringExtra("question"));





        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });


        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (etAnswer.getText().toString().matches("") || etQuestion.getText().toString().matches("") )
                {
                    Toast.makeText(getApplicationContext(), "You can not save a flashcard unless the question and answer feilds are filled out",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Intent data = new Intent(); // create a new Intent, this is where we will put our data
                    data.putExtra("answer",((EditText)findViewById(R.id.etAnswer)).getText().toString()); // puts one string into the Intent, with the key as 'string1'
                    data.putExtra("question", ((EditText)findViewById(R.id.etQuestion)).getText().toString()); // puts another string into the Intent, with the key as 'string2
                    data.putExtra("answerWrong", ((EditText)findViewById(R.id.etWrongAnswer)).getText().toString()); // puts another string into the Intent, with the key as 'string2
                    data.putExtra("answerWrongTwo", ((EditText)findViewById(R.id.etWrongAnswerTwo)).getText().toString()); // puts another string into the Intent, with the key as 'string2
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

    }
}
