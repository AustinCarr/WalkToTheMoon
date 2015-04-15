package com.example.austin.walktothemoon;

/**
 * Created by Julianne on 3/23/2015.
 */
public class Powerups {
    private String name;
    private int in_use;                 // 0 for false, 1 for true
    private String expiration_date;

    public Powerups() {

    }

    public Powerups(String name, int in_use, String expiration_date) {
        this.name = name;
        this.in_use = in_use;
        this.expiration_date = expiration_date;
    }

    public Powerups(String name, int in_use) {
        this.name = name;
        this.in_use = in_use;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setInUse(int in_use) {
        this.in_use = in_use;
    }

    public int getInUse() {
        return this.in_use;
    }

    public void setExpirationDate(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getExpirationDate() {
        return this.expiration_date;
    }
}
