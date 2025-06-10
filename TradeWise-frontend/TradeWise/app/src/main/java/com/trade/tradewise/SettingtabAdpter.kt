package com.trade.tradewise

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.trade.tradewise.frgement.CommissionFeesFragment
import com.trade.tradewise.frgement.TradeSettingFragment

class SettingtabAdpter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentMap = mutableMapOf<Int, Fragment>()

    override fun getItemCount(): Int = 2 // Only two tabs: Commission & Trade Setting

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> CommissionFeesFragment()
            1 -> TradeSettingFragment()
            else -> Fragment()
        }
        fragmentMap[position] = fragment
        return fragment
    }

    fun getFragmentAt(position: Int): Fragment? {
        return fragmentMap[position]
    }
}
