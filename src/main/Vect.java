package main;

import java.awt.*;

public class Vect {

    private double x;
    private double y;

    public Vect(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vect(Vect v) {
        this.x = v.getX();
        this.y = v.getY();
    }

    public Vect plus(Vect other) {
        return new Vect(this.getX() + other.getX(), this.getY() + other.getY());
    }

    public Vect minus(Vect other) {
        return new Vect(this.getX() - other.getX(), this.getY() - other.getY());
    }

    public Vect scale(double scalar){
        return new Vect(this.getX() * scalar, this.getY() * scalar);
    }

    public double length() {
        return Math.sqrt((this.x*this.x + this.y*this.y));
    }

    public Vect norm() {
        double len = this.length();
        if (len > 0) {
            return this.scale(1/len);
        }
        return new Vect(0, 0);
    }

    public Vect norm(double scalar) {
        double len = this.length();
        if (len > 0) {
            return this.scale(scalar/len);
        }
        return new Vect(0, 0);
    }

    public Point point() {
        return new Point((int) this.getX(), (int) this.getY());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Vect{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
