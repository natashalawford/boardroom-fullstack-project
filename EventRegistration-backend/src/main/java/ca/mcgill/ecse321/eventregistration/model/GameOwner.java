package ca.mcgill.ecse321.eventregistration.model;



public class GameOwner {
    
    @Id
    @OneToOne
    private Person person;

    protected GameOwner() {}

    public GameOwner(Person person){
        this.person = person;
    }

    public person getPerson(){
        return this.person;
    }

}