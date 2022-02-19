package com.example.studentsurvey

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log



class StudentSurveyViewModel: ViewModel() {

    var yesCount = MutableLiveData(0)
    var noCount = MutableLiveData(0)


    fun increaseYeses() {
        yesCount.value = yesCount.value?.plus(1)
    }

    fun increaseNoes() {
        noCount.value = noCount.value?.plus(1)
    }

    fun clearCounts() {
        yesCount.value = 0
        noCount.value = 0
    }



}