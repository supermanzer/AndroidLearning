package com.example.app1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private val questionBank = listOf(
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
        Question(R.string.question_australia, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_oceans, true)
    )
    private var currentIndex = 0
    private var totalScore = 0
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true, view)
            disableHighlightButtons(true)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false, view)
            disableHighlightButtons( true)
        }
        questionTextView.setOnClickListener{
           incrementQuestion(1)
        }
        // Making next button do something
        nextButton.setOnClickListener {
            incrementQuestion(1)
            disableHighlightButtons(false)
        }
        prevButton.setOnClickListener {
            incrementQuestion(-1)
            disableHighlightButtons(false)
        }
        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun disableHighlightButtons(isGuess: Boolean) {
        trueButton.isEnabled = !isGuess
        falseButton.isEnabled = !isGuess
    }
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }
    private fun incrementQuestion(increment: Int) {
        currentIndex = (currentIndex + increment) % questionBank.size
        updateQuestion()
    }
    // TODO: Use data binding in string resources to put % right in single Toast message
    // https://stackoverflow.com/questions/52280085/concat-a-localized-string-and-a-dynamic-string-using-databinding-in-kotlin-xml
    private fun checkAnswer(userAnswer: Boolean, view: View) {
        val correctAnswer = questionBank[currentIndex].answer
        var messageResId = 0
        if (userAnswer == correctAnswer){
            totalScore += 1
            val percentCorrect = totalScore / questionBank.size * 100
            messageResId = R.string.correct_toast
        } else {
            messageResId = R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        val snackMessage = totalScore.toString() + " out of " + questionBank.size + " correct"
        val sb = Snackbar.make(view, snackMessage, Snackbar.LENGTH_SHORT)
        sb.show()
    }
}
