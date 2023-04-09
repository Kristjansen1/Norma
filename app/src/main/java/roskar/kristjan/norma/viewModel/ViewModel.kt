package roskar.kristjan.norma.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class ViewModel : ViewModel() {

    private val _nekineki = MutableLiveData<String>()
    val nekineki: LiveData<String> = _nekineki

    private val _activeMonth = MutableLiveData<String>()
    val activeMonth: LiveData<String> = _activeMonth

    init {
        _nekineki.value = "nekineki"
    }

    fun setNekiNeki(value: String) {
        _nekineki.value = value
    }

    fun setActiveMonth(value: String) {
        _activeMonth.value = value
    }

}

