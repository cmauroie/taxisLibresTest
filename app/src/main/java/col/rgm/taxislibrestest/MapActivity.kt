package col.rgm.taxislibrestest


import Repository.RemoteDataSource.Models.GoogleDirectionsDTO
import Repository.RemoteDataSource.ConnectionService
import Repository.RemoteDataSource.URLService
import Utilities.Util
import ViewModels.MapViewModel
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.View.*
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.util.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import col.rgm.taxislibrestest.databinding.ActivityMapBinding
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    private var distancia: String? = null
    private var duracion: String? = null
    private val utilities = Util()
    private val connectionService =
        ConnectionService()
    private val ZOOM = 12f
    private val TRY_CONNECT_TIME = 3500L
    private val KEY_M_CURRENT_LAT_LONG = "key_m_current_lat_long"
    private var markerActive: Boolean = false
    private var adressess: List<Address>? = null
    private var geocoder: Geocoder? = null
    private var marker: Marker? = null
    private var markerIsDraggable: Boolean = false
    private var currentLocation: LatLng? = null
    private var destinationLocation: LatLng? = null
    private lateinit var binding: ActivityMapBinding
    private var lastLocation: Location? = null
    internal var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequested: Boolean = true
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0
    private var mapa: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
            binding.mapViewModel =ViewModelProviders.of(this).get(MapViewModel::class.java)
            //Syncronizes the viewmodel data and the UI
            binding.lifecycleOwner = this
            //binding.invalidateAll()
            //hides action bar to increase screen size usage
            supportActionBar?.hide()
            initialSettings()
            restoreSavedValues(savedInstanceState)
        } catch (e: Exception) {
            Log.i(utilities.TAG_CREATE, e.message.toString())
        }
    }


    fun initialSettings() {
        //Flag to don't repeat the intent petition for location activation
        locationRequested = true
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        binding.apply {
            texto_origen_titulo.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.texto_ubicaciones)
            )
            texto_destino_titulo.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.texto_ubicaciones)
            )
            texto_origen.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.texto_ubicaciones2)
            )
            texto_destino.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.texto_ubicaciones2)
            )
            boton_calcular_ruta.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.texto_ubicaciones2)
            )
            boton_resetear.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.texto_ubicaciones2)
            )
            boton_fijar_destino.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.texto_ubicaciones2)
            )
            boton_resumen_datos.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.texto_ubicaciones2)
            )
            botonAgregarDestino.setOnClickListener {
                activateMarker(currentLocation, true)
            }
            boton_fijar_destino.setOnClickListener {
                defineDestination()
            }
            botonCalcularRuta.setOnClickListener {
                calculateRoute()
            }
            botonResumenDatos.setOnClickListener {
                showFinalData()
            }
            botonResetear.setOnClickListener {
                reset()
            }
            invalidateAll()
        }
    }

    fun restoreSavedValues(data: Bundle?) {
        try {
            //RESTORES PREVIOUS DATA
            if (data != null) {
                destinationLocation = data.getParcelable(utilities.KEY_M_LAT_LONG)
                markerIsDraggable = data.getBoolean(utilities.KEY_M_DRAGGABLE)
                markerActive = data.getBoolean(utilities.KEY_M_ACTIVE)
                currentLocation = data.getParcelable(KEY_M_CURRENT_LAT_LONG)
                distancia = data.getString(utilities.KEY_DISTANCE)
                duracion = data.getString(utilities.KEY_TIME)
            }
        } catch (e: Exception) {
            Log.i(utilities.TAG_ON_RES, e.message.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        try {
            //SAVES RELEVANT STATES AND INFORMATION
            outState.putParcelable(utilities.KEY_M_LAT_LONG, marker?.position)
            outState.putParcelable(KEY_M_CURRENT_LAT_LONG, currentLocation)
            outState.putBoolean(utilities.KEY_M_DRAGGABLE, markerIsDraggable)
            outState.putBoolean(utilities.KEY_M_ACTIVE, markerActive)
            outState.putString(utilities.KEY_DISTANCE, distancia)
            outState.putString(utilities.KEY_TIME, duracion)

        } catch (e: Exception) {
            Log.i(utilities.TAG_ON_SIS, e.message.toString())
        }
    }

    private fun reset() {
        try {
            binding.apply {
                mapViewModel!!.resetValues()
                markerActive = false
                markerIsDraggable = false
                locationRequested = false
                marker = null
                mapa?.clear()
                invalidateAll()
            }
        } catch (e: Exception) {
            Log.i(utilities.TAG_RESET, e.message.toString())
        }
    }

    private fun calculateRoute() {
        try {
            val retrofit = connectionService.getRetrofit(utilities.URL_GOOGLE)
            val service = retrofit.create(URLService::class.java)
            val call = service.getDirections(
                currentLocation?.latitude.toString() + "," + currentLocation?.longitude.toString(),
                destinationLocation?.latitude.toString() + "," + destinationLocation?.longitude.toString(),
                resources.getString(R.string.google_maps_api)
            )

            call.enqueue(object : Callback<GoogleDirectionsDTO> {
                override fun onResponse(
                    call: Call<GoogleDirectionsDTO>,
                    response: Response<GoogleDirectionsDTO>
                ) {
                    if (response.code() == 200) {
                        binding.apply {
                            mapViewModel!!.vBotonResumenDatos.value = VISIBLE
                            distancia = response.body()!!.routes[0].legs[0].distance.text
                            duracion = response.body()!!.routes[0].legs[0].duration.text
                        }
                        Toast.makeText(
                            applicationContext,
                            resources.getText(R.string.ruta_calculada),
                            LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<GoogleDirectionsDTO>, t: Throwable) {
                    Log.i(utilities.TAG_RETROFIT, t.message)
                    Toast.makeText(
                        applicationContext,
                        resources.getText(R.string.error_google),
                        LENGTH_LONG
                    ).show()
                }
            })
        } catch (e: Exception) {
            Log.i(utilities.TAG_RETROFIT, e.message.toString())
        }
    }

    private fun showFinalData() {
        try {
            val intent = Intent(this, MapDetailsActivity::class.java)
            intent.putExtra(utilities.KEY_TIME, duracion)
            intent.putExtra(utilities.KEY_DISTANCE, distancia)
            startActivity(intent)
        } catch (e: Exception) {
            Log.i(utilities.TAG_FINAL_D, e.message.toString())
        }
    }

    private fun defineDestination() {
        try {
            Toast.makeText(this, R.string.destino_fijado, LENGTH_LONG).show()
            marker?.isDraggable = false
            markerIsDraggable = false
            destinationLocation = marker?.position!!
            adressess = geocoder?.getFromLocation(
                marker?.position!!.latitude,
                marker?.position!!.longitude,
                1
            )!!
            binding.apply {
                mapViewModel!!.texto_destino.value = adressess?.get(0)?.getAddressLine(0)
                mapViewModel!!.vBotonCalcularRuta.value = VISIBLE
                mapViewModel!!.vBotonFijarDestino.value = GONE
                invalidateAll()
            }
        } catch (e: Exception) {
            Log.i(utilities.TAG_DEF_DE, e.message.toString())
        }
    }

    fun activateMarker(location: LatLng?, isDraggable: Boolean) {
        try {
            if (currentLocation != null) {
                Toast.makeText(this, R.string.mover_marcador, LENGTH_LONG).show()
                if (marker == null) {
                    binding.apply {
                        mapViewModel!!.vContenedorClickDestino.value = GONE
                        mapViewModel!!.vContenedorBotones.value = VISIBLE
                        mapViewModel!!.vContenedorDestino.value = VISIBLE
                        mapViewModel!!.vBotonCalcularRuta.value = INVISIBLE
                        invalidateAll()
                    }
                    //Recovering a previous state
                    if (markerActive && !markerIsDraggable) {
                        binding.mapViewModel!!.vBotonCalcularRuta.value = VISIBLE
                    } else {
                        markerIsDraggable = true
                        markerActive = true
                    }
                    mapa?.setOnMarkerDragListener(this)
                    marker =
                        mapa?.addMarker(
                            MarkerOptions().position(location!!).draggable(isDraggable).title(
                                resources.getString(R.string.titulo_marcador)
                            )
                        )
                    marker?.showInfoWindow()
                    adressess = geocoder?.getFromLocation(
                        marker?.position!!.latitude,
                        marker?.position!!.longitude,
                        1
                    )!!
                    binding.apply {
                        mapViewModel!!.texto_destino.value = adressess?.get(0)?.getAddressLine(0)
                    }
                } else {
                    marker?.isDraggable = true
                    markerIsDraggable = true
                }
            } else {
                getLastLocation()
            }
        } catch (e: Exception) {
            Log.i(utilities.TAG_ACT_MKR, e.message.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            getLastLocation()
        } catch (e: Exception) {
            Log.i(utilities.TAG_ON_RESU, e.message.toString())
        }
    }

    override fun onMapReady(mapa: GoogleMap?) {
        try {
            this.mapa = mapa
            //If the state is recovered
            if (markerActive) {
                if (destinationLocation != null) {
                    activateMarker(destinationLocation, markerIsDraggable)
                } else {
                    activateMarker(currentLocation, markerIsDraggable)
                }
            }
            getLastLocation()
        } catch (e: Exception) {
            Log.i(utilities.TAG_ON_MAP, e.message.toString())
        }
    }

    private fun getLastLocation() {
        try {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    fusedLocationClient?.lastLocation?.addOnCompleteListener(this) { task ->
                        var location: Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            configureMap(LatLng(location.latitude, location.longitude))
                        }
                    }
                } else {
                    if (locationRequested) {
                        locationRequested = false
                        Toast.makeText(this, R.string.localizacion, Toast.LENGTH_LONG).show()
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                        getLastLocation()
                    }
                }
            } else {
                requestPermissions()
            }
        } catch (e: Exception) {
            Log.i(utilities.TAG_LAST_L, e.message.toString())
        }
    }


    private fun requestNewLocationData() {
        try {
            var mLocationRequest = LocationRequest()
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            mLocationRequest.interval = 0
            mLocationRequest.fastestInterval = 0
            mLocationRequest.numUpdates = 1

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient!!.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
            )
        } catch (e: Exception) {
            Log.i(utilities.TAG_NEW_L, e.message.toString())
        }
    }

    private fun configureMap(latLng: LatLng) {
        try {
            currentLocation = latLng
            geocoder = Geocoder(this, Locale.getDefault())
            adressess = geocoder?.getFromLocation(latLng.latitude, latLng.longitude, 1)!!
            mapa?.isMyLocationEnabled = true
            mapa?.uiSettings?.isMyLocationButtonEnabled = true
            mapa?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng,
                    ZOOM
                )
            )
            binding.apply {
                textoOrigen.text = adressess?.get(0)?.getAddressLine(0)
                invalidateAll()
            }
        } catch (e: Exception) {
            Log.i(utilities.TAG_CONF_M, e.message.toString())
        }

    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            try {
                currentLocation = LatLng(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude
                )
                configureMap(
                    currentLocation!!
                )
            } catch (e: Exception) {
                Log.i(utilities.TAG_LOC_RE, e.message.toString())
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        try {
            marker?.position = p0?.position
            adressess = geocoder?.getFromLocation(
                marker?.position!!.latitude,
                marker?.position!!.longitude,
                1
            )!!
            binding.apply {
                mapViewModel!!.texto_destino.value = adressess?.get(0)?.getAddressLine(0)
            }
        } catch (e: Exception) {
            Log.i(utilities.TAG_MRK_DE, e.message.toString())
        }
    }

    override fun onMarkerDragStart(p0: Marker?) {

    }

    override fun onMarkerDrag(p0: Marker?) {

    }
}
