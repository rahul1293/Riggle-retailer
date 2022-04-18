package com.riggle.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.riggle.BuildConfig
import com.riggle.R
import com.riggle.services.eventbus.EventBusEvents.*
import com.riggle.services.eventbus.GlobalBus
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.home.fragment.*
import com.riggle.utils.LogoutUserUtil
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.Subscribe
import org.koin.android.ext.android.inject
import java.util.*

class HomeActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    private val userPreference: UserProfileSingleton by inject()

    private var homePagerAdapter: TabAdapter? = null
    private var homeFragment: HomeFragment? = null
    private var cartFragment: ActiveOrderFragment? = null
    private var profileFragment: ProfileFragment? = null
    private var creditFragment: CreditFragment? = null
    private val rewardsFragment: RewardsFragment? = null
    private var tabPosOnNewActivity = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_home
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        setCrashlyticsUserIdentifier()
        populateBottomTabs()
        setUpTabs()
    }

    private fun populateBottomTabs() {
        homePagerAdapter = TabAdapter(supportFragmentManager, this)
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance()
            homePagerAdapter!!.addFragment(
                homeFragment!!,
                getString(R.string.home),
                R.drawable.ic_home
            )
        }

        if (creditFragment == null) {
            creditFragment = CreditFragment.newInstance()
            homePagerAdapter!!.addFragment(
                creditFragment!!,
                getString(R.string.credit),
                R.drawable.credit_card
            )
        }

        if (cartFragment == null) {
            //cartFragment = CartFragment.newInstance()
            cartFragment = ActiveOrderFragment.newInstance()
            homePagerAdapter!!.addFragment(
                cartFragment!!,
                getString(R.string.active_orders),
                R.drawable.ic_cart
            )
        }
        if (profileFragment == null) {
            profileFragment = ProfileFragment.newInstance()
            homePagerAdapter!!.addFragment(
                profileFragment!!,
                getString(R.string.account),
                R.drawable.ic_profile
            )
        }


        /*if (rewardsFragment == null) {
            rewardsFragment = RewardsFragment.newInstance();
            homePagerAdapter.addFragment(rewardsFragment, getString(R.string.rewards), R.drawable.ic_rewards);
        }*/

        viewPager!!.adapter = homePagerAdapter
        //tabLayout!!.setupWithViewPager(viewPager)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()

        setTabIcon(homePagerAdapter)
    }

    private fun setTabIcon(adapter: TabAdapter?) {
        for (i in 0 until tabLayout!!.tabCount) {
            val tab = tabLayout!!.getTabAt(i)
            tab!!.customView = adapter!!.getTabView(i)
        }
    }

    private fun setUpTabs() {
        highLightCurrentTab(0)
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                highLightCurrentTab(tab.position)
                tabPosOnNewActivity = tab.position
                if (tab.position == 1) {
                    cartFragment!!.loadTab()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        if (intent.getBooleanExtra("is_cart", false)) {
            /*viewPager.currentItem = 1
            highLightCurrentTab(1)*/
        }
    }

    private fun highLightCurrentTab(position: Int) {
        for (tabPosition in 0 until tabLayout!!.tabCount) {
            if (tabLayout!!.getTabAt(tabPosition) != null) {
                val v = tabLayout!!.getTabAt(tabPosition)!!.customView
                val tabTitle = v!!.findViewById<TextView>(R.id.tabTitle)
                val tabIcon: AppCompatImageView = v.findViewById(R.id.tabIcon)
                if (tabPosition == position) {
                    tabTitle.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    tabIcon.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorPrimary),
                        PorterDuff.Mode.SRC_IN
                    )
                } else {
                    tabTitle.setTextColor(ContextCompat.getColor(this, R.color.black_38))
                    tabIcon.setColorFilter(
                        ContextCompat.getColor(this, R.color.black_38),
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }
        }
    }

    private fun setCrashlyticsUserIdentifier() {
        if (!BuildConfig.DEBUG && userPreference.isLogin) {
            FirebaseCrashlytics.getInstance()
                .setUserId("" + userPreference.userId)
            FirebaseCrashlytics.getInstance().setCustomKey(
                "mobile",
                userPreference
                    .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.MOBILE)
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    inner class TabAdapter internal constructor(
        fm: FragmentManager?,
        private val context: Context
    ) : FragmentStateAdapter(this@HomeActivity) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        private val mFragmentIconList: MutableList<Int> = ArrayList()

        fun addFragment(fragment: Fragment, title: String, tabIcon: Int) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
            mFragmentIconList.add(tabIcon)
        }

        fun getTabView(position: Int): View {
            val view = LayoutInflater.from(context).inflate(R.layout.home_custom_tab, null)
            val tabTitle = view.findViewById<TextView>(R.id.tabTitle)
            tabTitle.text = mFragmentTitleList[position]
            val tabIcon: AppCompatImageView = view.findViewById(R.id.tabIcon)
            tabIcon.setImageResource(mFragmentIconList[position])
            return view
        }

        override fun getItemCount(): Int {
            return mFragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return mFragmentList[position]
        }
    }

    @Subscribe
    fun getMessage(logoutUser: LogoutUser) {
        if (logoutUser.isLogout) LogoutUserUtil.logoutUser(this, sharedPreferencesUtil)
    }

    @Subscribe
    fun getMessage(product: UpdateProdOnHome?) {
        if (product != null && homeFragment != null) {
            homeFragment?.itemUpdated(product.productsData)
        }
    }

    @Subscribe
    fun getMessage(orderConfirmed: OnOrderConfirmed) {
        if (orderConfirmed.isConfirmed) {
            cartFragment?.clearCart()
            homeFragment?.refreshData()
            //homeFragment?.totalRiggleCoins
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalBus.bus?.let {
            if (!(it.isRegistered(this))) {
                GlobalBus.bus?.register(this)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalBus.bus?.unregister(this)
    }

    override fun onResume() {
        super.onResume()
    }

    fun onLetsAddClicked(view: View?) {
        viewPager!!.setCurrentItem(0, true)
    }

    companion object {
        @JvmStatic
        fun start(context: Context, isCheck: Boolean) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("is_cart", isCheck)
            context.startActivity(intent)
        }
    }
}