package ca.mcgill.ecse321.eventregistration.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Location {
    @Id
    @GeneratedValue
    private int id;
    private String address;
    private String city;
    private String province;

    protected Location() {}


    public Location(String address, String city, String province) {
        this.address = address;
        this.city = city;
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }
}