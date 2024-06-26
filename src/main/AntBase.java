package main;

import java.awt.*;
import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntBase {

    private static final double SPAWN_RATE = 10;

    private final Vect pos;
    private final List<Ant> ants;
    private int maxAnts;
    private int foodCount = 0;
    private double nextSpawn;

    public final List<Marker> homeMarkers = new ArrayList<>();
    public final List<Marker> foodMarkers = new ArrayList<>();

    private Random r;

    public AntBase(Vect pos, int numAnts) {
        this.pos = pos;
        this.ants = new ArrayList<>();
        this.nextSpawn = 0.0;

        this.maxAnts = numAnts;

        this.r = new Random();
    }

    public void spawnAnt() {
        if (this.ants.size() < this.maxAnts) {
            Vect mov = new Vect(r.nextDouble(-1, 1), r.nextDouble(-1, 1)).norm(Ant.SPEED);
            this.ants.add(new Ant(this, pos, mov));
        }
    }

    public void addFoodMarker(Vect pos) {
        this.foodMarkers.add(new Marker(pos, 100, true));
    }

    private void updateMarkers(double delta, List<Marker> markers) {
        List<Marker> oldMarkers = new ArrayList<>(markers);
        for (Marker marker : oldMarkers) {
            marker.update(delta);
            if (marker.getStrength() <= 0)
                markers.remove(marker);
        }
    }

    public void update(double delta) {
        this.nextSpawn += .01;

        if (this.nextSpawn > (1 / AntBase.SPAWN_RATE)) {
            this.spawnAnt();
            this.nextSpawn = 0;
        }

        this.updateMarkers(delta, this.homeMarkers);
        this.updateMarkers(delta, this.foodMarkers);

//        System.out.println(this.ants.get(0).getPos());
        for (Ant ant : this.ants) {
            ant.update(delta);
        }
    }

    public void paintMarkers(Graphics2D g2d, int scale, List<Marker> markers, Color baseCol) {
        for (Marker marker : markers) {
            if (marker == null)
                continue;
            Vect pos = marker.getPos();
            double strength = marker.getStrength();
            strength = Math.min(strength, 1.0);

            Color col = new Color(baseCol.getRed(), baseCol.getGreen(), baseCol.getBlue(), (int) (((double) baseCol.getAlpha()) * strength));
            g2d.setPaint(col);
            g2d.fillOval((int) (pos.getX() * scale), (int) (pos.getY() * scale), 2 * scale, 2 * scale);
        }
    }

    public void paint(Graphics2D g2d, int scale) {
        //PAINT MARKERS
        this.paintMarkers(g2d, scale, new ArrayList<>(this.homeMarkers), Surface.COL_MARKER_HOME);
        this.paintMarkers(g2d, scale, new ArrayList<>(this.foodMarkers), Surface.COL_MARKER_FOOD);

        // PAINT VIEW
//        g2d.setPaint(Surface.COL_VIEW);
//        for (Ant ant : new ArrayList<>(this.ants)) {
//            Vect[] view = ant.getView();
//            int[] x = new int[]{(int) (view[0].getX()*scale), (int) view[1].getX()*scale, (int) view[2].getX()*scale};
//            int[] y = new int[]{(int) (view[0].getY()*scale), (int) view[1].getY()*scale, (int) view[2].getY()*scale};
//            g2d.fillPolygon(x, y, x.length);
//        }

        // PAINT FORCE
//        g2d.setPaint(Surface.COL_FORCE);
//        for (Ant ant : this.ants) {
//            Vect force = ant.getPos().plus(ant.getLastForce());
//            g2d.drawLine(
//                    (int) (ant.getPos().getX() * scale), (int) (ant.getPos().getY() * scale),
//                    (int) (force.getX() * scale), (int) (force.getY() * scale)
//            );
//        }

        // PAINT BODY
        for (Ant ant : new ArrayList<>(this.ants)) {
            Vect mov = ant.getMov();
            Vect pos = ant.getPos();

            g2d.setPaint(Surface.COL_FOOD);
            if (ant.isHasFood())
                g2d.fillOval((int) (pos.getX() * scale), (int) (pos.getY() * scale), 4 * scale, 4 * scale);

            g2d.setPaint(Surface.COL_ANT);
            for (int i = -1; i <= 1; i++) {
                Vect cPos = pos.plus(mov.norm(i));
                g2d.fillOval((int) (cPos.getX() * scale), (int) (cPos.getY() * scale), 2 * scale, 2 * scale);
            }
        }
    }

    public Vect getPos() {
        return pos;
    }

    public List<Ant> getAnts() {
        return ants;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void addFood() {
        this.foodCount++;
    }
}
