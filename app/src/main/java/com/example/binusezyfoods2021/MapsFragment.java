package com.example.binusezyfoods2021;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

//import android.support.v4.app.FragmentActivity;

public class MapsFragment extends AppCompatActivity implements OnMapReadyCallback {

    public static Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    static float size = 15.0f;
    public static LatLng pickCoor;
    DatabaseHelper mDatabaseHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_layout);

//        Mengubah title toolbar
        TextView myTitleText = (TextView) findViewById(R.id.homeTitle);
        myTitleText.setText("Binus EzyFoody: Location");

//        Mengubah tulisan button pada pada toolbar
        Button barBtn = (Button) findViewById(R.id.homeBtn);
        barBtn.setText("Choose\nLocation");

        barBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNearest();
            }
        });

        ImageButton myImgBtn = (ImageButton) findViewById(R.id.backBtn);
        myImgBtn.setVisibility(View.VISIBLE);

        myImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainMenu();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

    }

    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;

                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude()
                            +", "+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsFragment.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor dataBranch = mDatabaseHelper.getData("branch");
        Cursor dataUser = mDatabaseHelper.getData("user");

        //        0 karena pasti akan terganti valuenya jika pickcoor masih null (belum ada lokasi yg terpilih)
        float nearestDistance = 0;

        dataUser.moveToFirst();


        while (dataBranch.moveToNext()) {
            LatLng restoCoor = new LatLng(dataBranch.getDouble(2), dataBranch.getDouble(3));
            MarkerOptions markerOptions = new MarkerOptions().position(restoCoor).title(dataBranch.getString(1));
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(restoCoor));
            googleMap.addMarker(markerOptions);
            if(dataUser.getInt(3)==-1) {
                float result[] = new float[10];
                Location.distanceBetween(MapsFragment.currentLocation.getLatitude(), MapsFragment.currentLocation.getLongitude(), dataBranch.getDouble(2), dataBranch.getDouble(3), result);

                if (pickCoor == null || result[0] < nearestDistance) {
                    nearestDistance = result[0];
                    pickCoor = restoCoor;
                    TextView locText = (TextView) findViewById(R.id.chosenLoc);
                    locText.setText("Chosen Location: " + dataBranch.getString(1));
                    mDatabaseHelper.updateName("user", "last_restaurant", dataBranch.getInt(0), 1);
                }
            }
            else{
                Cursor chosenData = mDatabaseHelper.getItemByID("branch", dataUser.getInt(3));
                chosenData.moveToFirst();

                pickCoor = new LatLng(chosenData.getDouble(2), chosenData.getDouble(3));
                TextView locText = (TextView) findViewById(R.id.chosenLoc);
                locText.setText("Chosen Location: " + chosenData.getString(1));
            }

        }



        final LatLng currCoor = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(currCoor).icon(BitmapDescriptorFactory.defaultMarker(220)).title("Your Location");

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(currCoor));
        googleMap.addMarker(markerOptions);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickCoor, size));

        Button zoomBtn = (Button) findViewById(R.id.zoomBtn);

        zoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size += 1;
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickCoor, size));
            }
        });

        Button shrinkBtn = (Button) findViewById(R.id.shrinkBtn);

        shrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size -= 1;
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickCoor, size));
            }
        });

        Button currLocBtn = (Button) findViewById(R.id.currLocBtn);

        currLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCoor = currCoor;
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(pickCoor));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

    public void openMainMenu(){
        pickCoor = null;
        size = 15.0f;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openNearest(){
        Intent intent = new Intent(this, NearestActivity.class);
        startActivity(intent);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        openMainMenu();
    }
}