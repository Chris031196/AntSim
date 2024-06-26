package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant {

    public static final int VIEW_DIST = 50;
    public static final int MARKER_TO_INITIATIVE_RATIO = 1 / 100;
    public static final double SPEED = 50;
    public static final int TURN_SPEED = 30;
    public static final double MIN_MARKER_DROP_TIME = .01;
    public static final double MAX_MARKER_DROP_TIME = .6;
    public static final double INTERACTION_DIST = 2;

    private final AntBase base;
    private Vect pos;
    private Vect mov;

    private Vect lastForce;

    private boolean hasFood;
    private double markerDropTime;

    private double homeMarkerStrength;
    private double foodMarkerStrength;

    private Random r;

    public Ant(AntBase base, Vect pos, Vect mov) {
        this.base = base;
        this.pos = pos;
        this.mov = mov;
        this.hasFood = false;

        base.homeMarkers.add(new Marker(pos, 100, true));

        r = new Random();
        this.markerDropTime = this.r.nextDouble(Ant.MIN_MARKER_DROP_TIME, Ant.MAX_MARKER_DROP_TIME);
        this.homeMarkerStrength = 1;
        this.foodMarkerStrength = 0;
    }

    public void update(double delta) {
        // PICKUP FOOD
        if (!this.hasFood) {
            for (Food food : Food.foods)
                if (food.getPos().point().distance(this.getPos().point()) <= Ant.INTERACTION_DIST)
                    this.foundFood(food);
        } else {
            if (this.base.getPos().point().distance(this.getPos().point()) <= Ant.INTERACTION_DIST)
                this.foundBase();
        }

        Vect smellForce = this.getAvgForce();
        this.mov = this.getMov().plus(smellForce).norm(Ant.SPEED*delta);

        this.pos = this.pos.plus(this.getMov());

        if (this.pos.getX() < 0) {
            this.pos.setX(0);
            this.mov.setX(-this.mov.getX());
        }
        if (this.pos.getY() < 0) {
            this.pos.setY(0);
            this.mov.setY(-this.mov.getY());
        }
        if (this.pos.getX() > Surface.DIM.getX()) {
            this.pos.setX(Surface.DIM.getX());
            this.mov.setX(-this.mov.getX());
        }
        if (this.pos.getY() > Surface.DIM.getY()) {
            this.pos.setY(Surface.DIM.getY());
            this.mov.setY(-this.mov.getY());
        }

        // MARKER DROPPING
        this.homeMarkerStrength -= delta*.01;
        this.foodMarkerStrength -= delta*.01;
        if (this.homeMarkerStrength < 0)
            this.homeMarkerStrength = 0;
        if (this.foodMarkerStrength < 0)
            this.foodMarkerStrength = 0;
        java.util.List<Marker> dropMarkers = this.hasFood ? this.base.foodMarkers : this.base.homeMarkers;
        this.markerDropTime -= delta;
        if (this.markerDropTime <= 0) {
            dropMarkers.add(new Marker(this.getPos(), this.homeMarkerStrength, false));
            this.markerDropTime = this.r.nextDouble(Ant.MIN_MARKER_DROP_TIME, Ant.MAX_MARKER_DROP_TIME);
        }
    }

    private Vect getAvgForce() {
        java.util.List<Marker> markers = this.hasFood ? this.base.homeMarkers : this.base.foodMarkers;
        Vect allForces = new Vect(0, 0);
        int numForces = 0;

        Vect pos = this.getPos();
        for (Marker marker : markers) {
            Vect markerPos = marker.getPos();
            if (!this.isVisible(markerPos))
                continue;

            allForces = allForces.plus(marker.getForce(pos));
            numForces++;
        }
        this.lastForce = allForces.length() > 0 ? allForces.scale(1.0 / (double) numForces) : new Vect(0, 0);

        double b = Math.max(this.lastForce.length()*.2, .01);
        double x = r.nextDouble(-b, b);
        double y = r.nextDouble(-b, b);
        this.lastForce = this.lastForce.plus(new Vect(x, y));
        return this.lastForce;
    }

    public Vect getLastForce() {
        return this.lastForce != null ? this.lastForce : new Vect(0, 0);
    }

    public Vect[] getView() {
        Vect root = this.getPos();
        Vect viewLine = this.getMov().norm(Ant.VIEW_DIST);

        Vect rightAngle = viewLine.plus(new Vect(viewLine.getY()*1.5, -viewLine.getX()*1.5));
        Vect leftAngle = viewLine.plus(new Vect(-viewLine.getY()*1.5, viewLine.getX()*1.5));

        return new Vect[]{root, root.plus(rightAngle), root.plus(leftAngle)};
    }

    public boolean isVisible(Vect v) {
        // check distance first
        double distance = this.getPos().point().distance(v.point());
        if (distance > Ant.VIEW_DIST)
            return false;

        // now check view
        Vect[] view = this.getView();
        double d1 = sign(v, view[0], view[1]);
        double d2 = sign(v, view[1], view[2]);
        double d3 = sign(v, view[2], view[0]);

        boolean has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);
    }

    private static double sign(Vect v1, Vect v2, Vect v3) {
        return (v1.getX() - v3.getX()) * (v2.getY() - v3.getY()) - (v2.getX() - v3.getX()) * (v1.getY() - v3.getY());
    }

    public void foundFood(Food food) {
        food.bite();
        this.mov = this.mov.scale(-1);
        this.hasFood = true;
        this.foodMarkerStrength = 1;
    }

    public void foundBase() {
        this.mov = this.mov.scale(-1);
        this.hasFood = false;
        this.base.addFood();
        this.homeMarkerStrength = 1;
    }

    public Vect getPos() {
        return this.pos;
    }

    public Vect getMov() {
        return this.mov;
    }

    public boolean isHasFood() {
        return this.hasFood;
    }

}
