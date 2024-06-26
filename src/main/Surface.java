package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.Random;

public class Surface extends JPanel {

    public static final Point DIM = new Point(1000, 1000);
    public static final int SCALE = 1;

    // COLORS
    public static final Color COL_ANT = new Color(1f, 0f, 0f, 1f);
    public static final Color COL_VIEW = new Color(1f, 0f, 0f, .1f);
    public static final Color COL_FORCE = new Color(.5f, .5f, 0f, 1f);
    public static final Color COL_FOOD = new Color(0f, 1f, 0f, 1f);
    public static final Color COL_MARKER_HOME = new Color(0f, 0f, 1f, .3f);
    public static final Color COL_MARKER_FOOD = new Color(0f, 1f, 0f, .3f);


    private double test = 100;

    private final AntBase base;

    public Surface(AntBase base) {
        this.base = base;
    }

    public void update(double delta) {
        base.update(delta);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.clearRect(0, 0, Surface.DIM.x*Surface.SCALE, Surface.DIM.y*Surface.SCALE);

        g2d.setPaint(Surface.COL_FOOD);
        for (Food f: Food.foods) {
            Vect pos = f.getPos();
            g2d.fillRect((int) (pos.getX()*Surface.SCALE), (int) (pos.getY()*Surface.SCALE), Surface.SCALE, Surface.SCALE);
        }
        this.base.paint(g2d, Surface.SCALE);

        g2d.setPaint(Color.BLACK);
        g2d.drawString("" +this.base.getFoodCount(), 20, 20);
    }

}
