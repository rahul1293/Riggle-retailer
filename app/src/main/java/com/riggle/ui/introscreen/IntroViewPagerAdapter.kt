package com.riggle.ui.introscreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroViewPagerAdapter(
    var mContext: Context,
    var mListScreen: ArrayList<ScreenItem>,
    activity: FragmentActivity
) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return mListScreen.size
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putParcelable("data", mListScreen[position])
        val fragment = IntroFragment()
        fragment.arguments = bundle
        return fragment
    }

    /*  override fun instantiateItem(container: ViewGroup, position: Int): Any {
          val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
          val layoutScreen = inflater.inflate(R.layout.intro_layout_screen, null)
          val imgSlide = layoutScreen.findViewById<ImageView>(R.id.intro_img)
          val title = layoutScreen.findViewById<TextView>(R.id.intro_title)
          val description = layoutScreen.findViewById<TextView>(R.id.intro_description)
          title.setText(mListScreen[position].title)
          description.setText(mListScreen[position].description)
          imgSlide.setImageResource(mListScreen[position].screenImg)
          container.addView(layoutScreen)
          return layoutScreen
      }*/

}

/*

class IntroViewPagerAdapter(var mContext: Context, var mListScreen: List<ScreenItem>) :
    PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen = inflater.inflate(R.layout.intro_layout_screen, null)
        val imgSlide = layoutScreen.findViewById<ImageView>(R.id.intro_img)
        val title = layoutScreen.findViewById<TextView>(R.id.intro_title)
        val description = layoutScreen.findViewById<TextView>(R.id.intro_description)
        title.setText(mListScreen[position].title)
        description.setText(mListScreen[position].description)
        imgSlide.setImageResource(mListScreen[position].screenImg)
        container.addView(layoutScreen)
        return layoutScreen
    }

    override fun getCount(): Int {
        return mListScreen.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}*/
