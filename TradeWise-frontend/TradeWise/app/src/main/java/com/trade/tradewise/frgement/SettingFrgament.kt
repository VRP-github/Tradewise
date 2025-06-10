package com.trade.tradewise.frgement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.trade.tradewise.R
import com.trade.tradewise.SettingtabAdpter
import com.trade.tradewise.UserTabAdapter
import com.trade.tradewise.databinding.FragmentAddtradFrgementBinding
import com.trade.tradewise.databinding.FragmentSettingFrgamentBinding


class SettingFrgament : Fragment() {

    private var _binding: FragmentSettingFrgamentBinding?=null
    private val binding get() = _binding!!
    var userTabMediator: TabLayoutMediator? = null
    var generalTabMediator: TabLayoutMediator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentSettingFrgamentBinding.inflate(layoutInflater, container, false)



        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                val title = tab.text

                Log.d("xxx", "Main tab selected: $position - $title")

                // Always reset the view pager adapter and mediators
                binding.userViewPager.adapter = null
                userTabMediator?.detach()
                generalTabMediator?.detach()

                if (position == 0) {
                    binding.usertabLayout.visibility = View.VISIBLE
                    binding.generaltabLayout.visibility = View.GONE

                    val adapter = UserTabAdapter(activity as FragmentActivity)
                    binding.userViewPager.adapter = adapter

                    userTabMediator = TabLayoutMediator(binding.usertabLayout, binding.userViewPager) { subTab, pos ->
                        subTab.text = when (pos) {
                            0 -> "Profile"
                            1 -> "Security"
                            2 -> "Subscription"
                            else -> ""
                        }
                    }
                    userTabMediator?.attach()

                    binding.usertabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab) {
                            when (tab.position) {
                                0 -> (adapter.getFragmentAt(0) as? UserProfileFragment)?.callApi()
                            }
                        }
                        override fun onTabUnselected(tab: TabLayout.Tab) {}
                        override fun onTabReselected(tab: TabLayout.Tab) {}
                    })
                } else if (position == 1) {
                    binding.usertabLayout.visibility = View.INVISIBLE
                    binding.generaltabLayout.visibility = View.VISIBLE

                    val adapter = SettingtabAdpter(activity as FragmentActivity)
                    binding.userViewPager.adapter = adapter

                    generalTabMediator = TabLayoutMediator(binding.generaltabLayout, binding.userViewPager) { subTab, pos ->
                        subTab.text = when (pos) {
                            0 -> "Commission and Fees"
                            1 -> "Trade Setting"
                            else -> ""
                        }
                    }
                    generalTabMediator?.attach()
                }

                Toast.makeText(activity, "Selected Tab: $title", Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                val title = tab.text
                Toast.makeText(activity, "Reselected Tab: $title", Toast.LENGTH_SHORT).show()
            }
        })

//       binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//           override fun onTabSelected(tab: TabLayout.Tab) {
//               // Called when a tab is selected
//               val position = tab.position
//               val title = tab.text
//               Log.d("xxx",tab.text.toString())
//
//               if (position==0)
//               {
//                   Log.d("xxx",position.toString())
//                   binding.usertabLayout.visibility= View.VISIBLE
//                   binding.generaltabLayout.visibility= View.GONE
//
//                   val adapter = UserTabAdapter(activity as FragmentActivity)
//                   binding.userViewPager.adapter = adapter
//
//                   TabLayoutMediator(binding.usertabLayout, binding.userViewPager) { tab, position ->
//                       tab.text = when (position) {
//                           0 -> "Profile"
//                           1 -> "Security"
//                           2 -> "Subscription"
//                           else -> ""
//                       }
//                   }.attach()
//
//                   binding.usertabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//                       override fun onTabSelected(tab: TabLayout.Tab) {
//                           when (tab.position) {
//                               0 -> (adapter.getFragmentAt(0) as? UserProfileFragment)?.callApi()
//
//                           }
//                       }
//
//                       override fun onTabUnselected(tab: TabLayout.Tab) {}
//                       override fun onTabReselected(tab: TabLayout.Tab) {}
//                   })
//               }
//               else  if(position==1){
//                   Log.d("xxx",position.toString())
//                   binding.usertabLayout.visibility= View.GONE
//                   binding.generaltabLayout.visibility= View.VISIBLE
//                   val adapter = SettingtabAdpter(activity as FragmentActivity)
//                   binding.userViewPager.adapter = adapter
//
//                   TabLayoutMediator(binding.generaltabLayout, binding.userViewPager) { tab, position ->
//                       tab.text = when (position) {
//                           0 -> "Accounts"
//                           1 -> "Comesion and Fess"
//                           2 -> "Trade Setting"
//                           else -> ""
//                       }
//                   }.attach()
//               }
//
//               Toast.makeText(activity, "Selected Tab: $title", Toast.LENGTH_SHORT).show()
//           }
//
//           override fun onTabUnselected(tab: TabLayout.Tab) {
//
//           }
//
//           override fun onTabReselected(tab: TabLayout.Tab) {
//
//               val title = tab.text
//               Toast.makeText(activity, "Reselected Tab: $title", Toast.LENGTH_SHORT).show()
//           }
//       })

        val adapter = UserTabAdapter(activity as FragmentActivity)
        binding.userViewPager.adapter = adapter

        TabLayoutMediator(binding.usertabLayout, binding.userViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Profile"
                1 -> "Security"
                2 -> "Subscription"
                else -> ""
            }
        }.attach()


        return  binding.root
    }


}