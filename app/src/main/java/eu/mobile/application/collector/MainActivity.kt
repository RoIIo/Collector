package eu.mobile.application.collector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.fragment.mainMenu.MainFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerEventBusListener()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val appBarConfiguration = AppBarConfiguration.Builder(R.id.mainMenuFragmentDestination).build()
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
        navHostFragment.findNavController().navigate(R.id.mainMenuFragmentDestination)
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterEventBusListener()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorMessageReceived(message: Message){
        Toast.makeText(this, message.message , Toast.LENGTH_SHORT).show()
    }

    private fun registerEventBusListener() {
        EventBus.getDefault().register(this)
    }

    private fun unregisterEventBusListener() {
        EventBus.getDefault().unregister(this)
    }
}