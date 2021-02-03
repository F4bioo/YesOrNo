package fbo.costa.yesorno.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fbo.costa.yesorno.data.model.Ans
import fbo.costa.yesorno.repository.MainRepository
import fbo.costa.yesorno.util.DataState
import fbo.costa.yesorno.util.MainStateEvent
import fbo.costa.yesorno.util.VibrateUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val vibrate: VibrateUtil,
    private val mainRepository: MainRepository
) : ViewModel() {

    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        GlobalScope.launch(Dispatchers.Main) {
            _ansEvent.value = DataState.Error("Exception: ${throwable.message}")
        }
    }

    private val _ansEvent = MutableLiveData<DataState<Ans>>()
    val ansEvent: LiveData<DataState<Ans>>
        get() = _ansEvent

    fun setStateEvent(stateEvent: MainStateEvent) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            when (stateEvent) {
                is MainStateEvent.AnsEvent -> {
                    mainRepository.getAns().onEach { _dataState ->
                        withContext(Dispatchers.Main) {
                            _ansEvent.value = _dataState
                        }
                    }.launchIn(this)
                }
                is MainStateEvent.VibrateEvent -> {
                    val position = vibrate.getPattern(stateEvent.vibratePosition)
                    vibrate[position] = VibrateUtil.NO_REPEAT
                }
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}
