package com.pointer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.pointer.Calendar.getDate;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    boolean stule_state = false;
    boolean mode_state = false;
    boolean is_started = false;
//    int curentDate;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationSettingsRequest locationSettingsRequest;
    Location currentLocation;
    ImageView style_mode, go_mode;
    TextView distance_display;
    Button polilineBuilder, layers;
    Marker myMarker, myMarker1;
    // private int mapType = 0;
    // public static  final String MAP_TYPE = "MAP_TYPE";
    public static final int PERMISSIONS_REQUEST = 123;
    public Double distanses;
    Double km;
    String result;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public int style = R.raw.style_json;
    public AdView mAdView;
    String mode = "walking";
    private static final String TAG = null;
    ArrayList<LatLng> listGet = new ArrayList<>();
    ArrayList<LatLng> listSave = new ArrayList<>();
    private GoogleMap mMap;
    double lat, lon;
    ImageButton mapView, btnGetLoc, getWay, car, mapTopmap, sputnik, driving_mode, walking_mode, styles_day, styles_night;
    //рисуем линию
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    //
    private View mapViews;

    Button polilineBuilderStop, btn_save_location;

    private SharedPreferences sharedPreferences;


    //если что удалить
    SupportMapFragment mapFragment;
    SearchView searchView;

    List<LatLng> LLLL = new ArrayList<>();
    List<LatLng> LatLon = new ArrayList<>();
    Polyline poliline = null;


//    private ConsentInformation consentInformation;
//    private ConsentForm consentForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            window.setNavigationBarColor(Color.YELLOW);
        }


        //ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);

        buildLocationRequest();
//        getCurrentLocation();


        //рекламный банер
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-5043630913510790/1504857082");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //реклама
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        //


//        searchView = findViewById(R.id.sv_location);
//        driving_mode = (ImageButton) findViewById(R.id.driving_mode);
//        walking_mode = (ImageButton) findViewById(R.id.walking_mode);
//        sputnik = (ImageButton) findViewById(R.id.sputnik);
//        mapTopmap = (ImageButton) findViewById(R.id.mapTopmap);
//        btnGetLoc = (ImageButton) findViewById(R.id.btnGetLoc);
//        car = (ImageButton) findViewById(R.id.car);
//        mapView = (ImageButton) findViewById(R.id.mapView);
//        getWay = (ImageButton) findViewById(R.id.getWay);
//        styles_day = (ImageButton) findViewById(R.id.styles_day);
//        styles_night = (ImageButton) findViewById(R.id.styles_night);
//        style_mode = (ImageView) findViewById(R.id.style_mode);
//        go_mode = (ImageView) findViewById(R.id.go_mode);
//        polilineBuilder = (Button) findViewById(R.id.polilineBuilder);
//        polilineBuilderStop = (Button) findViewById(R.id.polilineBuilderStop);
//        distance_display = (TextView) findViewById(R.id.distance_display);

        layers = findViewById(R.id.layers);
        btn_save_location = findViewById(R.id.save_button_location);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        layers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapsActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_layers);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.show();

                Button layer_sput = bottomSheetDialog.findViewById(R.id.layer_sput);
                Button layer_shem = bottomSheetDialog.findViewById(R.id.layer_shema);
                Button layer_probki = bottomSheetDialog.findViewById(R.id.layer_probki);

                Button color_layer_standart = bottomSheetDialog.findViewById(R.id.color_layer_standart);
                Button color_layer_yelow = bottomSheetDialog.findViewById(R.id.color_layer_yellow);
                Button color_layer_blue = bottomSheetDialog.findViewById(R.id.color_layer_blue);
                Button color_layer_black = bottomSheetDialog.findViewById(R.id.color_layer_black);

                layer_sput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    }
                });

                layer_shem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    }
                });

                layer_probki.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mMap.isTrafficEnabled()) {
                            mMap.setTrafficEnabled(true);
                        } else {
                            mMap.setTrafficEnabled(false);
                        }

                    }
                });


                color_layer_standart.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onClick(View v) {
                        mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        MapsActivity.this, R.raw.style_json_base));
                        setLayerStyle(0);
                    }
                });


                color_layer_yelow.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onClick(View v) {
                        mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        MapsActivity.this, R.raw.style_yellow));
                        setLayerStyle(1);
                    }
                });

                color_layer_blue.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onClick(View v) {
                        mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        MapsActivity.this, R.raw.style_json));
                        setLayerStyle(2);
                    }
                });

                color_layer_black.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onClick(View v) {
                        mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        MapsActivity.this, R.raw.style_dark));
                        setLayerStyle(3);
                    }
                });

            }
        });

        btn_save_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapsActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_save_position);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.show();

                Button layer_sput = bottomSheetDialog.findViewById(R.id.layer_sput);
                Button layer_shem = bottomSheetDialog.findViewById(R.id.layer_shema);
                Button layer_probki = bottomSheetDialog.findViewById(R.id.layer_probki);

            }
        });


//        polilineBuilder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (is_started == false) {
//                    is_started = true;
//                    onClickStart();
//                    polilineBuilder.setVisibility(View.GONE);
//                    polilineBuilderStop.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        polilineBuilderStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (is_started == true) {
//                    is_started = false;
//                    polilineBuilder.setVisibility(View.VISIBLE);
//                    polilineBuilderStop.setVisibility(View.GONE);
//                }
//
//            }
//        });


//        btnGetLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                GPStracer g = new GPStracer(getApplicationContext());
////                Location l = g.getLocation();
//                getCurrentLocation();
//                buildLocationRequest();
//                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//
//                if (latLng != null) {
//                    if (place2 != null)
//                        myMarker1.remove();
//                    if (currentPolyline != null)
//                        currentPolyline.remove();
//                    lat = currentLocation.getLatitude();
//                    lon = currentLocation.getLongitude();
//                    place2 = new MarkerOptions().position(new LatLng(lat, lon)).icon(BitmapDescriptor.bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_person_pin_24));
//                    myMarker1 = mMap.addMarker(place2);
//                    //Alert
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
//                    //заменить переменной настроек, языковое расширение
//                    builder.setMessage(R.string.SavePos).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            saveGeolocation();
//                            FancyToast.makeText(MapsActivity.this, getString(R.string.save_position), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
//                        }
//                    }).setNegativeButton(R.string.no, null);
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//
//
//                } else {
//                    //Вывод сообщения об отсутствии сигнала
////                    Toast.makeText(getApplicationContext(), "GPS сигнал отсутствует", Toast.LENGTH_LONG).show();
//                    FancyToast.makeText(MapsActivity.this, getString(R.string.gps_signal), FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
//                }
//
//            }
//        });

//        driving_mode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mode = "driving";
//                go_mode.setImageDrawable(getDrawable(R.drawable.ic_baseline_drive_eta_24));
//                if (place1 != null && place2 != null)
//                    new FetchURL(MapsActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), mode), mode);
//            }
//        });
//
//
//        styles_day.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                style_mode.setImageDrawable(getDrawable(R.drawable.ic_baseline_wb_sunny_24));
//                mMap.setMapStyle(
//                        MapStyleOptions.loadRawResourceStyle(
//                                MapsActivity.this, R.raw.style_json_base));
//            }
//        });
//        styles_night.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                style_mode.setImageDrawable(getDrawable(R.drawable.ic_baseline_brightness_3_24));
//                mMap.setMapStyle(
//                        MapStyleOptions.loadRawResourceStyle(
//                                MapsActivity.this, R.raw.style_json));
//            }
//        });


//        walking_mode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mode = "walking";
//                go_mode.setImageDrawable(getDrawable(R.drawable.ic_baseline_directions_walk_24));
//                if (place1 != null && place2 != null)
//                    new FetchURL(MapsActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), mode), mode);
//            }
//        });
//
//
//        //switch map/sputnik
//        mapTopmap.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            }
//
//        });
//        sputnik.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//            }
//
//        });


//        car.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GPStracer g = new GPStracer(getApplicationContext());
//                Location l = g.getLocation();
//                getCurrentLocation();
//                buildLocationRequest();
//                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//
//                //проверка доступность координат
//                if (latLng != null) {
//                    if (place2 != null) {
//                        if (place1 != null) {
//                            myMarker.remove();
//                        }
//                        if (currentPolyline != null)
//                            currentPolyline.remove();
//                        place1 = new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("you this now").visible(true);
//                        myMarker = mMap.addMarker(place1);
//                        new FetchURL(MapsActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), mode), mode);
//
//                        distanses = SphericalUtil.computeDistanceBetween(place1.getPosition(), place2.getPosition());
//                        km = distanses / 1000;
//                        result = String.format("%.3f", km);
//                        distance_display.setText(result + " km");
//
//                    } else {
////                        Toast.makeText(getApplicationContext(), "задайте начальну точку", Toast.LENGTH_LONG).show();
//                        FancyToast.makeText(MapsActivity.this, getString(R.string.start_point), FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
//                    }
//                } else {
//                    //Вывод сообщения об отсутствии сигнала
//                    FancyToast.makeText(MapsActivity.this, getString(R.string.gps_signal), FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
//                }
//            }
//
//        });

//        mapView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MapsActivity.this, SavesActivity.class);
//                startActivity(intent);
//            }
//        });


        //перенаправление камеры на авто
//        getWay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                //Alert
//                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
//                //заменить переменной настроек, языковое расширение
//                builder.setMessage(R.string.delite_message).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mMap.clear();
//                        LatLon.clear();
//                        distance_display.setText("");
//
//                        FancyToast.makeText(MapsActivity.this, getString(R.string.map_clear), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
//                    }
//                }).setNegativeButton(R.string.no, null);
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//            }
//        });


        //удалить если что
        //поиск городов
//        searchView.setFocusable(false);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                String location = searchView.getQuery().toString();
//                List<Address> addressList = null;
//                try {
//                    if (location != null || !location.equals("")) {
//                        Geocoder geocoder = new Geocoder(MapsActivity.this);
//                        try {
//                            addressList = geocoder.getFromLocationName(location, 1);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        Address address = addressList.get(0);
//                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                        //mMap.addMarker(new MarkerOptions().position(latLng).title(location));
//                        if (place1 != null) {
//                            myMarker.remove();
//                        }
//                        if (myMarker1 != null)
//                            myMarker1.remove();
//                        if (currentPolyline != null)
//                            currentPolyline.remove();
//                        place2 = (new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
//                        myMarker1 = mMap.addMarker(place2);
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    FancyToast.makeText(MapsActivity.this, getString(R.string.adress_null), FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapTop);
////        mapFragment.getMapAsync(this);
        mapViews = mapFragment.getView();
    }



//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        is_started = false;
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);

        switch (requestCode) {
            case 101:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getCurrentLocation();
////
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
////                        Toast.makeText(MapsActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }

    }


//    public void saveGeolocation() {
//        new Thread(new Runnable() {
//            public void run() {
//                SharedPreferences prefs = getSharedPreferences("AppName", Context.MODE_PRIVATE);
//                String isSave = prefs.getString("LatLon", null);
//                if (isSave != null) {
//                    listSave = getListFromLocal("LatLon");
//                }
//                listSave.add(place2.getPosition());
//                saveListInLocal(listSave, "LatLon");
//                Log.e("saveArrayList:", "Save ArrayList success" + listGet.size());
//            }
//        }).start();
//    }

        public void getSettingsGPS() {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(2000);
            mLocationRequest.setSmallestDisplacement(0);
            mLocationRequest.setFastestInterval(0);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new
                    LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);

            Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

            task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            MapsActivity.this,
                                            101);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                }
            });
        }





        private void getCurrentLocation () {
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnable) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSIONS_REQUEST);
                } else {
                    fusedLocationProviderClient.getLastLocation()
                            .addOnCompleteListener(new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    if (task.isSuccessful()) {
                                        currentLocation = task.getResult();
                                        if (currentLocation != null) {
//                                        getCurrentLocation();
                                            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTop);
                                            supportMapFragment.getMapAsync(MapsActivity.this);
                                        } else {
                                            buildLocationRequest();
                                            buildLocationCallBackWithMovingCameraToLastKnownLocationWithZoom();
                                            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    ActivityCompat#requestPermissions
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for ActivityCompat#requestPermissions for more details.
                                                return;
                                            }
                                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                                            getCurrentLocation();
                                        }
                                    } else {
                                        getCurrentLocation();
                                    }
                                }
                            });
                    //было в комментах
//            Task<Location> task = fusedLocationProviderClient.getLastLocation();
//            task.addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        currentLocation = location;
//                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTop);
//                        supportMapFragment.getMapAsync(MapsActivity.this);
//                    } else {
//                        buildLocationRequest();
//                        getDeviceLocation();
//                    }
//                }
//            });
            buildLocationRequest();
            buildLocationCallBackWithMovingCameraToLastKnownLocationWithZoom();

                }
            } else {
                getSettingsGPS();
            }
        }


//было в комментах
//    @SuppressLint("MissingPermission")
//    private void getDeviceLocation() {
//        fusedLocationProviderClient.getLastLocation()
//                .addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            currentLocation = task.getResult();
//                            if (currentLocation != null) {
//                                getCurrentLocation();
//                                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTop);
//                                supportMapFragment.getMapAsync(MapsActivity.this);
//                            } else {
//                                buildLocationRequest();
//                                buildLocationCallBackWithMovingCameraToLastKnownLocationWithZoom();
//                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//                            }
//                        } else {
//                            Toast.makeText(MapsActivity.this, "Unable to get last location", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }

        private void buildLocationCallBackWithMovingCameraToLastKnownLocationWithZoom () {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    currentLocation = locationResult.getLastLocation();
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                }
            };
        }


//    public void onClickStart(){
//        if(is_started == true) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (is_started) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                buildLocationRequest();
//                                getCurrentLocation();
//                                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                                LatLon.add(latLng);
//                                if (poliline != null) poliline.remove();
//                                PolylineOptions polylineOptions = new PolylineOptions()
//                                        .clickable(true)
//                                        .addAll(LatLon);
//                                mMap.addPolyline(polylineOptions);
//                                Log.d("LogetThtead", "add");
//                                for (LatLng s : LatLon) {
//                                    Log.d("LogetThteadsss", String.valueOf(s));
//                                }
//                            }
//                        });
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();
//        }
//
//    }
//
//    //save list Array maps object
//    public void saveListInLocal(ArrayList<LatLng> list, String key) {
//
//        SharedPreferences prefs = getSharedPreferences("AppName", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        editor.putString(key, json);
//        editor.apply();     // This line is IMPORTANT !!!
//
//    }
//
//    //geting list Array maps jbject
//    public ArrayList<LatLng> getListFromLocal(String key)
//    {
//        SharedPreferences prefs = getSharedPreferences("AppName", Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<LatLng>>() {}.getType();
//        return gson.fromJson(json, type);
//
//    }



        @Override
        @SuppressWarnings("MissingPermission")
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;


//        setModeGo();
          style_states();


            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//            mMap.getUiSettings().setMyLocationButtonEnabled(true);
//            mMap.getUiSettings().setZoomControlsEnabled(true);

//            mMap.setOnMyLocationButtonClickListener(this);
//            mMap.setOnMyLocationClickListener(this);

            //переопределение положения кнопки навигации
//            if (mapViews != null && mapViews.findViewById(Integer.parseInt("1")) != null) {
//                View locationButton = ((View) mapViews.findViewById(Integer.parseInt("1")).getParent())
//                        .findViewById(Integer.parseInt("2"));
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//                layoutParams.setMargins(0, 0, 40, 550);
//            }
//            //переопределение положения зума
//            if (mapViews != null && mapViews.findViewById(Integer.parseInt("1")) != null) {
//                View zoomIn = ((View) mapViews.findViewById(Integer.parseInt("1")).getParent())
//                        .findViewById(Integer.parseInt("1"));
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) zoomIn.getLayoutParams();
//                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//                layoutParams.setMargins(0, 0, 40, 250);
//            }
//            //компас
//            if (mapViews != null && mapViews.findViewById(Integer.parseInt("1")) != null) {
//                View compass = ((View) mapViews.findViewById(Integer.parseInt("1")).getParent())
//                        .findViewById(Integer.parseInt("5"));
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) compass.getLayoutParams();
//                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//                layoutParams.setMargins(0, 200, 40, 0);
//            }
        }

//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    public void setModeGo(){
//        style_state();
//        if(!mode_state) {
//            if(mode.equals("driving"))
//                mode_state = true;
//            go_mode.setImageDrawable(getDrawable(R.drawable.ic_baseline_drive_eta_24));
//        }
//        if(mode.equals("walking")) {
//            mode_state = true;
//            go_mode.setImageDrawable(getDrawable(R.drawable.ic_baseline_directions_walk_24));
//        }
//    }
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    public void style_state(){
//        if(!stule_state) {
//            int time = getDate();
//            if (time < 7 || time > 19) {
//                mMap.setMapStyle(
//                        MapStyleOptions.loadRawResourceStyle(
//                                MapsActivity.this, R.raw.style_json));
//                stule_state = true;
//                style_mode.setImageDrawable(getDrawable(R.drawable.ic_baseline_brightness_3_24));
//            }
//            else{
//                mMap.setMapStyle(
//                        MapStyleOptions.loadRawResourceStyle(
//                                MapsActivity.this, R.raw.style_json_base));
//                stule_state = true;
//                style_mode.setImageDrawable(getDrawable(R.drawable.ic_baseline_wb_sunny_24));
//            }
//        }
//    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void style_states(){

        int saveSharedPref = (sharedPreferences.getInt("color_layer", 0));

        if (saveSharedPref == 0) {
            mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.style_json_base));
        }
        else if (saveSharedPref == 1 ) {
            mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.style_yellow));
        }
        else if (saveSharedPref == 2) {
            mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.style_json));
        }
        else if (saveSharedPref == 3) {
            mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.style_dark));
        }
        else{
            mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.style_json_base));
        }

    }

    private void setLayerStyle(int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("color_layer", value);
        editor.apply();
    }




    //получение значений json, рисуем
//    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//        // Mode
//        String mode = "mode=" + directionMode;
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + mode;
//        // Output format
//        String output = "json";
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.map_key);
//        return url;
//    }


        @Override
        public void onRequestPermissionsResult ( int requestCode, String[] permissions,
        int[] grantResults){
            switch (requestCode) {
                case PERMISSIONS_REQUEST: {
                    if (grantResults.length > 0
                            && grantResults[0] == RESULT_OK) {
                        getCurrentLocation();
                        break;
                    } else {
                        getCurrentLocation();
                    }
                    return;
                }
            }
        }


        private void buildLocationSettingsRequest () {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            locationSettingsRequest = builder.build();
        }


        private void buildLocationRequest () {
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(2000);
            locationRequest.setFastestInterval(0);
        }


//        @Override
//        public boolean onMyLocationButtonClick () {
////        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
////                .show();
//            // Return false so that we don't consume the event and the default behavior still occurs
//            // (the camera animates to the user's current position).
//            return false;
//        }


//    @Override
//    public void onTaskDone(Object... values) {
//        if (currentPolyline != null)
//            currentPolyline.remove();
//        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
//    }

//    @Override
//    public void onMyLocationClick(@NonNull Location location) {
////        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG)
////                .show();
//    }


    }


