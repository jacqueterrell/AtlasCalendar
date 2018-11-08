package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.data.model.local.ContinentLocation;
import com.team.mamba.atlascalendar.databinding.GoogleMapLayoutBinding;
import com.team.mamba.atlascalendar.utils.CommonUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;

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

        if (userProfile == null){

            finish();

        } else {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        if (isIndividual) {

            setContactMarker();

        } else {

            setFavoritesMarker();
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

        try {

            List<Address> addresses = geocoder.getFromLocation(lat, longitude, 1);
            Address address = addresses.get(0);

            if (address != null) {

                showAddressDialog(address,userProfile);
            }

        } catch (Exception e) {

            Logger.e(e.getMessage());
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

        String latitude;
        String longitude;

            latitude = userProfile.getLastLocation().get("latitude");
            longitude = userProfile.getLastLocation().get("longitude");

            if (latitude != null && longitude != null) {

                String title = "Last location for " + userProfile.getFirstName() + " " + userProfile.getLastName();
                LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(title));
                marker.setTag(userProfile.getId());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            }

    }


    private void setFavoritesMarker(){

        //the include method will calculate the min and max bound.
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        List<LatLng> locationList = new ArrayList<>();

        for (UserProfile profile : userProfileList){

            if (!profile.getLastLocation().isEmpty()
                    && !profile.isIsVacationMode()
                    && !profile.isIsPrivacyMode()){

               String latitude = profile.getLastLocation().get("latitude");
               String longitude = profile.getLastLocation().get("longitude");

                if (latitude != null && longitude != null) {

                    String title = "Last location for " + profile.getFirstName() + " " + profile.getLastName();
                    LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    locationList.add(location);
                    builder.include(location);

                    Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(title));
                    marker.setTag(profile.getId());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                }

            }
        }

        getLocationsContinent(locationList);
    }


    /**
     * Gets the continent location for each marker
     * @param locationList
     */
    private void getLocationsContinent(List<LatLng> locationList){

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<ContinentLocation> continentLocations = new ArrayList<>();

        try{

            JSONObject jsonObject = new JSONObject(CommonUtils.loadJSONFromAsset(getApplicationContext(),"json/continents.json"));

            for (LatLng location : locationList){

                    List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    Address address = addresses.get(0);

                    if (address != null) {

                       String countryCode = address.getCountryCode();
                       String continentName = jsonObject.getString(countryCode);
                       ContinentLocation contLocation = new ContinentLocation(continentName,location.latitude, location.longitude);
                       continentLocations.add(contLocation);
                    }

            }

            setMapCamera(continentLocations);

        }catch (Exception e){
            Logger.e(e.getMessage());
        }
    }

    /**
     * Sets the map camera to the continent with the most locations
     *
     * @param continentLocations the list of continentLocation objects
     */
    private void setMapCamera(List<ContinentLocation> continentLocations){

        Map<String,Integer> continentsMap = new LinkedHashMap<>();
        int continentCount = 0;
        String selectedContinent = "";

        for (ContinentLocation location : continentLocations){

            if (continentsMap.get(location.getContinent()) == null){

                continentsMap.put(location.getContinent(),1);

            } else {

                int value = continentsMap.get(location.getContinent());
                continentsMap.put(location.getContinent(),value + 1);
            }
        }

        for (Map.Entry<String,Integer> entry : continentsMap.entrySet()){

            String continentName = entry.getKey();
            int count = entry.getValue();

            if (count > continentCount){

                continentCount = count;
                selectedContinent = continentName;
            }
        }

        if (!selectedContinent.isEmpty()){

            for (ContinentLocation location : continentLocations){

                if (location.getContinent().equals(selectedContinent)){

                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    return;

                }
            }
        }
    }
}
