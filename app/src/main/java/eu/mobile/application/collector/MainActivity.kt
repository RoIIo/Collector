package eu.mobile.application.collector

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.event.SubtitleMessage
import eu.mobile.application.collector.fragment.categoryEntry.CategoryEntryFragment
import eu.mobile.application.collector.fragment.initialization.InitializationFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Logger
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object{
        val logger = java.util.logging.Logger.getLogger(MainActivity::class.simpleName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerEventBusListener()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val appBarConfiguration = AppBarConfiguration.Builder(R.id.initializationFragmentDestination, R.id.categoryListFragmentDestination ).build()
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterEventBusListener()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorMessageReceived(message: Message){
        Toast.makeText(this, message.message , Toast.LENGTH_SHORT).show()
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    fun onSubtitleMessageReceived(subtitle: SubtitleMessage) {
        supportActionBar?.subtitle = subtitle.name
    }
    private fun registerEventBusListener() {
        EventBus.getDefault().register(this)
    }

    private fun unregisterEventBusListener() {
        EventBus.getDefault().unregister(this)
    }
}