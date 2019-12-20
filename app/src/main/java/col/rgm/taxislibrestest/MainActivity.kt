package col.rgm.taxislibrestest


import android.Manifest
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_INTERNET = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val layoutParams = androidx.appcompat.app.ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.custom_action_bar, null)
        supportActionBar!!.setCustomView(view, layoutParams)
        val parent = view.getParent() as Toolbar
        parent.setContentInsetsAbsolute(0, 0)
        findViewById<Button>(R.id.buttonMapa).setOnClickListener({ initiateMap() })
        findViewById<Button>(R.id.buttonPeliculas).setOnClickListener({
            startActivity(
                Intent(
                    this,
                    MovieActivity::class.java
                )
            )
        })
    }


    override fun onBackPressed() {
        // It's not gonna go back to the splash screen
    }

    private fun initiateMap() {
        if (checkInternetPermission()) {
            if (checkNetworkAvailability()) {
                startActivity(Intent(this, MapActivity::class.java))
            } else {
                Toast.makeText(this, R.string.internet, Toast.LENGTH_LONG).show()
                startActivity(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS))
            }
        } else {
            requestPermissions()
        }
    }


    private fun checkNetworkAvailability(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean? = activeNetwork?.isConnectedOrConnecting
        if (isConnected == null) {
            return false
        } else {
            return isConnected
        }
    }

    private fun checkInternetPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.INTERNET),
            PERMISSIONS_INTERNET
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_INTERNET) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                initiateMap()
            }
        }
    }
}

