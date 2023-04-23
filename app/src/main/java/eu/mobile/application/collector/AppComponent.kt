import dagger.Component
import eu.mobile.application.collector.MainActivity

@Component
interface AppComponent {
    // Classes that can be injected by this Component
    fun inject(activity: MainActivity)
}