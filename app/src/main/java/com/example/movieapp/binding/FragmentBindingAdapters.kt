package com.example.movieapp.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.movieapp.database.MovieGenre
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment)  {

    @BindingAdapter("viewVisible")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        val originalUrl = "https://image.tmdb.org/t/p/w500$url"
        val circularProgressDrawable = CircularProgressDrawable(fragment.requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(fragment.context).load(originalUrl).placeholder(circularProgressDrawable).into(imageView)
    }

    @BindingAdapter("upperString")
    fun bindText(textView: TextView, value: String?){
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

            val dateFormat = SimpleDateFormat(format.replace("dd","dd'${dayNumberSuffix}'"), Locale.ENGLISH)
            textView.text = dateFormat.format(mDate)
        } else
            textView.text = emptyTxt
    }

    private fun getDayNumberSuffix(day: Int): String {
        if (day in 11..13) {
            return "th"
        }
        when (day % 10) {
            1 -> return "st"
            2 -> return "nd"
            3 -> return "rd"
            else -> return "th"
        }
    }

    @BindingAdapter(value = ["genresList"], requireAll = true)
    fun bindGenres(textView: TextView, list: MutableList<MovieGenre?>?){
        list?.let {
            val localList: MutableList<String?> = ArrayList()
            it.filterIndexed { index, movieGenres ->
                localList.add(movieGenres?.name)
            }
            textView.text = localList.toString().replace("[","").replace("]","")
        }
    }

}