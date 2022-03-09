package com.sivan.famcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.sivan.famcard.databinding.ActivityMainBinding
import com.sivan.famcard.ui.MainViewModel
import com.sivan.famcard.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            mainViewModel.famCardList.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is DataState.Error -> {
                            hideLoading()
                            showErrorLayout()
                            mainViewModel.setRequestStatusMessage(it.message)

                        }
                        is DataState.Loading -> {
                            showLoading()
                        }
                        is DataState.Success -> {
                            binding.swipeToRefreshLayout.isRefreshing = false
                            mainViewModel.setRequestStatusMessage(getString(R.string.get_cards_success_message))
                            hideErrorLayout()
                            hideLoading()
                        }
                    }
                }
        }

        binding.swipeToRefreshLayout.setOnRefreshListener {
            mainViewModel.getFamcards()
        }

        lifecycleScope.launch {
            mainViewModel.requestStatusMessage.collect {
                showSnackBar(it)
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showLoading() {
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressCircular.visibility = View.GONE
    }

    private fun hideErrorLayout() {
        binding.errorLayout.errorLayoutRoot.visibility = View.GONE
    }

    private fun showErrorLayout() {
        binding.errorLayout.errorLayoutRoot.visibility = View.VISIBLE
    }
}