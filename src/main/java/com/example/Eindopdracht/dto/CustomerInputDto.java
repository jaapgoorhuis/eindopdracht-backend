package com.example.Eindopdracht.dto;

import com.example.Eindopdracht.model.Car;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

public class CustomerInputDto {

    Long id;

    @Email
    @Column(unique = true)
    public String email;

    public String firstname;

    public String lastname;

    public String streetname;

    public String town;

    public String zipcode;

    public Collection<Car> cars;

    public CustomerInputDto(Long id,@NotBlank String email, String firstname, String lastname, String streetname, String town, String zipcode) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.streetname = streetname;
        this.town = town;
        this.zipcode = zipcode;
    }

    public CustomerInputDto() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Collection<Car> getCars() {
        return cars;
    }

    public void setCar(Collection<Car> cars) {
        this.cars = cars;
    }
}
