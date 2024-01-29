package com.example.searchapi.activity


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.searchapi.fragment.LockerFragment
import com.example.searchapi.R
import com.example.searchapi.fragment.SearchFragment
import com.example.searchapi.adapter.ViewPager2Adapter
import com.example.searchapi.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initViewPager()
    }

    private fun initViewPager() {
        //ViewPager2 Adapter 셋팅
        val viewPager2Adatper = ViewPager2Adapter(this)
        viewPager2Adatper.addFragment(SearchFragment())
        viewPager2Adatper.addFragment(LockerFragment())

        //Adapter 연결
        binding.vpViewpagerMain.apply {
            adapter = viewPager2Adatper
        }
        val iconList = ArrayList<Drawable?>()
        iconList.add(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground))
        iconList.add(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground))


        //ViewPager, TabLayout 연결
        TabLayoutMediator(binding.tlNavigationView, binding.vpViewpagerMain) { tab, position ->
            Log.e("jblee", "ViewPager position: ${position}")
            when (position) {
                0 -> tab.text = "검색"
                1 -> tab.text = "보관함"
            }
            when (position) {
                0 -> tab.icon = iconList[position]
                1 -> tab.icon = iconList[position]
            }
        }.attach()
    }
}