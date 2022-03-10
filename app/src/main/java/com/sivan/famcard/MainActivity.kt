package com.sivan.famcard

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sivan.famcard.data.remote.dto.Cta
import com.sivan.famcard.databinding.ActivityMainBinding
import com.sivan.famcard.ui.BIG_CARD_CLICK_TYPES
import com.sivan.famcard.ui.CardGroupAdapter
import com.sivan.famcard.ui.MainViewModel
import com.sivan.famcard.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mainAdapter: CardGroupAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupContainerRecyclerView()

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
                            mainAdapter.submitList(it.data.card_groups)
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

    private fun setupContainerRecyclerView() {

        val onItemClickListenerWithCta: (List<Cta>) -> Unit = { item ->
            launchItem(item = item.first().url)
        }

        val onItemClickListenerWithUrl: (String) -> Unit = { item ->
            Toast.makeText(this, "Item clicked: ${item}", Toast.LENGTH_SHORT).show()
            launchItem(item)
        }

        val onItemClickListenerOnBigCard: (BIG_CARD_CLICK_TYPES) -> Unit = { item ->


            if (item == BIG_CARD_CLICK_TYPES.DISMISS) {
                /**
                 * Delete the card & add the card id to shared preferences.
                 * When submitting fresh data -> Make sure you filter out all the dismissed items from the shared pref.
                 * */
            }

        }


        mainAdapter = CardGroupAdapter(
            onItemClickListenerWithCta,
            onItemClickListenerWithUrl,
            onItemClickListenerOnBigCard
        )
        binding.cardContainerRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }
    }

    private fun launchItem(item: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(item)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
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