package col.rgm.taxislibrestest

import Utilities.Util
import ViewModels.MapDetailsViewModel
import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import col.rgm.taxislibrestest.databinding.ActivityMapDetailsBinding

class MapDetailsActivity : AppCompatActivity() {
    private val utilities = Util()
    private lateinit var binding: ActivityMapDetailsBinding
    private val mapDetailsViewModel = MapDetailsViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_details)
        binding.mapDetailsViewModel = mapDetailsViewModel
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
        setDefaultData()
    }

    private fun setDefaultData() {
        binding.apply {
            mapDetailsViewModel!!.texto_tiempo.value = intent.getStringExtra(utilities.KEY_TIME)
            mapDetailsViewModel!!.texto_distancia.value = intent.getStringExtra(utilities.KEY_DISTANCE)
            invalidateAll()
        }
    }


}