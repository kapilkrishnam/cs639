package com.yuga.merrygifting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    fun login(number: String, password: String): Boolean {
        return loginRepository.login(number, password)
    }
}


class LoginRepository {

    fun login(number: String, password: String): Boolean {
        return number.isNotBlank() && password.isNotBlank() && number == "1234567890" && password == "123456"
    }
}