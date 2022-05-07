package com.example.app1

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_SCORE = "score"
private const val REQUEST_CODE_CHEAT = 0


class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        quizViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.totalScore = savedInstanceState?.getInt(KEY_SCORE, 0) ?: 0
        
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)
        
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
        cheatButton.setOnClickListener{
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        outState.putInt(KEY_SCORE, quizViewModel.totalScore)
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
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }
    private fun incrementQuestion(increment: Int) {
        quizViewModel.incrementIndex(increment)
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    // TODO: Use data binding in string resources to put % right in single Toast message
    // https://stackoverflow.com/questions/52280085/concat-a-localized-string-and-a-dynamic-string-using-databinding-in-kotlin-xml
    private fun checkAnswer(userAnswer: Boolean, view: View) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        quizViewModel.incrementScore(userAnswer==correctAnswer)
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Log.d(TAG, messageResId)
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        val snackMessage = quizViewModel.totalScore.toString() + " out of " + quizViewModel.totalQuestions + " correct"
        val sb = Snackbar.make(view, snackMessage, Snackbar.LENGTH_SHORT)
        sb.show()
    }
}
