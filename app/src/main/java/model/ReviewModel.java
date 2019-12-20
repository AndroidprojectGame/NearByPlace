package model;

/**
 * Created by mappsupport on 15-05-2018.
 */

public class ReviewModel
{
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String profileImage;
    String userName;

    public String getReleventTime() {
        return releventTime;
    }

    public void setReleventTime(String releventTime) {
        this.releventTime = releventTime;
    }

    String releventTime;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    String review;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    float rating;
}
