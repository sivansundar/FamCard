package com.sivan.famcard.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sivan.famcard.data.remote.dto.CardGroup
import com.sivan.famcard.data.remote.dto.Cta
import com.sivan.famcard.databinding.LayoutCardRecyclerViewBinding


class CardGroupAdapter(
    private val itemClickListenerWithCta: (List<Cta>) -> Unit,
    private val itemClickListenerWithUrl: (String) -> Unit,
    private val itemClickListenerOnBigCard: (BIG_CARD_CLICK_TYPES) -> Unit
) :
    ListAdapter<CardGroup, CardGroupAdapter.MyCardGroupAdapterHolder>(PojoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCardGroupAdapterHolder {
        return MyCardGroupAdapterHolder(
            LayoutCardRecyclerViewBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ),
        )
    }

    override fun onBindViewHolder(myViewHolder: MyCardGroupAdapterHolder, position: Int) {
        myViewHolder.bind(
            position,
            itemClickListenerWithCta,
            itemClickListenerWithUrl,
            itemClickListenerOnBigCard
        )
    }

    inner class MyCardGroupAdapterHolder(
        private val binding: LayoutCardRecyclerViewBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            itemClickListenerWithCta: (List<Cta>) -> Unit,
            itemClickListenerWithUrl: (String) -> Unit,
            itemClickListenerOnBigCard: (BIG_CARD_CLICK_TYPES) -> Unit
        ) {
            val item = getItem(position)
            val cardAdapter = CardAdapter(
                item.design_type, height = item.height
                    ?: 0,
                itemClickListenerWithCta,
                itemClickListenerWithUrl,
                itemClickListenerOnBigCard
            )


            binding.childCardRv.apply {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

                adapter = cardAdapter
            }

            cardAdapter.submitList(getItem(position).cards)

        }
    }

    companion object {
        class PojoDiffCallback : DiffUtil.ItemCallback<CardGroup>() {
            override fun areItemsTheSame(
                oldItem: CardGroup,
                newItem: CardGroup
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: CardGroup,
                newItem: CardGroup
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}