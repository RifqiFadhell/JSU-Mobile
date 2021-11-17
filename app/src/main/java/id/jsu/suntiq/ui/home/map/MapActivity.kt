package id.jsu.suntiq.ui.home.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import id.jsu.suntiq.R
import id.jsu.suntiq.api.request.location.LocationsRequest
import id.jsu.suntiq.api.response.location.LocationItem
import id.jsu.suntiq.api.response.location.LocationResponse
import id.jsu.suntiq.api.response.vehicle.confirmation.ConfirmationVehicleResponse
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_LOCATION
import id.jsu.suntiq.preference.tinyDb.TinyDB
import id.jsu.suntiq.ui.home.confirmation.ConfirmationVehicleContract
import id.jsu.suntiq.ui.home.confirmation.ConfirmationVehiclePresenter
import id.jsu.suntiq.utils.LoaderIndicatorHelper
import id.jsu.suntiq.utils.base.BaseActivity
import id.jsu.suntiq.utils.extensions.getToken
import id.jsu.suntiq.utils.extensions.showOkDialog
import id.jsu.suntiq.utils.extensions.toGone
import id.jsu.suntiq.utils.extensions.toVisible
import kotlinx.android.synthetic.main.map_activity.*
import java.lang.StringBuilder

class MapActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, ConfirmationVehicleContract.View {

    private lateinit var gMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var loading: LoaderIndicatorHelper? = null
    private var presenter: ConfirmationVehiclePresenter? = null
    private var adapter: MapAdapter? = null

    private var list = ArrayList<LocationItem>()

    private var location: Boolean = false
    private var dataLocation: LocationItem? = null
    private var status: Boolean = false

    private val LOCATION_PERMISSION = 42

    override fun provideLayout() {
        setContentView(R.layout.map_activity)
    }

    override fun init(bundle: Bundle?) {
        checkPermission()
        loading = LoaderIndicatorHelper()
        presenter = ConfirmationVehiclePresenter(this)

        adapter = MapAdapter(list, this,
            onItemClick = { _: Int, item: LocationItem ->
                dataLocation = item
            }, onLocationClick = { _: Int, item: LocationItem ->
                showMarker(item)
            }
        )
        listLocation.adapter = adapter
        listLocation.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        listLocation.itemAnimator = DefaultItemAnimator()
        showLoading()
    }

    override fun initData(bundle: Bundle?) {
    }

    override fun initListener(bundle: Bundle?) {
        buttonSubmit.setOnClickListener {
            dataLocation?.let { it1 -> openMapDestination(it1) }
            TinyDB(this).putObject(TINY_LOCATION, dataLocation)
            finish()
        }
    }

    override fun getDataConfirmation(response: ConfirmationVehicleResponse) {}

    private fun openMapDestination(data: LocationItem) {
        if (data.coordinate != null) {
            val navigation: Uri = Uri.parse("google.navigation:q=" + data.coordinate)
            val navigationIntent = Intent(Intent.ACTION_VIEW, navigation)
            navigationIntent.setPackage("com.google.android.apps.maps")
            startActivity(navigationIntent)
        }
    }

    private fun showMarker(data: LocationItem) {
        gMap.clear()
        if (data.coordinate != null) {
            val dataLocation = data.coordinate.replace(".", "")
            val location = dataLocation.split(",").toTypedArray()
            val latitude = StringBuilder(location[0]).insert(2, ".").toString().toDouble()
            val longitude = StringBuilder(location[1]).insert(3, ".").toString().toDouble()
            gMap.addMarker(MarkerOptions().position(LatLng(latitude, longitude)))
            gMap.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        latitude,
                        longitude
                    )
                )
            )
            gMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
        }
    }

    override fun getListLocation(response: LocationResponse) {
        if (response.status == 200) {
            status = true
            response.data?.let { list.addAll(it) }
            adapter?.notifyDataSetChanged()
            hideLoading()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = false
                AlertDialog.Builder(this)
                    .setTitle("Kamu belum meng-aktifkan gps kamu")  // GPS not found
                    .setMessage("Silahkan Nyalakan Gps kamu sebelum menggunakan maps") // Want to enable?
                    .setPositiveButton("Oke") { _, _ ->
                        this.startActivity(
                            Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                            )
                        )
                    }
                    .setNegativeButton("Tidak") { _, _ -> onBackPressed() }
                    .setCancelable(false)
                    .show()
            } else {
                initMap()
            }

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION
            )
        }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mapFragment.getMapAsync(this)
    }

    private fun updateMapLocation(location: Location?) {
        gMap.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    location?.latitude ?: 0.0,
                    location?.longitude ?: 0.0
                )
            )
        )
        gMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
        if (location?.latitude != null) {
            if (!status) {
                presenter?.listLocation(
                    LocationsRequest(
                        location.latitude.toString(),
                        location.longitude.toString()
                    )
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationTracking() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    updateMapLocation(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            LocationRequest(),
            locationCallback,
            null
        )
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.isMyLocationEnabled = true
        gMap.setOnMyLocationButtonClickListener(this)
        gMap.setOnMyLocationClickListener(this)
        initLocationTracking()
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMyLocationClick(location: Location) {
        gMap.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    location.latitude,
                    location.longitude
                )
            )
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == LOCATION_PERMISSION) {
            if (permissions.size == 1 &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                initMap()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initMap()
        if (::gMap.isInitialized) {
            initLocationTracking()
        }
    }

    override fun onPause() {
        super.onPause()
//        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun showLoading() {
        layoutLoading.toVisible()
    }

    override fun hideLoading() {
        layoutLoading.toGone()
    }

    override fun showError(throwable: Throwable) {
        showOkDialog(throwable.message.toString(), "Oke", null)
    }
}