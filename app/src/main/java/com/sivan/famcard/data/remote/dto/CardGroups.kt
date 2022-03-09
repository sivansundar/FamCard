package com.sivan.famcard.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CardGroups(
    val card_groups : List<CardGroup>
)

data class CardGroup(
    val cards: List<Card>,
    val design_type: DesignType,
    val height: Int?,
    val id: Int,
    val is_scrollable: Boolean,
    val name: String
) {
    enum class DesignType {
        @SerializedName("HC1")
        SMALL_DISPLAY_CARD,

        @SerializedName("HC3")
        BIG_DISPLAY_CARD,

        @SerializedName("HC4")
        CENTER_CARD,

        @SerializedName("HC5")
        IMAGE_CARD,

        @SerializedName("HC6")
        SMALL_CARD_WITH_ARROW
    }
}

data class BgImage(
    val aspect_ratio: Double,
    val image_type: String,
    val image_url: String
)

data class Cta(
    val text: String,
    val bg_color: String,
    val url: String,
    val text_color: String
)

data class FormattedTitle(
    val align: String,
    val entities: List<Entity>,
    val text: String
)

data class FormattedDescription(
    val entities: List<Entity>,
    val text: String
)

data class Entity(
    val text: String,
    val color: String?,
    val url: String?,
    val font_style: FontStyleType
) {
    enum class FontStyleType {
        @SerializedName("underline")
        UNDERLINE,

        @SerializedName("italic")
        ITALIC
    }
}


data class Card(
    val bg_color: String,
    val bg_image: CardImage,
    val bg_gradient : Gradient,
    val cta: List<Cta>,
    val description: String,
    val formatted_description: FormattedDescription,
    val formatted_title: FormattedTitle,
    val icon: CardImage,
    val name: String,
    val title: String,
    val url: String
)


data class CardImage(
    val image_type: String,
    val asset_type: String? = null,
    val image_url: String? = null
)



data class Gradient(
    val colors: List<String>,
    val angle: Int = 0
)