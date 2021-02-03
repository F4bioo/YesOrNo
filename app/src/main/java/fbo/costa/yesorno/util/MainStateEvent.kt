package fbo.costa.yesorno.util

sealed class MainStateEvent {
    object AnsEvent : MainStateEvent()
    data class VibrateEvent(val vibratePosition: Int) : MainStateEvent()
}
