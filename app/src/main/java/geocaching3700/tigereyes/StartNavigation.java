package geocaching3700.tigereyes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Kara on 9/8/2014.
 */
public class StartNavigation extends Activity {

        private LocationManager mgr=null;
        private LocationListener listener = null;
        private Location lastKnownLocation = null;
    private SharedPreferences settings;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.start_navigation);
            mgr=(LocationManager)getSystemService(LOCATION_SERVICE);
            listener = new MyLocationListener();
            mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,listener);
            settings = getSharedPreferences("settings", MODE_WORLD_WRITEABLE);
            float destLat = settings.getFloat("destLatitude", 0);
            float destLon = settings.getFloat("destLongitude", 0);
            TextView destCoords = (TextView) findViewById(R.id.destcoords);
            String destCoordsString = "Latitude: " + Float.toString(destLat) + ", Longitude: " + Float.toString(destLon);
            destCoords.setText(destCoordsString);
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean enabled = service
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // check if GPS is enabled and if not send user to the GSP settings
            // Better solution would be to display a dialog and suggesting to
            // go to the settings. Will implement later
            if (!enabled) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }


            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private class MyLocationListener implements LocationListener{

            @Override
            public void onLocationChanged(final Location location) {
                lastKnownLocation = location;

                String longitude = "Longitude: " + location.getLongitude();
                //Log.v(TAG, longitude);
                String latitude = "Latitude: " + location.getLatitude();
                //Log.v(TAG, latitude);
                TextView currentCoords = (TextView) findViewById(R.id.currentCoordText);
                currentCoords.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());


                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putFloat("currentLatitude", (float) location.getLatitude());
                prefEditor.putFloat("currentLongitude", (float) location.getLongitude());
                prefEditor.commit();
            }

            @Override
            public void onProviderDisabled(final String provider) {

            }
            @Override
            public void onProviderEnabled(final String provider) {

            }

            @Override
            public void onStatusChanged(final String provider, final int status, final Bundle extras) {

            }
        };
    }


