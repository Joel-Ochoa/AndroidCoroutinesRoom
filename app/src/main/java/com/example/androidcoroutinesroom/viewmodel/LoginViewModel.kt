package com.example.androidcoroutinesroom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.androidcoroutinesroom.model.LoginState
import com.example.androidcoroutinesroom.model.User
import com.example.androidcoroutinesroom.model.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db by lazy { UserDatabase(getApplication()).userDao() }

    val loginComplete = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun login(username: String, password: String) {
        coroutineScope.launch {
            val user: User = db.getUser(username)
            val userMatch = user != null && user.passwordHash == password.hashCode()
            if (userMatch) {
                LoginState.login(user)
                withContext(Dispatchers.Main) {
                    loginComplete.value = true
                }
            } else {
                withContext(Dispatchers.Main) {
                    error.value = "Usuario o contrasena incorrectos"
                }
            }

        }
    }
}