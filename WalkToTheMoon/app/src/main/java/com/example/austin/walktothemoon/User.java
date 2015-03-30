package com.example.austin.walktothemoon;

/**
 * Created by Julianne on 3/23/2015.
 */
public class User {
    private String license_id;
    private String name;
    private String address_state;
    private int real_steps;
    private int boosted_steps;

    public User() {

    }

    public User(String license_id, String name, String address_state,
                int real_steps, int boosted_steps) {
        this.license_id = license_id;
        this.name = name;
        this.address_state = address_state;
        this.real_steps = real_steps;
        this.boosted_steps = boosted_steps;
    }

    public void setLicenseId(String license_id) {
        this.license_id = license_id;
    }

    public String getLicenseId() {
        return this.license_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAddressState(String address_state) {
        this.address_state = address_state;
    }

    public String getAddressState() {
        return this.address_state;
    }

    public void setRealSteps(int real_steps) {
        this.real_steps = real_steps;
    }

    public int getRealSteps() {
        return this.real_steps;
    }

    public void setBoostedSteps(int boosted_steps) {
        this.boosted_steps = boosted_steps;
    }

    public int getBoostedSteps() {
        return this.boosted_steps;
    }
}
