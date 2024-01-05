package com.example.everywaffle

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val api: RestAPI
):ViewModel(){
}