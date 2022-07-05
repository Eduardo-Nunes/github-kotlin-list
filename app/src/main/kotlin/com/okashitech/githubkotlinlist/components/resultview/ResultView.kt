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
        binding.progressBar.changeVisibility(true)
        this.changeVisibility(true)
    }

    fun hideLoading() {
        binding.progressBar.changeVisibility(false)
        this.changeVisibility(false, INVISIBLE)
    }

    private fun showError() {
        this.changeVisibility(true)
        binding.errorGroup.changeVisibility(true)
    }

    fun hideError() {
        binding.errorGroup.changeVisibility(false)
        this.changeVisibility(false, INVISIBLE)
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