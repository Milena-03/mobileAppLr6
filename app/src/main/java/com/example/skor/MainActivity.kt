package com.example.skor

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Устанавливаем кастомный Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        // Скрываем стандартную кнопку назад
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setupNavigation()
        setupCustomBackButton()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.addItemFragment)
        )

        // Слушатель для обновления заголовка и кнопки назад
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateToolbarTitle(destination.label.toString())
            updateBackButtonVisibility(destination.id)
        }
    }

    private fun setupCustomBackButton() {
        val customBackButton = findViewById<ImageButton>(R.id.custom_back_button)
        customBackButton.setOnClickListener {
            onSupportNavigateUp()
        }
    }

    private fun updateToolbarTitle(title: String) {
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = title
    }

    private fun updateBackButtonVisibility(currentDestinationId: Int) {
        val customBackButton = findViewById<ImageButton>(R.id.custom_back_button)

        // Показываем кнопку назад везде, кроме стартового фрагмента
        if (currentDestinationId == R.id.addItemFragment) {
            customBackButton.visibility = View.GONE
        } else {
            customBackButton.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}