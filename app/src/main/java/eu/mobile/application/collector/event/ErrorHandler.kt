package eu.mobile.application.collector.event

import org.greenrobot.eventbus.EventBus

class ErrorHandler {
    companion object {
        fun postMessageEvent(message: Message) {
            postEvent(message)
        }

        fun postErrorMessageEvent(throwable: Throwable) {
            postEvent(Message().apply { message = throwable.message })
        }
        fun postErrorMessageEvent(ex: java.lang.Exception) {
            postEvent(Message().apply { message = ex.message })
        }

        private fun postEvent(message: Message) {
            EventBus.getDefault().post(message)
        }
    }
}