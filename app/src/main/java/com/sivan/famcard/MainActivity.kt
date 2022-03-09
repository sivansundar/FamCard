package com.sivan.famcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.sivan.famcard.ui.MainViewModel
import com.sivan.famcard.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            mainViewModel.getFamcards().collect {
                when (it) {
                    is DataState.Error -> {
                        Timber.d("STATE: ERROR : ${it.exception}")
                    }
                    is DataState.Loading -> {
                        Timber.d("STATE: LOADING")
                    }
                    is DataState.Success -> {
                        Timber.d("STATE: SUCCESS : ${it.data}")
                    }
                }
            }
        }

    }
}