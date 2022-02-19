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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


private const val TAG = "RESULTSY_YES_NO_COUNTS"
// const to send back whether or not results are desired by user to be erased or continued
const val EXTRA_ERASE_COUNTS = "com.example.studentsurvey.ERASE_COUNTS"


class ResultsFragment : Fragment() {

    private lateinit var yesCounterTextView: TextView
    private lateinit var noCounterTextView: TextView
    private lateinit var resetButton: Button
    private lateinit var continueButton: Button

    // again important to note that loading the viewModel that ViewModelProvider needs
    // the requireActivity() function as an argument
    private val studentSurveyViewModel: StudentSurveyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(StudentSurveyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_results, container, false)

        // taken from previous results activity
        yesCounterTextView = view.findViewById(R.id.yes_counter)
        noCounterTextView = view.findViewById(R.id.no_counter)
        resetButton = view.findViewById(R.id.reset_button)
        continueButton = view.findViewById(R.id.continue_button)

        showResults()

        resetButton.setOnClickListener {
            // clear results
            keepResults(false)
        }

        continueButton.setOnClickListener {
            // keep results
            keepResults(true)
        }

        return view

    }

    private fun showResults(){
        // getting yes and no counts from view model doesn't work like this
//        val yesCount = studentSurveyViewModel.yesCount.value
//        val noCount = studentSurveyViewModel.noCount.value
        // need to use an observer
        studentSurveyViewModel.yesCount.observe(requireActivity()) {
            yeses -> displayYesCount(yeses)
            Log.d(TAG, "Getting yes count from showResults button ${studentSurveyViewModel.yesCount.value}")

        }

        studentSurveyViewModel.noCount.observe(requireActivity()) {
            noes -> displayNoCount(noes)
        }

    }

    private fun displayYesCount(yesCount: Int) {
        yesCounterTextView.text = yesCount.toString().plus(" yes vote(s)")
        Log.d(TAG, "Getting yes count from displayYesCount button ${studentSurveyViewModel.yesCount.value}")

    }

    private fun displayNoCount(noCount: Int) {
        noCounterTextView.text = noCount.toString().plus(" no vote(s)")
    }


    private fun keepResults(doKeepResults: Boolean) {
        if (!doKeepResults) {
            // view model functions still working as expected but need to send the signal to
            // mainactivity to convey that the survey fragment needs to launch and that it needs to
                // be aware of the new count status (i.e. keep/continue results or erase/reset results)
            studentSurveyViewModel.clearCounts()
            parentFragmentManager.setFragmentResult(EXTRA_ERASE_COUNTS, Bundle.EMPTY)
        } else {
            // if the user presses the continue button
            parentFragmentManager.setFragmentResult(EXTRA_ERASE_COUNTS, Bundle.EMPTY)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = ResultsFragment()
    }
}