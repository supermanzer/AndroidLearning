package com.example.app1

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button

import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat


import androidx.lifecycle.ViewModelProvider



private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_SCORE = "score"
private const val IS_CHEATER = "isCheater"
private const val REQUEST_CODE_CHEAT = 0


class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private val getResult = registerForActivityResult(MyActivityContract()){ result ->
        val tag = "$TAG getResult"
        Log.d(tag, "$result")
        val didCheat = result ?: false
        quizViewModel.incrementCheat(didCheat)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        quizViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.totalScore = savedInstanceState?.getInt(KEY_SCORE, 0) ?: 0
        quizViewModel.isCheater = savedInstanceState?.getBoolean(IS_CHEATER, false) ?: false
        
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
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
        // Wrapping code that runs in a higher API than our minimum in a check so we can still run on older versions of Android
//        @SuppressLint("RestrictedApi")  // <- Not sure why this doesn't apply
        cheatButton.setOnClickListener{ view ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val options = ActivityOptionsCompat.makeClipRevealAnimation(view, 0,0, view.width, view.height)
                getResult.launch(answerIsTrue, options)
            } else {
                getResult.launch(answerIsTrue)
            }

        }
        // Making next button do something
        nextButton.setOnClickListener {
            incrementQuestion(1)
            quizViewModel.isCheater = false
            disableHighlightButtons(false)
        }
        updateQuestion()
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        outState.putInt(KEY_SCORE, quizViewModel.totalScore)
        outState.putBoolean(IS_CHEATER, quizViewModel.isCheater)
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

    private fun checkAnswer(userAnswer: Boolean, view: View) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        quizViewModel.incrementScore(userAnswer==correctAnswer)
        val messageResId = when {
            quizViewModel.tooMuchCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Log.d(TAG, messageResId.toString())
        val toastText = getString(messageResId, quizViewModel.totalScore, quizViewModel.totalQuestions)
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }
}
