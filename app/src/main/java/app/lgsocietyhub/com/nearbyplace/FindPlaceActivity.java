package app.lgsocietyhub.com.nearbyplace;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapter.PlaceAdapter;
import model.PlaceModel;
import util.MyApplication;


public class FindPlaceActivity extends AppCompatActivity
{
    EditText et_search;
    String searchText,title,keyword,status;
    TextView tv_title;
    double lat,lon,currentlat,curremtlng;
    final String TAG = "check";
    List<PlaceModel> placeList=new ArrayList<>();
    PlaceAdapter placeAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Geocoder geocoder;
    List<Address> addresses;
    public static String destiNationaddress;

    String imageUrl="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CmRaAAAA9Yn1lY1jsu8xJQnHafG2lDSPqO0akcLs_0iXww0i01G3xpGy3ZJHSUJjWOq5FgJDXVmaWMpFiZq15fLjYAGamTZkVCi_qL3MLEGOIKlWWMhYDgvMrqyHuQEH8lQ2_Od8EhBuA3rCkucNSl_2h6wR3XmXGhQp3q1zU3K88ZeOrAslGoiZhJWQtQ&key=AIzaSyCaRLXauLXDICEqqtSvy_W7XdDxqg3QcuU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_place);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_title = (TextView) findViewById(R.id.tv_title);

        recyclerView=(RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        recyclerView=(RecyclerView) findViewById(R.id.recylerview);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            searchText = bundle.getString("hint");
            title = bundle.getString("title");
            lat=bundle.getDouble("lat");
            lon=bundle.getDouble("long");
            keyword=bundle.getString("keyword");
            et_search.setHint(searchText);
            tv_title.setText(title);
        }

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place=et_search.getText().toString().trim();
                if(place.length()!=0) {
                    if(placeAdapter==null)
                    {
                        updateList(place);
                        et_search.setText("");
                    }
                    else{
                        placeList.clear();
                        updateList(place);
                        et_search.setText("");
                    }


                }
                else
                {
                    et_search.setError("Please Enter Location");
                }
            }
        });
    }


    public void updateList(String searchValue)
    {
        progressBar.setVisibility(View.VISIBLE);
        final String pnr_url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lon+"&radius=3500&type="+searchValue+"&keyword="+searchValue+"&key=AIzaSyCaRLXauLXDICEqqtSvy_W7XdDxqg3QcuU";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, pnr_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonarray = response.getJSONArray("results");
                    JSONObject jsonobject=null;
                    System.out.println("------url----"+pnr_url);
                    for(int i=0; i < jsonarray.length(); i++)
                    {
                        PlaceModel placeModel=new PlaceModel();
                        jsonobject = jsonarray.getJSONObject(i);
                        placeModel.setName(jsonobject.getString("name"));
                        placeModel.setPlace(jsonobject.getString("icon"));
                        placeModel.setNearPlace(jsonobject.getString("vicinity"));
                        placeModel.setPlaceID(jsonobject.getString("place_id"));

                        boolean rating=jsonobject.isNull("rating");
                        if(rating)
                        {
                        }
                        else
                        {
                            placeModel.setRating(Float.parseFloat(jsonobject.getString("rating")));
                        }

                        placeModel.setOpeningStatus(status);

                        if (jsonobject.has("opening_hours")){
                            JSONObject status = jsonobject.getJSONObject("opening_hours");
                            boolean openNow=jsonobject.isNull("open_now");
                            if(openNow){
                            }else{
                                placeModel.setOpeningStatus(status.getString("open_now"));
                            }

                        }

                        if (jsonobject.has("geometry")){
                            JSONObject geometry = jsonobject.getJSONObject("geometry");
                            JSONObject location=geometry.getJSONObject("location");
                            currentlat=location.getDouble("lat");
                            curremtlng=location.getDouble("lng");
                            placeModel.setCurlat(location.getDouble("lat"));
                            placeModel.setCurlng(location.getDouble("lng"));
                        }

                        if(jsonobject.has("photos")) {
                            JSONArray photo = jsonobject.getJSONArray("photos");
                            for (int j = 0; j < photo.length(); j++) {
                                JSONObject pJsonObject = photo.getJSONObject(j);
                                placeModel.setPlaceUrlRefe("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + pJsonObject.getString("photo_reference") + "&key=AIzaSyCaRLXauLXDICEqqtSvy_W7XdDxqg3QcuU");
                            }
                        }

                        placeList.add(placeModel);
                        getDestinationAddress();
                    }
                    placeAdapter=new PlaceAdapter(FindPlaceActivity.this,placeList);
                    recyclerView.setAdapter(placeAdapter);
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error != null){
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, error.toString());
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }
        });
        MyApplication.getInstance().addToReqQueue(jsonObjectRequest,"jreq");
    }


    public void getDestinationAddress()
    {
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(currentlat, curremtlng, 1);
            destiNationaddress = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String sourceAddress = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
