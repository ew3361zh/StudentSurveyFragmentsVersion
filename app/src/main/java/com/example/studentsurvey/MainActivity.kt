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


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // first fragment manager to listen for when the send counts is triggered
        // by the getresults button in the survey fragment
        supportFragmentManager.setFragmentResultListener(EXTRA_SEND_COUNTS, this) {
                requestKey, bundle ->
            // do something when the red fragment result is received (choices below)
            // bc we're doing lots of swapping and they don't use much in the way of resources, we're going with option 1
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, ResultsFragment.newInstance())
                .addToBackStack("RESULTS") // when you go back from the current fragment, your app will go to the previous fragment
                .commit()

        }

        // put a second supportfragmentmanager to allow for the
        // reset/continue buttons to trigger going back to the survey fragment
        supportFragmentManager.setFragmentResultListener(EXTRA_ERASE_COUNTS, this) {
            requestKey, bundle ->

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, SurveyFragment.newInstance())
                .addToBackStack("SURVEY")
                .commit()
        }

    }

}