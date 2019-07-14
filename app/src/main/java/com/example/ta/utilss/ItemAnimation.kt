package com.example.ta.utilss

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

public class ItemAnimation {

    /* animation type */
    val BOTTOM_UP = 1
    val FADE_IN = 2
    val LEFT_RIGHT = 3
    val RIGHT_LEFT = 4
    val NONE = 0

    /* animation duration */
    private val DURATION_IN_BOTTOM_UP: Long = 150
    private val DURATION_IN_FADE_ID: Long = 500
    private val DURATION_IN_LEFT_RIGHT: Long = 150
    private val DURATION_IN_RIGHT_LEFT: Long = 150

    fun animate(view: View, position: Int, type: Int) {
        when (type) {
            BOTTOM_UP -> animateBottomUp(view, position)

            FADE_IN -> animateFadeIn(view, position)

            LEFT_RIGHT -> animateLeftRight(view, position)

            RIGHT_LEFT -> animateRightLeft(view, position)
        }
    }

    private fun animateBottomUp(view: View, position: Int) {
        var position = position
        val not_first_item = position == -1
        position = position + 1
        view.translationY = (if (not_first_item) 800 else 500).toFloat()
        view.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, if (not_first_item) 800f else 500f, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f)
        animatorTranslateY.setStartDelay(if (not_first_item) 0 else position * DURATION_IN_BOTTOM_UP)
        animatorTranslateY.setDuration((if (not_first_item) 3 else 1) * DURATION_IN_BOTTOM_UP)
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }

    private fun animateFadeIn(view: View, position: Int) {
        var position = position
        val not_first_item = position == -1
        position = position + 1
        view.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 0.5f, 1f)
        ObjectAnimator.ofFloat(view, "alpha", 0f).start()
        animatorAlpha.startDelay = if (not_first_item) DURATION_IN_FADE_ID / 2 else position * DURATION_IN_FADE_ID / 3
        animatorAlpha.duration = DURATION_IN_FADE_ID
        animatorSet.play(animatorAlpha)
        animatorSet.start()
    }

    private fun animateLeftRight(view: View, position: Int) {
        var position = position
        val not_first_item = position == -1
        position = position + 1
        view.translationX = -400f
        view.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateY = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -400f, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f)
        ObjectAnimator.ofFloat(view, "alpha", 0f).start()
        animatorTranslateY.setStartDelay(if (not_first_item) DURATION_IN_LEFT_RIGHT else position * DURATION_IN_LEFT_RIGHT)
        animatorTranslateY.setDuration((if (not_first_item) 2 else 1) * DURATION_IN_LEFT_RIGHT)
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }

    private fun animateRightLeft(view: View, position: Int) {
        var position = position
        val not_first_item = position == -1
        position = position + 1
        view.translationX = view.x + 400
        view.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateY = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, view.x + 400f, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f)
        ObjectAnimator.ofFloat(view, "alpha", 0f).start()
        animatorTranslateY.setStartDelay(if (not_first_item) DURATION_IN_RIGHT_LEFT else position * DURATION_IN_RIGHT_LEFT)
        animatorTranslateY.setDuration((if (not_first_item) 2 else 1) * DURATION_IN_RIGHT_LEFT)
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }

}
