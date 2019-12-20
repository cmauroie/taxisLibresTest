package ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapDetailsViewModel: ViewModel(){
    var texto_distancia = MutableLiveData<String>()
    var texto_tiempo = MutableLiveData<String>()
}
