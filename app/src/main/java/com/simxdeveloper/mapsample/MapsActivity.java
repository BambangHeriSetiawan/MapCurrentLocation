package com.simxdeveloper.mapsample;
import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MapsActivity extends FragmentActivity {

  private GoogleMap mMap;
  SupportMapFragment mapFragment;
  private IconGenerator iconGenerator;
  private Marker markerNewLocation;
  private FusedLocationProviderClient mFusedLocationProviderClient;
  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate (savedInstanceState);
    setContentView (R.layout.activity_maps);
    iconGenerator = new IconGenerator(this);
    iconGenerator.setStyle(IconGenerator.STYLE_ORANGE);
    mapFragment = (SupportMapFragment) getSupportFragmentManager ()
        .findFragmentById (R.id.map);
    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient (this);
    requestPermissionLocation ();
  }

  /**
   * Manipulates the map once available. This callback is triggered when the map is ready to be
   * used. This is where we can add markers or lines, add listeners or move the camera. In this
   * case, we just add a marker near Sydney, Australia. If Google Play services is not installed on
   * the device, the user will be prompted to install it inside the SupportMapFragment. This method
   * will only be triggered once the user has installed Google Play services and returned to the
   * app.
   */
  private OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback () {
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady (GoogleMap googleMap) {
      mMap = googleMap;
      mMap.setMyLocationEnabled (true);
      mMap.setOnCameraIdleListener (cameraIdleListener);
    }
  };

  /**
   * Remove marker if marker is exit
   * Add new marker with new LatLng
   * @param latLng
   * @param title
   */
  private void changeMarkerPositions(LatLng latLng, String title){
    if (markerNewLocation!=null){
      markerNewLocation.remove ();
    }
    /*markerNewLocation = mMap.addMarker(new MarkerOptions()
        .position(latLng).icon (null));*/
    setDetailLocation (latLng.toString ());
  }
  private void moveCameraPosistion(LatLng latLng){
    mMap.animateCamera (CameraUpdateFactory.newLatLngZoom (latLng,13));
  }

  /**
   * Handle event where camera is moving
   */
  private OnCameraIdleListener cameraIdleListener = new OnCameraIdleListener () {
    @Override
    public void onCameraIdle () {
      changeMarkerPositions (mMap.getCameraPosition ().target,"YOU IN HERE");
    }
  };

  /**
   * Request permission LOCATION
   */
  @AfterPermissionGranted (MapManager.RC_PERMISSION_LOCATION)
  private void requestPermissionLocation () {
    if (hasPersmissionLocation ()){
      buildMap();
    }else {
      EasyPermissions.requestPermissions (this,"Need Location Permiison",MapManager.RC_PERMISSION_LOCATION,MapManager.LOCATION_PERMISSION);
    }

  }

  /**
   * Build map after permissionis granted
   */
  private void buildMap () {
    mapFragment.getMapAsync (mapReadyCallback);
    getDeviceLocation();
  }

  private void getDeviceLocation () {
    if (hasPersmissionLocation ()){
      @SuppressLint("MissingPermission") Task task = mFusedLocationProviderClient.getLastLocation ();
      task.addOnCompleteListener (new OnCompleteListener () {
        @Override
        public void onComplete (@NonNull Task task) {
         if (task.isSuccessful ()){
           Location location = (Location) task.getResult ();
           moveCameraPosistion (new LatLng (location.getLatitude (),location.getLongitude ()));
         }
        }
      }).addOnFailureListener (new OnFailureListener () {
        @Override
        public void onFailure (@NonNull Exception e) {
          Toast.makeText (getApplicationContext (),e.getMessage (),Toast.LENGTH_SHORT).show ();
        }
      });
    }
  }

  /**
   * Check app have permission location or not
   * @return
   */
  private boolean hasPersmissionLocation(){
    return EasyPermissions.hasPermissions (this, MapManager.LOCATION_PERMISSION);
  }

  /**
   * Handel event base on requestcode.
   * @param requestCode
   * @param permissions
   * @param grantResults
   */
  @Override
  public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
      case MapManager.RC_PERMISSION_LOCATION:{
        buildMap ();
      }
    }
  }
  private void setDetailLocation(String detailLocation){
    ((TextView)findViewById (R.id.tv_detail_location)).setText (detailLocation);
    ((TextView)findViewById (R.id.tv_detail_location)).setBackgroundColor (getResources ().getColor (android.R.color.transparent));
  }
}
