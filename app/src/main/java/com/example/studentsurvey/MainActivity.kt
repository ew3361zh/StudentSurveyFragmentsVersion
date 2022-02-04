package com.example.studentsurvey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

// create constants for bundled data
const val CLOSING_YES_COUNT_KEY = "closing-yes-count-key"
const val CLOSING_NO_COUNT_KEY = "closing-no-count-key"

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button
    private lateinit var yesCounterTextView: TextView
    private lateinit var noCounterTextView: TextView
    private lateinit var resetButton: Button

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.question_text)
        yesButton = findViewById(R.id.yes_button)
        noButton = findViewById(R.id.no_button)
        yesCounterTextView = findViewById(R.id.yes_count)
        noCounterTextView = findViewById(R.id.no_count)
        resetButton = findViewById(R.id.reset_button)

        // converted saved data to ints just to test out that/how it would work but
        // this would clearly be more easily done converting the saved data to string
        val savedYesCount = savedInstanceState?.getInt(CLOSING_YES_COUNT_KEY) ?: 0
        val savedNoCount = savedInstanceState?.getInt(CLOSING_NO_COUNT_KEY) ?: 0

        yesCounterTextView.text = savedYesCount.toString()
        noCounterTextView.text = savedNoCount.toString()

        updateQuestion()

        yesButton.setOnClickListener {
            // taken from geoquiz code which helps ever increasing index to reset at question bank size
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            studentSurveyViewModel.increaseYeses()
            yesCounterTextView.text = studentSurveyViewModel.yesCount.toString()
        }

        noButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            studentSurveyViewModel.increaseNoes()
            noCounterTextView.text = studentSurveyViewModel.noCount.toString()
        }

        resetButton.setOnClickListener {
            studentSurveyViewModel.clearCounts()
            yesCounterTextView.text = studentSurveyViewModel.yesCount.toString()
            noCounterTextView.text = studentSurveyViewModel.noCount.toString()
        }

    }

    // again as noted above, just using this code to see if I could extract the data and save it
    // as ints instead of strings to know that it worked. Is slightly less efficient this way.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CLOSING_YES_COUNT_KEY, yesCounterTextView.text.toString().toInt())
        outState.putInt(CLOSING_NO_COUNT_KEY, noCounterTextView.text.toString().toInt())
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex]
        questionTextView.setText(questionTextResId)
    }

}