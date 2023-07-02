package com.suka.superahorro.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.suka.superahorro.R
import com.suka.superahorro.packages.LoadingListener

class LoginActivity : AppCompatActivity(), LoadingListener {

    private lateinit var navHostFragment: NavHostFragment

    val isLoading = MutableLiveData(false)
    private lateinit var loadingLayout: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navhost_login) as NavHostFragment

        loadingLayout = findViewById(R.id.loading_layout)
        isLoading.observe(this) { isLoading ->
            loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }


    override fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }
}