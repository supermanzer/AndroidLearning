package com.example.app1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

const val EXTRA_ANSWER_SHOWN = "com.example.app1.answer_shown"
const val EXTRA_ANSWER_IS_TRUE = "com.example.app1.answer_is_true"
private const val TAG = "CheatActivity"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: MaterialTextView
    private lateinit var showAnswerButton: MaterialButton
    private lateinit var apiVersionTextView: MaterialTextView
    private var answerIsTrue = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        // Reporting Android API level to user
        apiVersionTextView = findViewById(R.id.api_level)
        val apiText = getString(R.string.api_level, Build.VERSION.SDK_INT)
        apiVersionTextView.text = apiText

        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            Log.d(TAG, "Show Button pressed")
            setAnswerShownResult(true)
        }
    }
    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        Log.d("$TAG setAnswer: ", data.toString())
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}

