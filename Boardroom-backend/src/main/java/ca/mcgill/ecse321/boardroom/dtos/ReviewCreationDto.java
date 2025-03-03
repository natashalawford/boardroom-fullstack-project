package ca.mcgill.ecse321.boardroom.dtos;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewCreationDto {

    @Min(value = 1, message = "Rating must be at least 1 star.")
    @Max(value = 5, message = "Rating must be at most 5 stars.")
    private int stars;

    @NotBlank(message = "Comment is required.")
    private String comment;

    @NotNull(message = "Author is required.")
    private Person author;

    @NotNull(message = "Board game is required.")
    private BoardGame boardGame;

    public ReviewCreationDto(int stars, String comment, Person author, BoardGame boardGame) {
        this.stars = stars;
        this.comment = comment;
        this.author = author;
        this.boardGame = boardGame;
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
