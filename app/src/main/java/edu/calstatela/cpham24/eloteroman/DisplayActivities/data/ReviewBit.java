package edu.calstatela.cpham24.eloteroman.DisplayActivities.data;

/**
 * Created by Johnson on 8/3/2017.
 */

public class ReviewBit {

    private String reviewName;
    private String reviewRating;
    private String reviewContent;


    public ReviewBit(String rn, String rr, String rc){
        this.reviewName = rn;
        this.reviewRating = rr;
        this.reviewContent = rc;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }







}
