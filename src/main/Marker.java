package main;

import java.util.ArrayList;
import java.util.List;


public class Marker {
    public static final int INIT_LIFETIME = 20;

    private final Vect pos;
    private double lifetime;
    private double strength_factor;
    private boolean stable;

    public Marker(Vect pos, double strength_factor, boolean stable) {
        this.pos = pos;
        this.lifetime = Marker.INIT_LIFETIME;
        this.strength_factor = strength_factor;
        this.stable = stable;
    }

    public Marker(Vect pos) {
        this(pos, 1, false);
    }

    public void update(double delta) {
        if (!this.stable)
            this.lifetime -= delta;
    }

    public Vect getPos() {
        return this.pos;
    }

    public double getStrength() {
        if (this.lifetime > 0)
            return (this.lifetime / Marker.INIT_LIFETIME) * this.strength_factor;
        return 0;
    }

    public Vect getForce(Vect pos) {
        Vect toMarker = this.getPos().minus(pos);
        double dist = toMarker.length();
        return toMarker.norm(10*this.getStrength());
    }
}
