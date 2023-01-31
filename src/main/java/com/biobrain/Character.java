package com.biobrain;

public class Character {

    public static final double MIN_HEALTH = 0;
    public static final double MAX_HEALTH = 100;

    private double health = 100.0;

    private boolean isDead = false;

    public double getHealth() {
        return health;
    }

    public void setHealth(double health){
        if(health < MIN_HEALTH){
            setDead(true);
        } else {
            this.health = health;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        this.isDead = dead;
    }
}
