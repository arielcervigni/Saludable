package edu.acervigni.saludable.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import edu.acervigni.saludable.R
import edu.acervigni.saludable.connectionHelper.ConnectionHelper
import edu.acervigni.saludable.model.Usuario
import edu.acervigni.saludable.viewmodel.UsuarioViewModel

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({


            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }, 3000)

    }
}