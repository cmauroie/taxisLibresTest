package Utilities


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Util() {
    ////////////////
    ///ERROR TAGS///
    ////////////////
    val TAG_RESET = "error en reset()"
    val TAG_ON_SIS = "error SaveInstanceSt"
    val TAG_ON_RES = "error RestInstanceSt"
    val TAG_CREATE = "error onCreate()"
    val TAG_RETROFIT = "error calculateRoute()"
    val TAG_FINAL_D = "error showFinalData()"
    val TAG_DEF_DE = "error defineDestin()"
    val TAG_ACT_MKR = "error activeMarker()"
    val TAG_ON_RESU = "error onResume()"
    val TAG_ON_MAP = "error onMapReady()"
    val TAG_LAST_L = "error getLastLocation()"
    val TAG_NEW_L = "error rNewLocationD()"
    val TAG_CONF_M = "error configureMap()"
    val TAG_LOC_RE = "error onLocationRes()"
    val TAG_MRK_DE = "error onMarkerDragEnd()"
    val TAG_REC_V_A = "error onBindViewH()"

    ////////////////
    //////URLS//////
    ////////////////
    val URL_GOOGLE="https://maps.googleapis.com/"
    val URL_THE_MOVIE_DB="https://api.themoviedb.org/"
    //For more info about images: https://developers.themoviedb.org/3/configuration/get-api-configuration
    val URL_IMAGES_THE_MOVIE_DB="https://image.tmdb.org/t/p/"

    /////////////////
    //CONFIGURATIONS/
    /////////////////
    //For more info about sizes: https://developers.themoviedb.org/3/configuration/get-api-configuration
    val IMAGE_SIZE = "w500"

    ////////////////
    ////LANGUAGES///
    ////////////////
    val LANGUAGE_ESPAÑOL="es"
    ////////////////
    //////KEYS//////
    ////////////////
    val KEY_TIME = "key_time"
    val KEY_DISTANCE = "key_distance"
    val KEY_ID_MOVIE = "key_id_movie"
    val KEY_M_ACTIVE = "key_m_active"
    val KEY_M_DRAGGABLE = "key_m_draggable"
    val KEY_M_LAT_LONG = "key_m_lat_long"
    val KEY_T_DESTINO = "key_t_destino"
    val KEY_V_CONTENEDOR_CLICK_DESTINO = "key_v_ccd"
    val KEY_V_CONTENEDOR_DESTINO = "key_v_cd"
    val KEY_V_BOTON_CALCULAR_RUTA = "key_v_bcr"
    val KEY_V_BOTON_FIJAR_DESTINO = "key_v_bfd"
    val KEY_V_CONTENEDOR_BOTONES = "key_v_cb"
    val KEY_V_BOTON_RESUMEN_DATOS = "key_v_cb"

}