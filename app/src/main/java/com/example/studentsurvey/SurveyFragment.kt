package com.example.studentsurvey

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

// const to send data from ViewModel and collected via MainActivity activity to SurveyResultsActivity
const val EXTRA_SEND_COUNTS = "com.example.studentsurvey.SEND_COUNTS"

private const val TAG = "TRACK_YES_NO_COUNTS"

class SurveyFragment : Fragment() {

    private lateinit var questionTextView: TextView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button
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

    private var currentIndex = 0

    // need to load the view model to allow for the buttons
    // to be able to send updating counts of yeses/noes
    // and to read values of yes/no counts when user comes back from results fragment
    private val studentSurveyViewModel: StudentSurveyViewModel by lazy {

        // changed viewModelProvider argument from this to requireActvity()
        ViewModelProvider(requireActivity()).get(StudentSurveyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_survey, container, false)

        questionTextView = view.findViewById(R.id.question_text)
        yesButton = view.findViewById(R.id.yes_button)
        noButton = view.findViewById(R.id.no_button)
        resultsButton = view.findViewById(R.id.results_button)

        updateQuestion()

        yesButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            studentSurveyViewModel.increaseYeses()
            Log.d(TAG, "Getting yes count from yesButton ${studentSurveyViewModel.yesCount.value}")
        }

        noButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            studentSurveyViewModel.increaseNoes()
        }

        resultsButton.setOnClickListener{
            getResults()
        }

        return view
    }

        private fun updateQuestion() {
            val questionTextResId = questionBank[currentIndex]
            questionTextView.setText(questionTextResId)
        }

        private fun getResults() {
            // switched from original survey app setup of sending the results to the results
            // activity to just a notifier that the results fragment should be launched from main
            // and then the results fragment can get data from the view model

            Log.d(TAG, "Getting yes count from getResults button ${studentSurveyViewModel.yesCount.value}")
            parentFragmentManager.setFragmentResult(EXTRA_SEND_COUNTS, Bundle.EMPTY)

        }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = SurveyFragment()
    }
}