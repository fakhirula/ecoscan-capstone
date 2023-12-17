package com.example.ecoscan.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecoscan.R
import com.example.ecoscan.databinding.ActivityProfileBinding
import com.example.ecoscan.remote.response.ListScansItem
import com.example.ecoscan.ui.main.MainActivity
import com.example.ecoscan.ui.ViewModelFactory
import com.example.ecoscan.ui.home.HomeActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupView()
    }

    private fun setupView() {
        binding.rvScanUser.layoutManager = LinearLayoutManager(this)
    }

    private fun setupAction() {
        viewModel.getSession().observe(this){
            binding.name.setText(it.fullname)
            binding.email.setText(it.email)
            viewModel.set10Scan(it.token)
        }
        binding.btnBack.setOnClickListener{onBackPressed()}
        binding.logout.setOnClickListener{
            viewModel.logout()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.listScan.observe(this){
            setScan(it)
        }
        viewModel.alert.observe(this) {
            showAlert(it.success, it.message)
        }
    }

    private fun setScan(listScan: List<ListScansItem>?) {
        if (listScan != null){
            val adapter=ScanAdapter()
            adapter.submitList(listScan)
            binding.rvScanUser.adapter = adapter
        }
    }

    private fun showAlert(success: Boolean, message: String) {
        if (!success) {
            AlertDialog.Builder(this).apply {
                setTitle(R.string.alertFailure)
                setMessage(message)
                if (message=="Oh no! Your token seems to be invalid. Please check and try again."){
                    setPositiveButton(R.string.login) { dialog, _ ->
                        viewModel.logout()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }else{
                    setPositiveButton(R.string.ButtonAlert) { dialog, _ ->
                        dialog.dismiss()
                    }
                }
            }.show()
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}