package eu.mobile.application.collector

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleOrGone")
    fun View.visibleOrGone(isVisible: Boolean?) {
    visibility = if (isVisible != null)
        if (isVisible) View.VISIBLE else View.GONE
    else
        View.GONE
}