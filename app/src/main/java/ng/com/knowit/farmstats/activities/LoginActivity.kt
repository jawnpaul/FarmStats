package ng.com.knowit.farmstats.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputLayout
import ng.com.knowit.farmstats.MainActivity
import ng.com.knowit.farmstats.R
import ng.com.knowit.farmstats.databinding.ActivityLoginBinding
import ng.com.knowit.farmstats.utility.Utils

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)


        val rootView = findViewById<View>(android.R.id.content)

        val textInputLayouts = Utils.findViewsWithType(
            rootView, TextInputLayout::class.java
        )

        binding.loginButton.setOnClickListener {
            var noErrors = true
            for (textInputLayout in textInputLayouts) {
                val editTextString = textInputLayout.editText!!.text.toString()
                if (editTextString.isEmpty()) {
                    textInputLayout.error = resources.getString(R.string.error_string)
                    noErrors = false
                } else {
                    textInputLayout.error = null
                }
            }

            if (noErrors) {
                // All fields are valid!

                login()

            }

        }
    }

    private fun login() {

        if (!validateInput()) {
            onLoginFailed()
            return
        }

        binding.loginButton.isEnabled = false

        val email = binding.emailInputEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (performLogin(email, password)) {

            onLoginSuccess()

        } else {
            onLoginFailed()
        }


    }

    private fun onLoginFailed() {
        Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_LONG).show()

        binding.loginButton.isEnabled = true

    }

    override fun onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true)
    }

    private fun validateInput(): Boolean {
        var valid = true

        val email = binding.emailInputEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailTextInputLayout.error = "Enter a valid email address"
            valid = false
        } else {
            binding.emailInputEditText.error = null
        }

        if (password.isEmpty() || password.length < 6) {
            binding.passwordInputLayout.error = "Password must be greater than 6"
            valid = false
        } else {
            binding.passwordEditText.error = null
        }

        return valid
    }

    private fun performLogin(email: String, password: String): Boolean {

        return email.equals("test@theagromall.com") && password.equals("password")
    }

    private fun onLoginSuccess() {
        binding.loginButton.isEnabled = true
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }
}
