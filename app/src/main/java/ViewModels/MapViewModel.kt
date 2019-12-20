package ViewModels

import android.util.Log
import android.view.View.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    var texto_origen = MutableLiveData<String>("")
    var texto_destino = MutableLiveData<String>("")
    var vContenedorClickDestino = MutableLiveData<Int>(VISIBLE)
    var vContenedorDestino = MutableLiveData<Int>(GONE)
    var vBotonCalcularRuta = MutableLiveData<Int>(INVISIBLE)
    var vContenedorBotones = MutableLiveData<Int>(INVISIBLE)
    var vBotonFijarDestino = MutableLiveData<Int>(VISIBLE)
    var vBotonResumenDatos = MutableLiveData<Int>(INVISIBLE)

    fun resetValues(){
        texto_destino.value = ""
        vContenedorClickDestino.value = VISIBLE
        vContenedorDestino.value = GONE
        vBotonCalcularRuta.value = INVISIBLE
        vContenedorBotones.value = INVISIBLE
        vBotonFijarDestino.value = VISIBLE
        vBotonResumenDatos.value = INVISIBLE
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("viewmodel", "Destroyed!")
    }
}