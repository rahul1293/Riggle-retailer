package com.riggle.utils

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.riggle.R
import com.riggle.data.models.response.CoinsEarning
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("file")
fun setUriImage(view: ImageView, uri: File?) {
    if (uri != null) {
        Glide.with(view.context).load(uri).apply(RequestOptions().override(200, 200))
            .error(R.drawable.placeholder).into(view)
    } else {
        Glide.with(view.context).load(R.drawable.placeholder).into(view)

    }

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

@BindingAdapter(value = ["set_date_name"])
fun setDateName(textView: TextView, date: String?) {
    date?.let {
        var nwDate = ""
        //val oldFormat = "yyyy-MM-dd'T'HH:mm:ssz"
        val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
        val newFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(oldFormat)
        try {
            val newDate: Date = sdf.parse(date)
            sdf.applyPattern(newFormat)
            nwDate = sdf.format(newDate)
            textView.text = "Date : $nwDate"
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
}

@BindingAdapter(value = ["set_visibility"])
fun setVisibility(view: View, empty: String?) {
    if (Constants.DataKeys.isDeliver) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter(value = ["set_visibility_two"])
fun setVisibilityTwo(view: View, empty: String?) {
    if (Constants.DataKeys.isDeliver) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter(value = ["logo_images"])
fun setLogoImages(imageView: ImageView, image_url: String?) {
    image_url?.let {
        Glide.with(imageView.context)
            .load(it)
            /*.override(imageView.width, imageView.height)*/
            .placeholder(R.mipmap.ic_launcher)
            .into(imageView)
    }
}
