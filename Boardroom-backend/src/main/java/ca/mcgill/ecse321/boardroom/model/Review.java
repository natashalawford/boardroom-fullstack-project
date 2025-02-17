package ca.mcgill.ecse321.boardroom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalTime;

@Entity
public class Review {
    @Id
    @GeneratedValue
    private int id;
    private int stars;
    private String comment;
    private LocalTime timeStamp;

    @ManyToOne
    private Person author;
    @ManyToOne
    private BoardGame boardGame;

    protected Review() {}

    public Review(int stars, String comment, LocalTime timeStamp,
                  Person author, BoardGame boardGame) {

        this.stars = stars;
        this.comment = comment;
        this.timeStamp = timeStamp;
        this.author = author;
        this.boardGame = boardGame;
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

    public LocalTime getTimeStamp() {
        return timeStamp;
    }

    public Person getAuthor() {
        return author;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }

}
