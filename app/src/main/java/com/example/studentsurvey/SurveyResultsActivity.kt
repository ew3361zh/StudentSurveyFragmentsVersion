package com.example.studentsurvey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

// to send results of whether the reset or continue button was pressed
// to get back to the main screen and decide what happens
const val EXTRA_KEEP_RESULTS = "com.example.studentsurvey.KEEP_RESULTS"

class SurveyResultsActivity : AppCompatActivity() {

    private lateinit var yesCounterTextView: TextView
    private lateinit var noCounterTextView: TextView
    private lateinit var resetButton: Button
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_results)

        yesCounterTextView = findViewById(R.id.yes_counter)
        noCounterTextView = findViewById(R.id.no_counter)
        resetButton = findViewById(R.id.reset_button)
        continueButton = findViewById(R.id.continue_button)

        // get yes and no counts from MainActivity getResults function intent
        val yesCount = intent.getIntExtra(EXTRA_GET_YES_COUNT, 0)
        val noCount = intent.getIntExtra(EXTRA_GET_NO_COUNT, 0)

        // having some confusion over how to turn this into a string resource or
        // if that's appropriate here
        yesCounterTextView.text = yesCount.toString().plus(" yes vote(s)")
        noCounterTextView.text = noCount.toString().plus(" no vote(s)")

        // moved reset button to SurveyResultsActivity
        resetButton.setOnClickListener {
            // clear results, send Intent back to main activity of RESULT_CANCEL(?)
            keepResults(false)
        }

        continueButton.setOnClickListener {
            // keep results, send Intent back to main activity of true/RESULT_OK
            keepResults(true)
        }

    }


    // function to essentially get a result from either the reset or continue
    // button and send an intent back to main activity with the decision made
    // by the user
    private fun keepResults(doKeepResults: Boolean) {
        Intent().apply {
            putExtra(EXTRA_KEEP_RESULTS, doKeepResults)
            setResult(RESULT_OK, this)
            finish()
        }
    }
}






