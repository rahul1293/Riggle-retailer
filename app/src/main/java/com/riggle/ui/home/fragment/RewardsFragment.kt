package com.riggle.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.riggle.R
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl

class RewardsFragment  // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    : CustomAppFragmentViewImpl(), CustomAppViewConnector {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        connectViewToParent(this)
        super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun setView(): Int {
        return R.layout.fragment_reward
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        activity?.let {
            ButterKnife.bind(it)
        }

    }

    companion object {
        fun newInstance(): RewardsFragment {
            val fragment = RewardsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}