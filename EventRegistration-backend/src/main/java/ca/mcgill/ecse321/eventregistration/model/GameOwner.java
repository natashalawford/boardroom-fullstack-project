package ca.mcgill.ecse321.eventregistration.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class GameOwner {

    @Id
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