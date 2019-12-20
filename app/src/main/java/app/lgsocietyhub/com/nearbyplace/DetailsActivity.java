package app.lgsocietyhub.com.nearbyplace;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import util.MyApplication;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    Polyline line;
    Context context;
    LatLng startLatLng;
    LatLng endLatLng;
    double lat,lng;
    TextView tv_distance,tv_time;
    String time,dist;

    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_second);
        context = DetailsActivity.this;

        tv_distance=(TextView)findViewById(R.id.tvDistance);
        tv_time=(TextView)findViewById(R.id.tvDuration);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
        }

        startLatLng = new LatLng(MainActivity.latitude, MainActivity.longitude);
        endLatLng = new LatLng(lat, lng);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String urlTopass = makeURL(MainActivity.latitude,MainActivity.longitude, lat,lng);
        new connectAsyncTask(urlTopass).execute();
        updateList();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myMap.setMyLocationEnabled(true);
        myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        myMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
        LatLng hcmus = new LatLng(MainActivity.latitude, MainActivity.longitude);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 16));
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                drawPath(result);
            }
        }
    }

    public String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    public class JSONParser {

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // constructor
        public JSONParser() {
        }

        public String getJSONFromUrl(String url) {
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                json = sb.toString();
                is.close();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;

        }
    }

    public void drawPath(String result) {
        if (line != null) {
            myMap.clear();
        }
        myMap.addMarker(new MarkerOptions().position(endLatLng).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.end_green)));
        myMap.addMarker(new MarkerOptions().position(startLatLng).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.start_blue)));
        try {
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                line = myMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        .width(15).color(Color.GREEN).geodesic(true));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


    public void updateList()
    {
        final String pnr_url="http://maps.googleapis.com/maps/api/directions/json?origin="+MainActivity.latitude+","+MainActivity.longitude+"&destination="+lat+","+lng+"&sensor=false&units=metric&mode=driving";
        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, pnr_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonarray = response.getJSONArray("routes");
                    JSONObject jsonobject=null;
                    for(int i=0; i < jsonarray.length(); i++)
                    {
                        jsonobject = jsonarray.getJSONObject(i);
                    }

                    JSONArray legsJsonArray=jsonobject.getJSONArray("legs");
                    JSONObject legsObject=null;
                    for(int j=0;j<legsJsonArray.length();j++)
                    {
                        legsObject=legsJsonArray.getJSONObject(j);
                        JSONObject distObj=legsObject.getJSONObject("distance");
                        JSONObject duration=legsObject.getJSONObject("duration");
                        time=duration.getString("text");
                        dist=distObj.getString("text");
                    }
                    tv_distance.setText(dist);
                    tv_time.setText(time);
                } catch (NullPointerException e) {
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error != null){
                    Log.d("DetailsActivity", error.toString());
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }
        });
        MyApplication.getInstance().addToReqQueue(jsonObjectRequest,"jreq");
    }
}
