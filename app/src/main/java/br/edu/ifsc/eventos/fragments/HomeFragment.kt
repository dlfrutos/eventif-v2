package br.edu.ifsc.eventos.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.ifsc.eventos.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.MapView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var mMap: MapView? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMap?.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mMap = view?.findViewById(R.id.mapView) as MapView
        mMap?.onCreate(savedInstanceState)
        mMap?.getMapAsync(this)

        return view
    }

    override fun onResume() {
        super.onResume()
        mMap?.onResume()
        textViewEventDescription.text = arguments!!.getString("eventDescription")
        textViewContactInfo.text = arguments!!.getString("contactInfo")
    }

    override fun onPause() {
        super.onPause()
        mMap?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mMap?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMap?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap?.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap) {
        val ifsc = LatLng(-26.184514, -50.368601)
        map?.addMarker(MarkerOptions().position(ifsc).title("Auditório do IFSC"))
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(ifsc, 17F))
        map?.uiSettings.isZoomControlsEnabled = true;
        map?.uiSettings.isMyLocationButtonEnabled = true
        map?.uiSettings.isZoomGesturesEnabled = true
    }
}