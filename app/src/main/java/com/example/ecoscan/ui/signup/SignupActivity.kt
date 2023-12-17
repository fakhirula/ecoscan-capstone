package com.example.ecoscan.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.ecoscan.R
import com.example.ecoscan.databinding.ActivitySignupBinding
import com.example.ecoscan.ui.ViewModelFactory
import com.example.ecoscan.ui.home.HomeActivity
import com.example.ecoscan.ui.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupAction()

    }

    private fun setupAction() {
        viewModel.alert.observe(this) {
            showAlert(it.success, it.message)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        binding.btnArrow.setOnClickListener { onBackPressed() }
        binding.btnSignup.setOnClickListener {
            val fullname = binding.fullnameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val city = binding.cityEditText.text.toString()

            viewModel.registerUser(
                fullname,
                email,
                password,
                city
            )
        }
    }

    private fun playAnimation() {
        val icon = ObjectAnimator.ofFloat(binding.imageView2, View.ALPHA, 1f).setDuration(200)
        val header = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(200)
        val deskripsi = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA, 1f).setDuration(200)
        val fullname =
            ObjectAnimator.ofFloat(binding.fullnameEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val username =
            ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val email =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val signup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                icon,
                header,
                deskripsi,
                fullname,
                username,
                email,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }

    private fun showAlert(success: Boolean, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(
                when (success) {
                    true -> R.string.alertSuccess
                    false -> R.string.alertFailure
                }
            )
            setMessage(message)
            setPositiveButton(R.string.login) { dialog, _ ->
                when (success) {
                    true -> {
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    false -> dialog.dismiss()
                }
            }
        }.show()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}