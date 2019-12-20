package app.lgsocietyhub.com.nearbyplace;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.PlaceAdapter;
import adapter.ReviewAdapter;
import model.PlaceModel;
import model.ReviewModel;
import util.MyApplication;

public class ReviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    String placeId;
    List<ReviewModel> reviewList=new ArrayList<>();
    ReviewAdapter reviewAdapter;
    private Animation animationUp, animationDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            placeId = bundle.getString("placeid");
        }

        recyclerView=(RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        recyclerView=(RecyclerView) findViewById(R.id.recylerview);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        updateList(placeId);
    }


    public void updateList(String placeId)
    {
        final String pnr_url="https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeId+"&key=AIzaSyCaRLXauLXDICEqqtSvy_W7XdDxqg3QcuU";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, pnr_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("------reviewurl----"+pnr_url);
                    JSONObject jsonObject = response.getJSONObject("result");
                    JSONArray reviewArray=jsonObject.getJSONArray("reviews");
                    for(int i=0;i<reviewArray.length();i++)
                    {
                        ReviewModel reviewModel=new ReviewModel();
                        JSONObject revObject=reviewArray.getJSONObject(i);
                        reviewModel.setUserName(revObject.getString("author_name"));
                        reviewModel.setProfileImage(revObject.getString("profile_photo_url"));
                        reviewModel.setReleventTime(revObject.getString("relative_time_description"));
                        reviewModel.setReview(revObject.getString("text"));
                        System.out.println("----review---"+revObject.getString("text"));
                        reviewModel.setRating(Float.parseFloat(revObject.getString("rating")));
                        reviewList.add(reviewModel);
                    }
                    reviewAdapter=new ReviewAdapter(ReviewActivity.this,reviewList,animationUp, animationDown);
                    recyclerView.setAdapter(reviewAdapter);
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
                    Log.d("TAG", error.toString());
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }
        });
        MyApplication.getInstance().addToReqQueue(jsonObjectRequest,"jreq");
    }
}
