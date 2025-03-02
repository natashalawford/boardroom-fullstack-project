package ca.mcgill.ecse321.boardroom.dtos;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Location;
import ca.mcgill.ecse321.boardroom.model.Person;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Future;

public class EventCreationDto {
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "Start date and time are required.")
    @FutureOrPresent(message = "Start date and time must be in the present or future.")
    private LocalDateTime endDateTime;

    @NotNull(message = "End date and time are required.")
    @Future(message = "End date and time must be in the future.")
    private LocalDateTime startDateTime;

    @Min(value = 1, message = "Maximum participants must be at least 1.")
    private int maxParticipants;

    @NotNull(message = "Location is required.")
    private Location location;

    @NotNull(message = "Host is required.")
    private Person host;
    @NotNull(message = "Board game selection is required.")
    private BoardGame boardGame;

    public EventCreationDto(String title, String description,
                            LocalDateTime startDateTime, LocalDateTime endDateTime,
                            int maxParticipants, Location location, Person host, BoardGame boardGame) {
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.maxParticipants = maxParticipants;
        this.location = location;
        this.host = host;
        this.boardGame = boardGame;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() { return description; }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public Location getLocation() {
        return location;
    }
    public Person getHost() {return host;}

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}
