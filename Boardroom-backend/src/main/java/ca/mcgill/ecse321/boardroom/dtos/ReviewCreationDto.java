package ca.mcgill.ecse321.boardroom.dtos;

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

    @NotNull(message = "Author is required")
    private Integer authorId;

    @NotNull(message = "Board game is required")
    private String boardGameName;

    public ReviewCreationDto(int stars, String comment, int authorId, String boardGameName) {
        this.stars = stars;
        this.comment = comment;
        this.authorId = authorId;
        this.boardGameName = boardGameName;
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
