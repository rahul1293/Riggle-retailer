package com.riggle.utils

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.riggle.R
import com.riggle.data.models.response.CoinsEarning
import kotlinx.android.synthetic.main.fragment_cart.*
import java.io.File
import kotlin.math.roundToInt

@BindingAdapter("file")
fun setUriImage(view: ImageView, uri: File?) {
    if (uri != null) {
        Glide.with(view.context).load(uri).apply(RequestOptions().override(200, 200))
            .error(R.drawable.placeholder).into(view)
    } else {
        Glide.with(view.context).load(R.drawable.placeholder).into(view)

    }

}
@BindingAdapter("currency")
fun currency(view: TextView, amount:Double?) {
    view.text = String.format(
        view.context.getString(R.string.rupees_value_double) ?: "",
        amount?.roundToInt()?.toFloat()
    )

}

@BindingAdapter("url")
fun setUriImage(view: ImageView, uri: String?) {
    if (uri != null) {
        Glide.with(view.context).load(uri).apply(RequestOptions().override(200, 200))
            .error(R.drawable.placeholder).into(view)
    } else {
        Glide.with(view.context).load(R.drawable.placeholder).into(view)

    }

}

@BindingAdapter("customBackground")
fun customBackground(view: EditText, isLocked: Boolean) {
    view.isEnabled = !isLocked
    if (isLocked) {
        view.setBackgroundResource(R.drawable.rounded_4dp_border_disabled)

    } else {
        view.setBackgroundResource(R.drawable.rounded_4dp_border_16padding)
    }
}

@BindingAdapter("customCoinsText")
fun customCoinsText(view: TextView, data: CoinsEarning?) {
    data?.let {
        if (data.on_event == "order") {
            view.text = "${data.coins}"
            view.setTextColor(ContextCompat.getColor(view.context, R.color.colorSuccess_60))
        } else {
            if (data.coins > 0) {
                view.text = "${data.coins}"
                view.setTextColor(ContextCompat.getColor(view.context, R.color.colorSuccess_60))
            } else {
                view.text = "-${data.coins}"
                view.setTextColor(ContextCompat.getColor(view.context, R.color.colorError))
            }
        }
    }
}

@BindingAdapter("customRedeemCoinsText")
fun customRedeemCoinsText(view: TextView, data: CoinsEarning?) {
    data?.let {
        view.text = "-${data.redeemed_riggle_coins}"
        view.setTextColor(ContextCompat.getColor(view.context, R.color.colorError))
    }
}

@BindingAdapter("date")
fun date(view: TextView, data: String?) {
    data?.let {
        view.text = Utility.convertDate(data)//data.getDateFromUtc()?.date() ?: ""
    }
}

@BindingAdapter("time")
fun time(view: TextView, data: String?) {
    data?.let {
        view.text = Utility.convertTime(data)//data.getDateFromUtc()?.time() ?: ""
    }
}

@BindingAdapter(value = ["image_uri"])
fun setImageUri(imageView: ImageView, image_url: String?) {
    image_url?.let {
        Glide.with(imageView.context)
            .load(image_url/*.toUri()*/)
            .placeholder(R.drawable.ic_profile_place_holder)
            .into(imageView)
    }
}