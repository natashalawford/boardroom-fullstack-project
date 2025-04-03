package ca.mcgill.ecse321.boardroom.dtos.responses;

import java.time.LocalDateTime;

import ca.mcgill.ecse321.boardroom.model.Review;

public class ReviewResponseDto {

    private int id;
    private int stars;
    private String comment;
    private int authorId;
    private String boardGameName;
    private LocalDateTime timeStamp;

    @SuppressWarnings("unused")
    private ReviewResponseDto() {
    }

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.stars = review.getStars();
        this.comment = review.getComment();
        this.authorId = review.getAuthor().getId();
        this.boardGameName = review.getBoardGame().getTitle();
        this.timeStamp = review.getTimeStamp();
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timeStamp;
    }

    public int getStars() {
        return stars;
    }

    public String getComment() {
        return comment;
    }


    public int getAuthorId() {
        return authorId;
    }

    public String getBoardGameName() {
        return boardGameName;
    }
}
