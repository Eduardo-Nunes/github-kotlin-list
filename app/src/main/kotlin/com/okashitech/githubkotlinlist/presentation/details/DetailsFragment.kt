package com.okashitech.githubkotlinlist.presentation.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.okashitech.githubkotlinlist.R
import com.okashitech.githubkotlinlist.databinding.FragmentDetailsBinding
import com.okashitech.githubkotlinlist.extensions.changeVisibility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val webNavArgs: WebNavArgs by lazy {
        requireArguments().getParcelable(getString(R.string.web_view_nav_params))!!
    }
    private val viewModel: WebViewModel by viewModel {
        parametersOf(webNavArgs)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    private fun initData() {
        with(viewModel) {
            webUrl.observe(viewLifecycleOwner, Observer {
                loadUrl(it.first, it.second)
            })
            loading.observe(viewLifecycleOwner, Observer(::showLoading))
            failure.observe(viewLifecycleOwner, Observer(::showFailure))
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.webView.changeVisibility(false, View.INVISIBLE)
            binding.resultView.showLoading()
        } else {
            binding.webView.changeVisibility(true)
            binding.resultView.hideLoading()
        }
    }

    private fun loadUrl(url: String, webClient: WebViewClient) {
        binding.webView.webViewClient = webClient
        binding.webView.loadUrl(url)
    }

    private fun showFailure(show: Boolean) {
        if (show) {
            binding.webView.changeVisibility(false, View.INVISIBLE)
            binding.resultView.bindError(
                getString(R.string.generic_error),
                false,
                viewModel::retry
            )
        } else {
            binding.resultView.hideError()
        }
    }

    private fun initViews() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = webNavArgs.title
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        with(binding.webView.settings) {
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            javaScriptEnabled = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = false
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
        }
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        binding.webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        binding.webView.scrollBarSize = 0
        binding.webView.isScrollbarFadingEnabled = true
    }

    override fun onDestroyView() {
        binding.webView.clearView()
        binding.webView.clearCache(true)
        binding.webViewRoot.removeAllViewsInLayout()
        super.onDestroyView()
        _binding = null
        viewModel.clearDataBinds()
    }
}