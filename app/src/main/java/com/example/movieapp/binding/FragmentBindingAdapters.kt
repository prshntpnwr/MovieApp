package com.example.movieapp.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.movieapp.R
import com.example.movieapp.database.MovieGenre
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {

    @BindingAdapter("viewVisible")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @BindingAdapter(value = ["imageUrl", "cornerRadius"], requireAll = false)
    fun bindImage(imageView: ImageView, url: String?, radius: Int?) {
        val circularProgressDrawable = CircularProgressDrawable(fragment.requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        val glide = Glide.with(fragment.requireContext()).load(url)
        radius?.let { glide.transform(RoundedCorners(radius)) }
/*            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false // on exception we'll return false so placeholder can be set
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    circularProgressDrawable.stop()
                    return true
                }

            })*/
        glide.placeholder(circularProgressDrawable).into(imageView)
    }

    @BindingAdapter("upperString")
    fun bindText(textView: TextView, value: String?) {
        textView.text = value?.toUpperCase()
    }

    @BindingAdapter(value = ["timeStamp", "format", "emptyTxt"], requireAll = true)
    fun bindDateTime(textView: TextView, timeStamp: String?, format: String, emptyTxt: String) {
        if (!timeStamp.isNullOrEmpty()) {
            val format1 = SimpleDateFormat(
                "yyyy-MM-DD", Locale.US
            )
            format1.timeZone = TimeZone.getTimeZone("UTC")
            val mDate = format1.parse(timeStamp)
            val calendar = Calendar.getInstance()
            calendar.time = mDate
            val dayNumberSuffix = getDayNumberSuffix(calendar.get(Calendar.DAY_OF_MONTH))

            val dateFormat =
                SimpleDateFormat(format.replace("dd", "dd'$dayNumberSuffix'"), Locale.ENGLISH)
            textView.text = dateFormat.format(mDate)
        } else
            textView.text = emptyTxt
    }

    @BindingAdapter(value = ["hourMinute"], requireAll = true)
    fun bindHourMinuteText(textView: TextView, timeStamp: Long?) {
        timeStamp ?: return
        val hrMin = textView.context.getString(R.string.hour_minute_abbreviation)
        textView.text =
            hrMin.replace("%h", "${timeStamp.div(60)}").replace("%m", "${timeStamp.rem(60)}")
    }

    private fun getDayNumberSuffix(day: Int): String {
        if (day in 11..13) {
            return "th"
        }
        return when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    @BindingAdapter(value = ["genresList"], requireAll = true)
    fun bindGenres(textView: TextView, list: MutableList<MovieGenre?>?) {
        list?.let {
            val localList: MutableList<String?> = ArrayList()
            it.filterIndexed { _, movieGenres ->
                localList.add(movieGenres?.name)
            }
            textView.text =
                localList.toString().replace("[", "").replace("]", "").replace(",", " | ")
        }
    }

}