package com.sivan.famcard.util

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.res.Resources
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.sivan.famcard.ui.CardTransformState

class Ext {
}

fun View.animateSlide(newWidth: Int, transformToState : CardTransformState = CardTransformState.SHRINK) {
    val slideAnimator = this.width.let {
        ValueAnimator
            .ofInt(it, newWidth)
            .setDuration(500)
    }

    slideAnimator?.addUpdateListener { animation1: ValueAnimator ->
        val value = animation1.animatedValue as Int
        this.layoutParams.width = value
        this.requestLayout()

    }

    val animatorSet = AnimatorSet()
    animatorSet.interpolator = AccelerateDecelerateInterpolator()
    animatorSet.play(slideAnimator)
    animatorSet.start()
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
