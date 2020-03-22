package com.example.simpleapiquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpleapiquiz.data.AnswerListAsyncResponse;
import com.example.simpleapiquiz.data.QuestionBank;
import com.example.simpleapiquiz.model.Questions;
import com.example.simpleapiquiz.model.Score;
import com.example.simpleapiquiz.ui.Pref;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView question_counter;
    private TextView questions;
    private Button trueButton;
    private Button falseButton;
    private ImageButton preButton;
    private ImageButton nextButton;
    private List<Questions> questionsList;
    private TextView scoreTextView;
    private TextView highscore;
    private Pref pref;

    private int currentQuestionIndex=0;
    private int countscore=0;
    private Score score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score=new Score();
        pref=new Pref(MainActivity.this);

        question_counter=findViewById(R.id.ques_index);
        questions=findViewById(R.id.ques);
        trueButton=findViewById(R.id.true_button);
        falseButton=findViewById(R.id.false_button);
        preButton=findViewById(R.id.pre_button);
        nextButton=findViewById(R.id.next_button);
        scoreTextView=findViewById(R.id.scoretextview);
        highscore=findViewById(R.id.highscore);

        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        preButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        scoreTextView.setText("CurrentScore::"+score.getScore());
        pref.saveHighestScore(score.getScore());
        highscore.setText("HighestScore::"+pref.getHighestScore());

        currentQuestionIndex=pref.getIndex();


        questionsList=new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Questions> questionsArrayList) {
                questions.setText(questionsArrayList.get(currentQuestionIndex).getAnswer());
                question_counter.setText((currentQuestionIndex+1) + "/" +questionsArrayList.size());

                Log.d("inside","processfinised"+questionsArrayList);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;
            case R.id.true_button:
                checkAnswer(true);

                updateQuestion();
                break;
            case R.id.pre_button:
                if (currentQuestionIndex>0)
                {
                   currentQuestionIndex=(currentQuestionIndex-1)%questionsList.size();
                   updateQuestion();
                }
                break;
            case R.id.next_button:
              //  pref.saveHighestScore(countscore);
                currentQuestionIndex=(currentQuestionIndex+1)%questionsList.size();
                updateQuestion();
                break;
        }
    }

    private void addPoints()
    {
        countscore+=100;
        score.setScore(countscore);
        scoreTextView.setText("CurrentScore::"+score.getScore());
        Log.d("score","score::"+score.getScore());
    }

    private void reducePoint()
    {
        if (countscore>0)
        {
            countscore-=100;
            score.setScore(countscore);
            scoreTextView.setText("CurrentScore::"+score.getScore());
            Log.d("score","score::"+score.getScore());
        }
        else
        {
            countscore=0;
            score.setScore(countscore);
            scoreTextView.setText("CurrentScore::"+score.getScore());
            Log.d("score","score::"+score.getScore());
        }
    }

    private void checkAnswer(boolean b) {
      boolean answerIsTrue=questionsList.get(currentQuestionIndex).isAnswerTrue();
      String messageToastid;

      if(b==answerIsTrue)
      {
          messageToastid="Correct";
          fadeView();
          addPoints();
      }
      else
      {
          shakeAnimation();
          reducePoint();
          messageToastid="Incorrect";
      }
        Toast.makeText(MainActivity.this,messageToastid,Toast.LENGTH_SHORT).show();

    }

    private void updateQuestion() {
        String question=questionsList.get(currentQuestionIndex).getAnswer();
        questions.setText(question);
        question_counter.setText((currentQuestionIndex+1)+"/"+questionsList.size());
    }

    private void fadeView()
    {
        final CardView cardView=findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(2);

        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                 cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionIndex=(currentQuestionIndex+1)%questionsList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation(){
        Animation shake= AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        final CardView cardView=findViewById(R.id.cardView);

        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionIndex=(currentQuestionIndex+1)%questionsList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onPause() {
        pref.saveHighestScore(score.getScore());
        pref.setcurrentState(currentQuestionIndex);
        super.onPause();
    }
}
