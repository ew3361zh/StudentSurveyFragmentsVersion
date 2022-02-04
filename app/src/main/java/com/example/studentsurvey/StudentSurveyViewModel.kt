package com.example.studentsurvey

import androidx.lifecycle.ViewModel

class StudentSurveyViewModel: ViewModel() {

    var yesCount = 0
    var noCount = 0

    fun increaseYeses() {
        yesCount += 1
    }

    fun increaseNoes() {
        noCount += 1
    }

    fun clearCounts() {
        yesCount = 0
        noCount = 0
    }


}