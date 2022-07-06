package com.okashitech.githubkotlinlist.components.resultview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.okashitech.githubkotlinlist.databinding.ResultLayoutBinding
import com.okashitech.githubkotlinlist.extensions.changeVisibility

class ResultView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private var _binding: ResultLayoutBinding? = null
    private val binding get() = _binding!!

    init {
        _binding = ResultLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        changeVisibility(false, INVISIBLE)
    }

    fun showLoading() {
        binding.errorGroup.changeVisibility(false)
        binding.progressBar.changeVisibility(true)
        this.changeVisibility(true)
    }

    fun hideLoading() {
        this.changeVisibility(false, INVISIBLE)
        binding.progressBar.changeVisibility(false)
    }

    private fun showError() {
        binding.progressBar.changeVisibility(false)
        binding.errorGroup.changeVisibility(true)
        this.changeVisibility(true)
    }

    fun hideError() {
        this.changeVisibility(false, INVISIBLE)
        binding.errorGroup.changeVisibility(false)
        binding.tryAgainButton.setOnClickListener(null)
    }

    fun bindError(errorMessage: String, hideOnClick: Boolean = true, resourceCallback: () -> Unit) {
        binding.errorLabel.text = errorMessage
        binding.tryAgainButton.setOnClickListener {
            if (hideOnClick) hideError()
            resourceCallback.invoke()
        }
        showError()
    }

    override fun isShown(): Boolean {
        return binding.rootViewResource.isShown
    }
}