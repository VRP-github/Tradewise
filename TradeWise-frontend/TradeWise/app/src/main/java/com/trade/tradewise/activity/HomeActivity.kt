package com.trade.tradewise.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.star.starmedicahub.baseactivity.BaseActivity
import com.trade.tradewise.R
import com.trade.tradewise.databinding.ActivityHomeBinding
import com.trade.tradewise.frgement.AddtradFrgement
import com.trade.tradewise.frgement.DashboardFragment
import com.trade.tradewise.frgement.ProtfolimanagerFragment
import com.trade.tradewise.frgement.SettingFrgament
import com.trade.tradewise.frgement.TradeLogFragment

class HomeActivity : BaseActivity() {
    lateinit var binding: ActivityHomeBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            //  enableEdgeToEdge()
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadFragment(TradeLogFragment())
        binding.bottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> loadFragment(DashboardFragment())
                R.id.addTreads -> loadFragment(TradeLogFragment())

                R.id.tradlog -> loadFragment(AddtradFrgement())

                R.id.profile -> loadFragment(ProtfolimanagerFragment())

                R.id.setting -> loadFragment(SettingFrgament())


            }
            true
        }
        binding.btnAnalytic.setOnClickListener {
            var intent= Intent(this,AnyliticsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }


}