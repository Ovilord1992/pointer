package com.pointer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.pointer.Calendar.getDate;

public class SavesActivity extends FragmentActivity implements OnMapReadyCallback {
    public GoogleMap mMap;

    ArrayList<LatLng> listGet = new ArrayList<>();

    private MarkerOptions markerSave;

    boolean stule_state = false;
    View mapViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        mapViews = mapFragment.getView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public ArrayList<LatLng> getListFromLocal(String key) {
        SharedPreferences prefs = getSharedPreferences("AppName", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<LatLng>>() {
        }.getType();
        return gson.fromJson(json, type);

    }


    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        if (mapViews != null && mapViews.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapViews.findViewById(Integer.parseInt("1")).getParent())
                    .findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 550);
        }
        //переопределение положения зума
        if (mapViews != null && mapViews.findViewById(Integer.parseInt("1")) != null) {
            View zoomIn = ((View) mapViews.findViewById(Integer.parseInt("1")).getParent())
                    .findViewById(Integer.parseInt("1"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) zoomIn.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 250);
        }
        //компас
        if (mapViews != null && mapViews.findViewById(Integer.parseInt("1")) != null) {
            View compass = ((View) mapViews.findViewById(Integer.parseInt("1")).getParent())
                    .findViewById(Integer.parseInt("5"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) compass.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 200, 40, 0);
        }

        if(!stule_state) {
            int time = getDate();
            if (time < 7 || time > 19) {
                mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                SavesActivity.this, R.raw.style_json));
                stule_state = true;
            }
            if (time > 7 && time< 19) {
                mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                SavesActivity.this, R.raw.style_json_base));
                stule_state = true;
            }
        }


        try {
            listGet = getListFromLocal("LatLon");
                for (LatLng s : listGet) {
                    Log.e("getArrayList:", "Get ArrayList size " + s);
                    markerSave = new MarkerOptions().position(s);
                    mMap.addMarker(markerSave.icon(BitmapDescriptor.bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_record)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(s));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(s,10));
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
//        Drawable vectorDravable = ContextCompat.getDrawable(context,vectorResId);
//        vectorDravable.setBounds(0,0,vectorDravable.getIntrinsicWidth(),vectorDravable.getIntrinsicHeight());
//        Bitmap bitmap=Bitmap.createBitmap(vectorDravable.getIntrinsicWidth(),vectorDravable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        vectorDravable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }
}


//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
