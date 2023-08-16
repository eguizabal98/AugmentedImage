package com.eem.augmentedreality

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.eem.augmentedreality.augimage.AugmentedImageFragment
import com.eem.augmentedreality.cloud.CloudAnchorFragment
import com.eem.augmentedreality.geo.AugmentedLocationFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.commit {
            when (BuildConfig.BUILD_TYPE) {
                "geo" -> {
                    add(R.id.containerFragment, AugmentedLocationFragment::class.java, Bundle())
                }

                "aug" -> {
                    add(R.id.containerFragment, AugmentedImageFragment::class.java, Bundle())
                }

                "cloud" -> {
                    add(R.id.containerFragment, CloudAnchorFragment::class.java, Bundle())
                }

                else -> {
                    add(R.id.containerFragment, AugmentedLocationFragment::class.java, Bundle())
                }
            }
        }
    }
}