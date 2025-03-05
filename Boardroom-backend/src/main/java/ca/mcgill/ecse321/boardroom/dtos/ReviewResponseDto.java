package ca.mcgill.ecse321.boardroom.dtos;

import ca.mcgill.ecse321.boardroom.model.Review;

public class ReviewResponseDto {

    private int id;
    private int stars;
    private String comment;
    private int authorId;
    private String boardGameName;

    @SuppressWarnings("unused")
    private ReviewResponseDto() {
    }

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.stars = review.getStars();
        this.comment = review.getComment();
        this.authorId = review.getAuthor().getId();
        this.boardGameName = review.getBoardGame().getTitle();
    }

    public int getId() {
        return id;
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
