package com.example.ecoscan.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.ecoscan.R
import com.example.ecoscan.databinding.ActivityLoginBinding
import com.example.ecoscan.ui.ViewModelFactory
import com.example.ecoscan.ui.home.HomeActivity
import com.example.ecoscan.ui.profile.ProfileActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupAction()
    }

    private fun setupAction() {
        binding.btnArrow.setOnClickListener { onBackPressed() }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.alert.observe(this){
            showAlert(it.success, it.message)
        }
        binding.btnLogin.setOnClickListener{
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.loginUser(email,password)
        }
    }

    private fun playAnimation() {
        val icon = ObjectAnimator.ofFloat(binding.imageView2, View.ALPHA, 1f).setDuration(200)
        val header = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(200)
        val deskripsi = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA, 1f).setDuration(200)
        val username =
            ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                icon,
                header,
                deskripsi,
                username,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showAlert(success: Boolean, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(when (success) {
                true -> R.string.alertSuccess
                false -> R.string.alertFailure
            })
            setMessage(message)
            setPositiveButton(R.string.ButtonAlert) { dialog, _ ->
                when (success) {
                    true -> {
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    false -> dialog.dismiss()
                }
            }
        }.show()
    }
}