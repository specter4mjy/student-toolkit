package ml.mk.jm.ay.ak.studenttoolkit.map;

/**
 * Created by Ahmed.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URLEncoder;

import ml.mk.jm.ay.ak.studenttoolkit.R;

//class MapActivity lunch by Intent with an extra-text attached, that extra is a room number that this class responsible to show it's location on a map.
//so this class will call to create data base, setup a map, and put marker on a specific room loacation.
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //Create google map object
    private GoogleMap mMap;

    //Create DatabaseHandler Object to call and create the data base
    DatabaseHandler db = new DatabaseHandler(this);

    @Override //onCreate method will make sure that the map is been setup and call the getMapAsync to register the callback.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(savedInstanceState == null) {
            setUpMapIfNeeded();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override //onResume call setUpMapIfNeeded to check if the map not been setup.
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override //onMapReady will (1)set the user location, (2)call db.createDatabase(this); to create the data base, (3) extract the intent to  get the room
    //number, (4) look for the location of that room in the data base (5)if the location found, put a marker on it otherwise show toast that the room location
    //not a available.
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true); //set the user location
        LatLng ucd = new LatLng(53.3068254,-6.2212337); //save the coordinate of the cerner point of the ucd as defult value in case the room position not available.
        db.createDatabase(this); //create the data base

        //Extract intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String roomNumber = extras.getString("EXTRA_TEXT");

        Location loca = db.getLocation(roomNumber); //look for the room location in the data base.
        if (loca != null) {
            ucd = new LatLng(Double.parseDouble(loca.room_lat), Double.parseDouble(loca.room_lng)); //save room coordinate in the ucd (LatLng object)
            map.addMarker(new MarkerOptions().position(ucd).title("Room: " + roomNumber)); //add the room name to the marker
        }
        else{
            String query = URLEncoder.encode(roomNumber);
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + query);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
                CharSequence text = this.getString(R.string.lunch_google_map); //showing toast in case the room location not available
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(this, text, duration).show();
            }
            else{
                CharSequence text = this.getString(R.string.pos_not_available_err); //showing toast in case the room location not available
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(this, text, duration).show();
            }
        }
        //move and zoom the camera to the room location (or to the default location if the room location not available).
        CameraUpdate center= CameraUpdateFactory.newLatLng(ucd);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo((float) 17.5);
        map.moveCamera(center);
        map.animateCamera(zoom);
    }

    //setUpMapIfNeeded method will chcek and setup the map if it is not been set yet.
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }
}
