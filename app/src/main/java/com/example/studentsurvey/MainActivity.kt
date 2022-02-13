package com.example.studentsurvey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

// create constants for bundled data - commented out because no longer displaying results data on MainActivity
//const val CLOSING_YES_COUNT_KEY = "closing-yes-count-key"
//const val CLOSING_NO_COUNT_KEY = "closing-no-count-key"

// consts to send data from ViewModel and collected via MainActivity activity to SurveyResultsActivity
const val EXTRA_GET_YES_COUNT = "com.example.studentsurvey.YES_COUNT"
const val EXTRA_GET_NO_COUNT = "com.example.studentsurvey.NO_COUNT"

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button
    private lateinit var yesCounterTextView: TextView
    private lateinit var noCounterTextView: TextView
    private lateinit var resultsButton: Button

    // list of questions extracted to string resources
    private val questionBank = listOf(
        R.string.question_1,
        R.string.question_2,
        R.string.question_3,
        R.string.question_4,
        R.string.question_5,
        R.string.question_6,
        R.string.question_7
    )

    // in some other version would want to extract this number to be saved as well
    // so that the spot in the question array wouldn't reset if the activity is destroyed
    private var currentIndex = 0

    // lazy view model
    private val studentSurveyViewModel: StudentSurveyViewModel by lazy {
        ViewModelProvider(this).get(StudentSurveyViewModel::class.java)
    }

    private val surveyResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> handleSurveyResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.question_text)
        yesButton = findViewById(R.id.yes_button)
        noButton = findViewById(R.id.no_button)
        yesCounterTextView = findViewById(R.id.yes_count)
        noCounterTextView = findViewById(R.id.no_count)
        resultsButton = findViewById(R.id.results_button)


        // no longer used same as OnSaveInstanceState because data is no longer being displayed
        // on this page I believe as intended

        // converted saved data to ints just to test out that/how it would work but
        // this would clearly be more easily done converting the saved data to string
//        val savedYesCount = savedInstanceState?.getInt(CLOSING_YES_COUNT_KEY) ?: 0
//        val savedNoCount = savedInstanceState?.getInt(CLOSING_NO_COUNT_KEY) ?: 0


        updateQuestion()

        yesButton.setOnClickListener {
            // taken from geoquiz code which helps ever increasing index to reset at question bank size
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            studentSurveyViewModel.increaseYeses()
            // no longer displaying counter on this screen but might bring that option back so
            // just commenting this out for now
//            yesCounterTextView.text = studentSurveyViewModel.yesCount.toString()
        }

        noButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            studentSurveyViewModel.increaseNoes()
            // no longer displaying counter on this screen but might bring that option back so
            // just commenting this out for now
//            noCounterTextView.text = studentSurveyViewModel.noCount.toString()
        }

        resultsButton.setOnClickListener{
            getResults()
        }

    }

    private fun getResults() {
        Intent(this, SurveyResultsActivity::class.java).apply {
            this.putExtra(EXTRA_GET_YES_COUNT, studentSurveyViewModel.yesCount)
            this.putExtra(EXTRA_GET_NO_COUNT, studentSurveyViewModel.noCount)
            surveyResultLauncher.launch(this)
        }
    }

    // options for displaying results if user presses one or other of buttons on SurveyResultsActivity
    private fun handleSurveyResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val intent = result.data
            val keepResults = intent?.getBooleanExtra(EXTRA_KEEP_RESULTS, true) ?: true
            val message = if (keepResults) {
                getString(R.string.keeping_results_message)
            } else {
                // reset counts to 0 here if user presses reset button in SurveyResultsActivity
                studentSurveyViewModel.clearCounts()
                getString(R.string.clearing_results_message)
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            // result if user presses back button or cancels results page without pressing a button
        } else if (result.resultCode == RESULT_CANCELED) {
            Toast.makeText(this, getString(R.string.canceled_results_message), Toast.LENGTH_SHORT).show()
        }
    }

    // commented this out because it no longer seems needed...data is not displayed in MainActivity
    // currently and when it's sent from MainActivity to SurveyResultsActivity it's coming from
    // the ViewModel
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putInt(CLOSING_YES_COUNT_KEY, studentSurveyViewModel.yesCount)
//        outState.putInt(CLOSING_NO_COUNT_KEY, studentSurveyViewModel.noCount)
//    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex]
        questionTextView.setText(questionTextResId)
    }

}