package com.simxdeveloper.mapsample;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * User: simx Date: 09/05/18 18:01
 */
public class MapManager {
  private Context mContext;
  public static String [] LOCATION_PERMISSION = new String[]{permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION};
  public final static int RC_PERMISSION_LOCATION = 123;
  public MapManager (Context context) {
    this.mContext = context;
  }
  public static MapManager instance(Context context){
    return new MapManager (context);
  }

}
