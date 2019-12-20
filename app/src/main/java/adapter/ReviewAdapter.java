package adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import app.lgsocietyhub.com.nearbyplace.DetailsActivity;
import app.lgsocietyhub.com.nearbyplace.R;
import app.lgsocietyhub.com.nearbyplace.ReviewActivity;
import model.PlaceModel;
import model.ReviewModel;
import util.CustomVolleyRequest;

/**
 * Created by mappsupport on 15-05-2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    List<ReviewModel> list;
    Activity activity;
    private ImageLoader imageLoader;
    int count = 0;
    private Animation animationUp, animationDown;
    private final int COUNTDOWN_RUNNING_TIME = 500;
    boolean expand=false;

    public ReviewAdapter(Activity activity, List<ReviewModel> list,Animation animationUp, Animation animationDown) {
        this.list = list;
        this.activity = activity;
        this.animationUp=animationUp;
        this.animationDown=animationDown;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_adapter_layout, viewGroup, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewAdapter.ViewHolder viewHolder, int i) {
        final ReviewModel dataModel = list.get(i);

        viewHolder.tv_autherName.setText(dataModel.getUserName());
        viewHolder.tv_releventtime.setText(dataModel.getReleventTime());
        viewHolder.tv_review.setText(dataModel.getReview());
        viewHolder.ratingBar.setRating(dataModel.getRating());
        imageLoader = CustomVolleyRequest.getInstance(activity).getImageLoader();
        imageLoader.get(dataModel.getProfileImage(), ImageLoader.getImageListener(viewHolder.iv_reviewImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        viewHolder.iv_reviewImage.setImageUrl(dataModel.getProfileImage(), imageLoader);
        final int length = viewHolder.tv_review.length();

//        viewHolder.tv_review.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                count++;
//                if (count == 1) {
//                    expand(viewHolder.tv_review,2000, length+50);
//                    viewHolder.tv_review.setMaxLines(100);
//                } else {
//                    viewHolder.tv_review.setMaxLines(1);
//                    collapse(viewHolder.tv_review, 2000, 100);
//                    count = 0;
//                }
//            }
//        });

        viewHolder.tv_showMorw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!expand) {
                    expand = true;
                    ObjectAnimator animation = ObjectAnimator.ofInt(viewHolder.tv_review, "maxLines", 190);
                    animation.setDuration(100).start();
                } else {
                    expand = false;
                    ObjectAnimator animation = ObjectAnimator.ofInt(viewHolder.tv_review, "maxLines", 1);
                    animation.setDuration(100).start();
                    //btnSeeMore.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_expand));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_autherName, tv_review, tv_releventtime,tv_showMorw;
        NetworkImageView iv_reviewImage;
        CardView cardView;
        RatingBar ratingBar;

        public ViewHolder(View carView) {
            super(carView);
            tv_autherName = (TextView) carView.findViewById(R.id.tv_autherName);
            tv_releventtime = (TextView) carView.findViewById(R.id.tv_releventtime);
            tv_review = (TextView) carView.findViewById(R.id.tv_review);
            tv_showMorw = (TextView) carView.findViewById(R.id.tv_showMorw);
            iv_reviewImage = (NetworkImageView) carView.findViewById(R.id.iv_reviewImage);
            cardView = (CardView) carView.findViewById(R.id.card_view);
            ratingBar = (RatingBar) carView.findViewById(R.id.rb_rating);
        }
    }


    public static void expand(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();
        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void collapse(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

}
