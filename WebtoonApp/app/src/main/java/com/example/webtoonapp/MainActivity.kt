package com.example.webtoonapp

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.webtoonapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

/**
 * WebView 띄우기,
 *
 */

class MainActivity : AppCompatActivity(), OnTabLayoutNameChanged {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(WebViewFragment.Companion.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val tab0 = sharedPreferences?.getString("tab0_name", "월요일 웹툰")
        val tab1 = sharedPreferences?.getString("tab1_name", "수요일 웹툰")
        val tab2 = sharedPreferences?.getString("tab2_name", "금요일 웹툰")

        binding.viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            run {
//                val textView = TextView(this@MainActivity)
//                textView.text = when(position) {
//                    0 -> tab0
//                    1 -> tab1
//                    else -> tab2
//                }
//                textView.gravity = Gravity.CENTER
//
//                tab.customView = textView
                tab.text = when(position) {
                    0 -> tab0
                    1 -> tab1
                    else -> tab2
                }
            }
        }.attach()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.fragments[binding.viewPager.currentItem]
        if(currentFragment is WebViewFragment) {
            if(currentFragment.canGoBack()) {
                currentFragment.goBack()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun nameChanged(position: Int, name: String) {
        //WebViewFragment에서 Listen하고 있음
        val tab = binding.tabLayout.getTabAt(position)
        tab?.text = name
    }
}