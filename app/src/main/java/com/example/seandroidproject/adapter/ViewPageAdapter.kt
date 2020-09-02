package com.example.seandroidproject.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.seandroidproject.R
import com.squareup.picasso.Picasso


class ViewPagerAdapter (context: Context, imageUrls: ArrayList<String>) :
    PagerAdapter() {
    private val context: Context
    private val imageUrls: ArrayList<String>
    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        Picasso.get()
            .load(imageUrls[position])
            .fit()
            .centerInside()
            .placeholder(R.drawable.loading)
            .into(imageView)
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    init {
        this.context = context
        this.imageUrls = imageUrls
    }
}
