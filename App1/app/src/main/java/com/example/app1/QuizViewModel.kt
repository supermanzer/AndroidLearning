package com.example.app1

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
private const val MAX_CHEATS = 3

class QuizViewModel : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
        Question(R.string.question_australia, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_oceans, true)
    )
    var currentIndex = 0
    var totalScore = 0
    var isCheater = false
    var totalCheats = 0


    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val percentCorrect: Float
        get() = totalScore.toFloat() / questionBank.size.toFloat() * 100

    val totalQuestions: Int
        get() = questionBank.size

    fun incrementIndex(increment: Int) {
        currentIndex = (currentIndex + increment) % questionBank.size
    }
    fun incrementScore(isCorrect: Boolean) {
        if (isCorrect && !tooMuchCheater) {
            totalScore += 1
        }
    }

    val tooMuchCheater: Boolean
        get() = totalCheats > MAX_CHEATS

    fun incrementCheat(didCheat: Boolean) {
        if (didCheat) {
            totalCheats += 1
        }
    }
}