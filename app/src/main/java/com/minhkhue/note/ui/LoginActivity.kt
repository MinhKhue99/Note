package com.minhkhue.note.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.minhkhue.note.databinding.ActivityLoginBinding
import com.minhkhue.note.utils.DataEncryptorModern
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var dataEncryptorModern: DataEncryptorModern

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataEncryptorModern = DataEncryptorModern()
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("password", Context.MODE_PRIVATE)
        val pass = sharedPreferences.getString("pass", "")
        if (pass.equals("")) {
            binding.layoutPassWord.visibility = View.GONE
            binding.cardView.visibility = View.VISIBLE
        }
        if (checkBiometric()) {
            executor = ContextCompat.getMainExecutor(this)
            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        binding.btnLogin.setOnClickListener {
                            if (binding.edtPass.text.toString()
                                    .trim() == dataEncryptorModern.decryptString(pass!!)
                            ) {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                Snackbar.make(
                                    binding.root,
                                    "Password failed",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(
                            applicationContext, "Authentication failed",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()
            biometricPrompt.authenticate(promptInfo)
            binding.ivFingerPrinter.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
            }
        }
    }

    @SuppressLint("SwitchIntDef")
    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkBiometric(): Boolean {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                return true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                return false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                return false
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                startActivityForResult(enrollIntent, REQUEST_CODE)
            }
        }
        return false
    }

    companion object {
        const val REQUEST_CODE = 9669
    }
}