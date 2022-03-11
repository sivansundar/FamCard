package com.sivan.famcard.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.sivan.famcard.R
import com.sivan.famcard.data.remote.dto.Card
import com.sivan.famcard.data.remote.dto.CardGroup
import com.sivan.famcard.data.remote.dto.Cta
import com.sivan.famcard.data.remote.dto.Entity
import com.sivan.famcard.databinding.ItemBigDisplayCardBinding
import com.sivan.famcard.databinding.ItemDynamicWidthCardBinding
import com.sivan.famcard.databinding.ItemImageCardBinding
import com.sivan.famcard.databinding.ItemSmallCardWithArrowBinding
import com.sivan.famcard.databinding.ItemSmallDisplayCardBinding
import com.sivan.famcard.util.animateSlide
import com.sivan.famcard.util.dp


enum class CardTransformState {
    EXPAND,
    SHRINK
}

class CardAdapter(
    private val container_id: Int,
    private val designType: CardGroup.DesignType,
    val height: Int = 0,
    val onItemClickListenerWithCta: (List<Cta>) -> Unit,
    val onItemClickListenerWithUrl: (String) -> Unit,
    val onItemClickListenerOnBigCard: (BIG_CARD_CLICK_TYPES, Int) -> Unit
) : ListAdapter<Card, CardAdapter.CardAdapterVH>(PojoDiffCallback()) {

    private var cardTransformState: CardTransformState = CardTransformState.SHRINK


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapterVH {
        var resourceLayout = R.layout.item_big_display_card
        when (designType) {
            CardGroup.DesignType.SMALL_DISPLAY_CARD -> {
                resourceLayout = R.layout.item_small_display_card
            }
            CardGroup.DesignType.BIG_DISPLAY_CARD -> {
                resourceLayout = R.layout.item_big_display_card
            }
            CardGroup.DesignType.DYNAMIC_WIDTH_CARD -> {
                resourceLayout = R.layout.item_dynamic_width_card
            }
            CardGroup.DesignType.IMAGE_CARD -> {
                resourceLayout = R.layout.item_image_card
            }
            CardGroup.DesignType.SMALL_CARD_WITH_ARROW -> {
                resourceLayout = R.layout.item_small_card_with_arrow
            }
        }

        val view = LayoutInflater.from(parent.context).inflate(resourceLayout, parent, false)

        return CardAdapterVH(
            view
        )
    }

    override fun onBindViewHolder(holder: CardAdapter.CardAdapterVH, position: Int) {
        when (designType) {
            CardGroup.DesignType.SMALL_DISPLAY_CARD -> {
                holder.bindSmallCard(getItem(position))

            }
            CardGroup.DesignType.BIG_DISPLAY_CARD -> {

                holder.bindBigCard(getItem(position))

            }
            CardGroup.DesignType.DYNAMIC_WIDTH_CARD -> {
                holder.bindDynamicCard(getItem(position))

            }
            CardGroup.DesignType.IMAGE_CARD -> {
                holder.bindImageCard(getItem(position))

            }
            CardGroup.DesignType.SMALL_CARD_WITH_ARROW -> {
                holder.bindSmallCardWithArrow(getItem(position))

            }
        }
    }

    inner class CardAdapterVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bindSmallCardWithArrow(item: Card?) {
            if (item != null) {
                val binding = ItemSmallCardWithArrowBinding.bind(itemView)
                binding.apply {
                    //Check alignment

                    scTitleText.text =
                        getFormattedText(item.formatted_title.text, item.formatted_title.entities)
                    scDescriptionText.text = getFormattedText(
                        item.formatted_description.text,
                        item.formatted_description.entities
                    )
                    placeholderContactImage.load(item.icon.image_url) {
                        placeholder(R.drawable.placeholder_image)
                        transformations(CircleCropTransformation())
                    }

                    scWithArrowRoot.setOnClickListener {
                        onItemClickListenerWithUrl(item.url)
                    }
                }
            }
        }


        fun bindBigCard(item: Card?) {
            if (item != null) {
                val binding = ItemBigDisplayCardBinding.bind(itemView)
                val cta = item.cta.first()

                binding.apply {
                    bdcRootLayout.setCardBackgroundColor(Color.parseColor(item.bg_color))
                    bdcImage.load(item.bg_image.image_url)
                    bdcActionBtn.apply {
                        text = cta.text
                        setTextColor(Color.parseColor(cta.text_color))
                        setBackgroundColor(Color.parseColor(cta.bg_color))

                        setOnClickListener {
                            if (item.cta.isNotEmpty()) {
                                onItemClickListenerWithCta(
                                    item.cta
                                )
                            }
                        }

                        menuRemind.setOnClickListener {
                            deleteCard(adapterPosition)
                        }
                    }

                    menuDismiss.setOnClickListener {
                        deleteCard(adapterPosition)
                        onItemClickListenerOnBigCard(BIG_CARD_CLICK_TYPES.DISMISS, container_id)
                    }

                    root.setOnLongClickListener {
                        cardTransformState = if (cardTransformState == CardTransformState.SHRINK) {
                            binding.bdcOptionsLayout.animateSlide(
                                binding.root.width / 3
                            )
                            CardTransformState.EXPAND
                        } else {
                            binding.bdcOptionsLayout.animateSlide(0)
                            CardTransformState.SHRINK
                        }
                        true
                    }

                    bdcTitleText.text =
                        getFormattedText(item.formatted_title.text, item.formatted_title.entities)
                    bdcDescriptionText.text = getFormattedText(
                        item.formatted_description.text,
                        item.formatted_description.entities
                    )
                }
            }
        }

        fun bindDynamicCard(item: Card?) {
            if (item != null) {
                val binding = ItemDynamicWidthCardBinding.bind(itemView)

                binding.root.setOnClickListener {
                    if (item.url.isNotEmpty()) {
                        onItemClickListenerWithUrl(
                            item.url
                        )
                    }
                }

                binding.apply {
                    dwcCard.layoutParams.height = height.dp
                    dynamicCardImage.load(
                        item.bg_image.image_url.toString()
                    )
                }
            }

        }

        fun bindSmallCard(item: Card?) {
            if (item != null) {
                val binding = ItemSmallDisplayCardBinding.bind(itemView)

                binding.apply {
                    smallCardImage.load(item.icon.image_url) {
                        transformations(CircleCropTransformation())
                    }
                    sdcTitleText.text =
                        getFormattedText(
                            item.formatted_title.text,
                            item.formatted_title.entities
                        )
                    root.setOnClickListener {
                        onItemClickListenerWithUrl(item.url)
                    }
                }
            }
        }

        fun bindImageCard(item: Card?) {
            if (item != null) {
                val binding = ItemImageCardBinding.bind(itemView)
                binding.apply {
                    root.setOnClickListener {
                        onItemClickListenerWithUrl(item.url)
                    }
                    icCard.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(item.bg_color)))
                    icImage.load(
                        item.bg_image.image_url
                    )
                }
            }
        }
    }

    private fun getFormattedText(
        text: String,
        entities: List<Entity>
    ): SpannableStringBuilder {
        var result = SpannableStringBuilder()

        if (text.contains("{}")) {
            val text = text.split("{}")
            for (index in entities.indices) {
                val color = Color.parseColor(entities[index].color)
                val spannableString = SpannableString(entities[index].text)
                spannableString.setSpan(
                    ForegroundColorSpan(color),
                    0,
                    entities[index].text.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                result = result.append(text[index]).append(spannableString)
            }
            if (text.last() != result.last().toString()) {
                result.append(text.last())
            }
            result.append()

        } else {
            result = result.append(text)
        }
        return result
    }

    private fun deleteCard(adapterPosition: Int) {
        val currentList = this.currentList.toMutableList()
        val item = getItem(adapterPosition)
        currentList.remove(item)
        this.submitList(currentList)
    }

    companion object {
        class PojoDiffCallback : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(
                oldItem: Card,
                newItem: Card
            ): Boolean {
                return true
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: Card,
                newItem: Card
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

enum class BIG_CARD_CLICK_TYPES {
    REMIND_ME,
    DISMISS
}