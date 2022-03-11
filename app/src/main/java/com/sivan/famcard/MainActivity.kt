package com.sivan.famcard

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
                            /**
                             * If the network request throws an error, we hide the loading progress indicator,
                             * show the error layout and set the snackbar message with the error message.
                             **/

                            hideLoading()
                            showErrorLayout()
                            mainViewModel.setRequestStatusMessage(it.message)
                        }
                        is DataState.Loading -> {
                            /**
                             * When we are loading the data, appropriate loading state layouts are shown.
                             **/
                            showLoading()
                            hideErrorLayout()
                        }
                        is DataState.Success -> {
                            /**
                             * Here, we filter out the card data against our dismissed list from shared preferences
                             * and only return the data that is needed.
                             * */

                            val dismissedIds =
                                mainViewModel.getDismissedID().map { dismissedId -> dismissedId.id }
                            val finalCardGroups = it.data.card_groups.filterNot { cardGroup ->
                                dismissedIds.contains(cardGroup.id)
                            }

                            binding.swipeToRefreshLayout.isRefreshing = false
                            mainViewModel.setRequestStatusMessage(getString(R.string.get_cards_success_message))
                            mainAdapter.submitList(finalCardGroups)
                            hideErrorLayout()
                            hideLoading()
                        }
                    }
                }
        }

        binding.swipeToRefreshLayout.setOnRefreshListener {
            /**
             * When the layout is being swipped down to refresh, we call the getFamCards() method that queries for fresh data.
             * */
            mainViewModel.getFamcards()
        }

        lifecycleScope.launch {
            mainViewModel.requestStatusMessage.collect {
                showSnackBar(it)
            }
        }
    }

    private fun setupContainerRecyclerView() {

        /**
         * Here, we define all the onClick lambdas that will be triggered for different cases.
         *
         * onItemClickListenerWithCta -> Handles the onClick process for cards with CTAs
         *
         * onItemClickListenerWithUrl -> Handles the onClick process for cards with Url.
         *
         * onItemClickListenerOnBigCard -> Handles the onClick process for big cards. Based on click type (DISMISS, REMIND_ME) we take the necessary actions
         * If the action is dismiss, then we save the ID in our shared preferences.
         * */
        val onItemClickListenerWithCta: (List<Cta>) -> Unit = { item ->
            launchItem(item = item.first().url)
        }

        val onItemClickListenerWithUrl: (String) -> Unit = { item ->
            launchItem(item)
        }

        val onItemClickListenerOnBigCard: (BIG_CARD_CLICK_TYPES, Int) -> Unit = { item, id ->
            if (item == BIG_CARD_CLICK_TYPES.DISMISS) {
                lifecycleScope.launch {
                    mainViewModel.saveId(id)
                }
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