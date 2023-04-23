package eu.mobile.application.collector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.ui.main.MainFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        toolbar.subtitle = "Menu główne"
        setSupportActionBar(toolbar)
        registerEventBusListener()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
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