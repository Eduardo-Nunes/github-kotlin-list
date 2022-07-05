package com.okashitech.githubkotlinlist.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.okashitech.githubkotlinlist.R
import com.okashitech.githubkotlinlist.data.model.Repo
import com.okashitech.githubkotlinlist.databinding.FragmentGithubListBinding
import com.okashitech.githubkotlinlist.presentation.details.WebNavArgs
import com.okashitech.githubkotlinlist.presentation.list.adapter.RepoListAdapter
import com.okashitech.githubkotlinlist.presentation.list.adapter.ReposItemsDecorator
import com.okashitech.githubkotlinlist.presentation.list.adapter.ReposLoadStateAdapter
import com.okashitech.githubkotlinlist.utils.GlideApp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GithubListFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentGithubListBinding? = null
    private val binding get() = _binding!!

    private val _viewModel by viewModel<GithubReposViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGithubListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = setupAdapter()
        setupObserver(adapter)
    }

    private fun setupObserver(adapter: RepoListAdapter) {
        lifecycleScope.launch {
            _viewModel.getData()
                .collectLatest { value: PagingData<Repo> ->
                    adapter.submitData(value)
                }
        }
    }

    private fun setupAdapter(): RepoListAdapter {
        val pagingAdapter = RepoListAdapter(GlideApp.with(requireContext()), ::onItemClickListener)
        binding.root.apply {
            setHasFixedSize(true)
            addItemDecoration(ReposItemsDecorator())
            adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = ReposLoadStateAdapter { pagingAdapter.retry() },
                footer = ReposLoadStateAdapter { pagingAdapter.retry() }
            )
        }

        return pagingAdapter
    }

    private fun onItemClickListener(repo: Repo) {
        findNavController().navigate(
            R.id.action_FirstFragment_to_SecondFragment,
            bundleOf(
                getString(R.string.web_view_nav_params) to WebNavArgs(
                    title = repo.fullName,
                    webUrl = repo.url
                )
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}