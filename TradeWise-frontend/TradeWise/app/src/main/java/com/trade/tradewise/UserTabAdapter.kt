package com.trade.tradewise

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.trade.tradewise.frgement.ChangePasswordFrgement
import com.trade.tradewise.frgement.SubscriptionCardFragment
import com.trade.tradewise.frgement.UserProfileFragment

class UserTabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentMap = mutableMapOf<Int, Fragment>()

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> UserProfileFragment()
            1 -> ChangePasswordFrgement()
            2 -> SubscriptionCardFragment()
            else -> UserProfileFragment()
        }
        fragmentMap[position] = fragment
        return fragment
    }
    fun getFragmentAt(position: Int): Fragment? {
        return fragmentMap[position]
    }
}
