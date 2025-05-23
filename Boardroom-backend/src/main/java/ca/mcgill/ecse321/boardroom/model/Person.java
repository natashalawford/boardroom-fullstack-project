package ca.mcgill.ecse321.boardroom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Person {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean owner;

    protected Person() {}

    public Person(String name, String email, String password, boolean owner) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.owner = owner;
    }

    public Person(int id, String name, String email, String password, boolean owner) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setPassword(String passwordChange) {
        this.password = passwordChange;
    }

}
