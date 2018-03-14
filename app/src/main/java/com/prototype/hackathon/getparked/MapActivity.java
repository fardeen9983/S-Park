package com.prototype.hackathon.getparked;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, LocationListener
        , GoogleApiClient.ConnectionCallbacks ,GoogleApiClient.OnConnectionFailedListener{


    //Firebase
    private FirebaseAuth auth;

    //Permissions
    private final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final int FINE_LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final int COARSE_LOCATION_PERMISSION_REQUEST_CODE = 2;
    private boolean locPermission;

    //Map variables
    private GoogleMap mGoogleMap;
    private Marker marker;

    //API
    private GoogleApiClient googleApiClient;

    //Display objects
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private AutoCompleteTextView autoCompleteTextView;
    private LinearLayout searchLayout;
    private ImageView recenter;
    private ImageView details;

    //Location Variable
    private double radius;
    public static Location location;
    private LatLng current;
    private Marker Dest;
    private List<Marker> markerList;
    private FusedLocationProviderClient fusedLocationProviderClient;

    //Location requests
    private LocationRequest locationRequest;
    private int locPriority;
    private final int PRIORITY_BALANCED_POWER_ACCURACY = 2;
    private final int PRIORITY_HIGH_ACCURACY = 3;
    private final int PRIORITY_LOW_POWER = 1;
    private final int PRIORITY_NO_POWER = 0;

    //Map elements
    private final float DEFAULT_ZOOM = 15.0f;
    private ArrayList<SpotDetails> spotDetailsList;
    //Places
    private String placeID;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private final LatLngBounds LATLNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private Place place;
    private List<Address> addresses;
    private AutocompletePrediction prediction;

    //Listener
    private AdapterView.OnItemClickListener adapterClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            hideKeyboard();
            prediction = placeAutocompleteAdapter.getItem(position);
            placeID = prediction.getPlaceId();
            PendingResult<PlaceBuffer> placeBufferPendingResult = Places.GeoDataApi.getPlaceById(googleApiClient, placeID);
            placeBufferPendingResult.setResultCallback(placeBufferResultCallback);
        }
    };

    private ResultCallback<PlaceBuffer> placeBufferResultCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "Didn't get the places" + places.getStatus().toString());
                places.release();
                return;
            }
            place = places.get(0);
            moveCamera(place.getLatLng(), DEFAULT_ZOOM, place.getName().toString());
            places.release();
        }
    };
    //Miscellaneous
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        spotDetailsList = (ArrayList<SpotDetails>)getIntent().getSerializableExtra("spot");
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API).build();
        locPermission = false;
        radius=10;
        setUpDisplay();
        locPriority = PRIORITY_BALANCED_POWER_ACCURACY;
        checkPermissions();
    }

    private void hideKeyboard(){
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (methodManager != null) {
            methodManager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
        }
    }

    private void setUpDisplay() {

        markerList = new ArrayList<>();

        toolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        searchLayout = findViewById(R.id.search_layout);
        searchLayout.setAlpha(0.65f);

        details =findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DetailsActivity.class));
            }
        });

        if(spotDetailsList!=null){
            for(SpotDetails spotDetails : spotDetailsList){
                LatLng latLng= new LatLng(spotDetails.getLatitude(),spotDetails.getLongitude());
                Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(spotDetails.getAddress()));
                markerList.add(marker);
            }
        }
        autoInit();

        recenter = findViewById(R.id.reset);
        recenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

    }

    private void autoInit(){
        autoCompleteTextView = findViewById(R.id.search_auto);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        AutocompleteFilter filter = new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_COUNTRY).setCountry("IN").build();
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient, LATLNG_BOUNDS, filter);
        autoCompleteTextView.setOnItemClickListener(adapterClickListener);
        autoCompleteTextView.setAdapter(placeAutocompleteAdapter);
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == KeyEvent.ACTION_DOWN
                        || actionId == KeyEvent.KEYCODE_ENTER) {
                    hideKeyboard();
                    moveToSearchedAddress(v.getText().toString());

                }
                return false;
            }
        });
    }

    private void moveToSearchedAddress(String query) {
        if (query == null)
            return;
        autoCompleteTextView.clearListSelection();
        Geocoder geocoder = new Geocoder(this);
        try {
            addresses = geocoder.getFromLocationName(query, 5);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            if (addresses.get(0) != null) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                moveCamera(latLng, DEFAULT_ZOOM, address.getAddressLine(0));
            }
        }

    }
    private void getDirections(){

    }
    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{COARSE_LOCATION}, COARSE_LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                locPermission = true;
                initMap();
            }
        } else
            ActivityCompat.requestPermissions(this, new String[]{FINE_LOCATION}, FINE_LOCATION_PERMISSION_REQUEST_CODE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locPermission = false;
        switch (requestCode) {
            case FINE_LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        locPermission = false;
                        return;
                    } else {
                        locPermission = true;
                        initMap();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initMap() {
        googleApiClient.connect();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getCurrentLocation() {
        if (locPermission) {
            mGoogleMap.clear();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkPermissions();
            } else {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()&&task.getResult()!=null){
                          location = task.getResult();
                          current = new LatLng(location.getLatitude(),location.getLongitude());
                          moveCamera(current,DEFAULT_ZOOM);
                        }
                    }
                });
            }
        }
    }


    private void moveCamera(LatLng latLng,float zoom){
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    private void moveCamera(LatLng latLng,float zoom,String desc){
        marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(desc));
        markerList.add(marker);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.nav_account :
                startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                finish();
                break;
            case R.id.nav_settings :
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));

                break;
            case R.id.nav_logout :
                auth = FirebaseAuth.getInstance();
                if(auth.getCurrentUser()!=null) {
                    auth.signOut();
                }
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;
        }
        return false;
    }


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mGoogleMap == null) {
            mGoogleMap = googleMap;
        }
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (locPermission) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkPermissions();
            } else {
                getCurrentLocation();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest  = LocationRequest.create();
        locationRequest.setPriority(locPriority);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    private void createLocationRequest(){

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
