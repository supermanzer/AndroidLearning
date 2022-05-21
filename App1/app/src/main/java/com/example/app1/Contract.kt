package com.example.app1

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class MyActivityContract: ActivityResultContract<Boolean, Boolean?>() {

    override fun createIntent(context: Context, input: Boolean): Intent {
        return CheatActivity.newIntent(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean? = when {
        resultCode != Activity.RESULT_OK -> null
        else -> intent?.getBooleanExtra(EXTRA_ANSWER_SHOWN, true)
    }
}