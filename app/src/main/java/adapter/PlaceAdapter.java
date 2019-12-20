package adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import app.lgsocietyhub.com.nearbyplace.R;
import app.lgsocietyhub.com.nearbyplace.DetailsActivity;
import app.lgsocietyhub.com.nearbyplace.ReviewActivity;
import model.PlaceModel;
import util.CustomVolleyRequest;

/**
 * Created by mappsupport on 09-05-2018.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>
{
    List<PlaceModel> list;
    Activity activity;
    private ImageLoader imageLoader;

    public PlaceAdapter(Activity activity,List<PlaceModel> list) {
        this.list = list;
        this.activity=activity;
    }

    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_adpter_layout, viewGroup, false);
        return new PlaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceAdapter.ViewHolder viewHolder, int i) {
        final PlaceModel dataModel=list.get(i);

        viewHolder.tv_name.setText(dataModel.getName());
        viewHolder.tv_nearestplace.setText(dataModel.getNearPlace());
        viewHolder.tv_openingstatus.setText(dataModel.getOpeningStatus());
        viewHolder.ratingBar.setRating(dataModel.getRating());
        String openStatus=dataModel.getOpeningStatus();
        String imageurl=dataModel.getPlaceUrlRefe();

        if(openStatus=="true")
        {
            viewHolder.tv_openingstatus.setText("Open");
        }

        if(imageurl!=null) {
            imageLoader = CustomVolleyRequest.getInstance(activity).getImageLoader();
            imageLoader.get(dataModel.getPlaceUrlRefe(), ImageLoader.getImageListener(viewHolder.iv_placeImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
            viewHolder.iv_placeImage.setImageUrl(dataModel.getPlaceUrlRefe(), imageLoader);
        }else{
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, DetailsActivity.class);
                intent.putExtra("lat",dataModel.getCurlat());
                intent.putExtra("lng",dataModel.getCurlng());
                intent.putExtra("placename",dataModel.getName());
                activity.startActivity(intent);
            }
        });

        viewHolder.tv_viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, ReviewActivity.class);
                intent.putExtra("placeid",dataModel.getPlaceID());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name,tv_nearestplace,tv_openingstatus,tv_viewdetails;
        NetworkImageView iv_placeImage;
        CardView cardView;
        RatingBar ratingBar;

        public ViewHolder(View carView) {
            super(carView);
            tv_name=(TextView)carView.findViewById(R.id.tv_carname);
            tv_nearestplace=(TextView)carView.findViewById(R.id.tv_ownername);
            tv_openingstatus=(TextView)carView.findViewById(R.id.tv_contactnumber);
            iv_placeImage=(NetworkImageView) carView.findViewById(R.id.iv_carimage);
            ratingBar=(RatingBar) carView.findViewById(R.id.rb_rating);
            tv_viewdetails=(TextView)carView.findViewById(R.id.tv_viewdetails);
            cardView=(CardView)carView.findViewById(R.id.card_view);
        }
    }
}
