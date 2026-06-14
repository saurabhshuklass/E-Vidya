package com.example.e_vidya

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        progressBar =
            findViewById(R.id.progressBar)

        sharedPreferences =
            getSharedPreferences(
                "EVidyaPrefs",
                MODE_PRIVATE
            )

        Thread {

            for (i in 0..100) {

                Thread.sleep(25)

                runOnUiThread {
                    progressBar.progress = i
                }
            }

            val rememberMe =
                sharedPreferences.getBoolean(
                    "rememberMe",
                    false
                )

            runOnUiThread {

                if (rememberMe) {

                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )

                } else {

                    startActivity(
                        Intent(
                            this,
                            LoginActivity::class.java
                        )
                    )
                }

                finish()
            }

        }.start()
    }
}
