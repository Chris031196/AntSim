package main;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;


public class Main extends JFrame {

    public Main() {
        initUI();
    }

    private void initUI() {
        AntBase base = new AntBase(new Vect(50, 50), 500);
        base.addFoodMarker(new Vect(675, 675));
        final Surface surface = new Surface(base);
        add(surface);

        setTitle("Ants");
        setSize(Surface.DIM.x*Surface.SCALE, Surface.DIM.y*Surface.SCALE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int x=675;x<680;x++) {
            for (int y=675;y<680;y++) {
                Food f = new Food(new Vect(x, y));
            }
        }

        Thread t = new Thread(new Runnable() {
            private long lastTime = System.currentTimeMillis();
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long now = System.currentTimeMillis();
                    double delta = ((double) now - lastTime) / 1000.0;
                    lastTime = now;
                    surface.update(delta);
                    surface.repaint();
                }
            }
        });
        t.start();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main ex = new Main();
                ex.setVisible(true);
            }
        });
    }
}
