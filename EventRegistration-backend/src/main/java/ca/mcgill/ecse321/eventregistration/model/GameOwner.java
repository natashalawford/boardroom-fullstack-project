package ca.mcgill.ecse321.eventregistration.model;

import jakarta.persistence.*;

@Entity
public class GameOwner {

    @Id
    private int id;

    //MapsId makes it so that the person's id becomes the GameOwner's id too
    @MapsId
    @OneToOne
    private Person person;

    protected GameOwner() {}

    public GameOwner(Person person){
        this.person = person;
    }

    public Person getPerson(){
        return this.person;
    }

}