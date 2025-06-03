package com.gateopener

import android.annotation.SuppressLint
import androidx.car.app.CarAppService
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator


class GateOpenerService : CarAppService() {
    override fun onCreateSession(): Session {
        return GateOpenerSession()
    }

    @SuppressLint("PrivateResource")
    override fun createHostValidator(): HostValidator {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    }
}