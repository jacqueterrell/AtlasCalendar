package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.GoogleMapLayoutBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnMarkerClickListener {

    private GoogleMapLayoutBinding binding;
    private static boolean isIndividual = true;
    private static UserProfile userProfile;
    private static List<UserProfile> userProfileList = new ArrayList<>();
    private GoogleMap mMap;


    public static Intent newIntent(Context context, UserProfile profile) {

        isIndividual = true;
        userProfile = profile;
        return new Intent(context, GoogleMapsActivity.class);
    }

    public static Intent newIntent(Context context, List<UserProfile> profileList) {

        isIndividual = false;
        userProfileList = profileList;
        return new Intent(context, GoogleMapsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.google_map_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        if (isIndividual) {

            setContactMarker();

        } else {

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (isIndividual) {

            getGeoLocation(marker.getPosition().latitude,marker.getPosition().longitude,userProfile);

        } else {

            for (UserProfile profile : userProfileList) {

                if (profile.getId().equals(marker.getTag())) {

                    getGeoLocation(marker.getPosition().latitude,marker.getPosition().longitude,profile);

                    break;
                }
            }
        }

        return false;
    }


    private void getGeoLocation(double lat, double longitude,UserProfile userProfile) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        StringBuilder sb = new StringBuilder();

        try {

            List<Address> addresses = geocoder.getFromLocation(lat, longitude, 1);
            Address address = addresses.get(0);

            if (address != null) {

                showAddressDialog(address,userProfile);
            }

        } catch (Exception e) {

        }
    }


    private void showAddressDialog(Address address,UserProfile userProfile){

        String title = userProfile.getFirstName() + " " + userProfile.getLastName();
        String city = address.getLocality();
        String state = address.getAdminArea();
        String postal = address.getPostalCode();
        String country = address.getCountryCode();
        String body = city + ", " + state + " " + postal + " " + country;

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle(title)
                .setMessage(body)
                .setPositiveButton("ok", (paramDialogInterface, paramInt) -> {

                })
                .show();
    }

    private void setContactMarker() {

        String latitude = "";
        String longitude = "";

        if (userProfile.getLastLocation().isEmpty()) {

            latitude = "-34";//sydney australia
            longitude = "151";

        } else {

            latitude = userProfile.getLastLocation().get("latitude");
            longitude = userProfile.getLastLocation().get("longitude");
        }

        if (latitude != null && longitude != null) {

            String title = "Last location for " + userProfile.getFirstName() + " " + userProfile.getLastName();
            LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(title));
            marker.setTag(userProfile.getId());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        }
    }
}
