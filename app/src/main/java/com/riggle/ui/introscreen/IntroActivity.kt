package com.riggle.ui.introscreen

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.riggle.R
import com.riggle.ui.other.registration.EnterPhoneActivity
import kotlinx.android.synthetic.main.activity_intro.*
import kotlin.collections.ArrayList

class IntroActivity : AppCompatActivity() {
   // private var screenPager: ViewPager? = null
    var introViewPagerAdapter: IntroViewPagerAdapter? = null
  //  var tabIndicator: TabLayout? = null
    var btnNext: Button? = null
    var position = 0
    var btnGetStarted: Button? = null
    var btnAnim: Animation? = null
    var tvSkip: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_intro)

        // init views
        btnNext = findViewById(R.id.btn_next)
        btnGetStarted = findViewById(R.id.btn_get_started)

        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)
        tvSkip = findViewById(R.id.tv_skip)

        // fill list screen
        val mList: ArrayList<ScreenItem> = ArrayList()
        mList.add(
            ScreenItem(
                R.string.new_onboarding_title1,
                R.string.new_onboarding_text1,
                R.drawable.intro_img2
            )
        )
        mList.add(
            ScreenItem(
                R.string.new_onboarding_title2,
                R.string.new_onboarding_text2,
                R.drawable.intro_img2
            )
        )
        mList.add(
            ScreenItem(
                R.string.new_onboarding_title3,
                R.string.new_onboarding_text3,
                R.drawable.intro_img3
            )
        )

        // setup viewpager
        //screenPager = findViewById(R.id.screen_viewpager)
        introViewPagerAdapter = IntroViewPagerAdapter(this, mList,this)
        screenPager?.adapter = introViewPagerAdapter

        // setup tablayout with viewpager
        TabLayoutMediator(tab_indicator, screenPager) { tab, position ->
            //tab.text = "OBJECT ${(position + 1)}"
        }.attach()

        // next button click Listner
        btnNext?.setOnClickListener(View.OnClickListener {
            position = screenPager?.getCurrentItem()?:0
            if (position < mList.size) {
                position++
                screenPager?.currentItem = position
            }
            if (position == mList.size - 1) { // when we rech to the last screen

                // TODO : show the GETSTARTED Button and hide the indicator and the next button
                loaddLastScreen()
            }
        })

        // tablayout add change listener
        tab_indicator?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == mList.size - 1) {
                    loaddLastScreen()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        // Get Started button click listener
        btnGetStarted?.setOnClickListener(View.OnClickListener {
            EnterPhoneActivity.start(applicationContext)
            finish()
        })

        // skip button click listener
        tvSkip?.setOnClickListener(View.OnClickListener { screenPager?.currentItem = mList.size })
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private fun loaddLastScreen() {
        btnNext?.visibility = View.INVISIBLE
        btnGetStarted?.visibility = View.VISIBLE
        tvSkip?.visibility = View.INVISIBLE
        tab_indicator?.visibility = View.INVISIBLE
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted?.animation = btnAnim
    }
}