package ca.mcgill.ecse321.boardroom.dtos;

import ca.mcgill.ecse321.boardroom.model.Review;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.BoardGame;

public class ReviewResponseDto {

    private int id;
    private int stars;
    private String comment;
    private Person author;
    private BoardGame boardGame;

    @SuppressWarnings("unused")
    private ReviewResponseDto() {
    }

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.stars = review.getStars();
        this.comment = review.getComment();
        this.author = review.getAuthor();
        this.boardGame = review.getBoardGame();
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

    public Person getAuthor() {
        return author;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }
}
