package ru.example.sed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ru.example.sed.commons.ui.base.BaseActivity

class MainActivity : AppCompatActivity(), BaseActivity {
    private val cacheService by lazy {
        ExampleApplication.appComponent(this).cacheService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun logout() {
        cacheService.wipe()
        findNavController(R.id.nav_host_fragment).navigate(R.id.login, null)
    }
}