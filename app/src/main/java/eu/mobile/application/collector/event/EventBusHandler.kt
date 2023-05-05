package eu.mobile.application.collector.event

import org.greenrobot.eventbus.EventBus

class EventBusHandler {
    companion object {
        fun postSubtitle(subtitleMessage: SubtitleMessage){
            postEvent(subtitleMessage)
        }
        fun postMessage(message: Message) {
            postEvent(message)
        }

        fun postErrorMessage(throwable: Throwable) {
            postEvent(Message().apply { message = throwable.message })
        }
        fun postErrorMessage(ex: java.lang.Exception) {
            postEvent(Message().apply { message = ex.message })
        }

        private fun <T> postEvent(event: T) {
            EventBus.getDefault().post(event)
        }
    }
}