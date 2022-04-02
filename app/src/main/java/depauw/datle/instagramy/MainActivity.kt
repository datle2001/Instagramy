package depauw.datle.instagramy

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseFile
import com.parse.ParseUser
import depauw.datle.instagramy.databinding.ActivityMainBinding
import depauw.datle.instagramy.fragments.ComposeFragment
import depauw.datle.instagramy.fragments.HomeFragment
import depauw.datle.instagramy.fragments.ProfileFragment
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentManager: FragmentManager
    private var fragmentToShow: Fragment? = null

    private val bottom_navigation_itemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.action_home -> {
                //Navigate to home screen
                fragmentToShow = HomeFragment()
            }
            R.id.action_compose -> {
                fragmentToShow = ComposeFragment()
            }
            R.id.action_profile -> {
                fragmentToShow = ProfileFragment()            }
        }

        if(fragmentToShow != null) {
            fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, fragmentToShow!!)
                .commit()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, HomeFragment()).commit()

        binding.bottomNavigation.setOnItemSelectedListener(bottom_navigation_itemSelectedListener)
    }

    companion object {
        val TAG = "MainActivity"
    }
}