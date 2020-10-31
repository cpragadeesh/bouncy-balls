import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;

class Ball extends JPanel implements Runnable, MouseListener {

    public int x = 30;
    public int y = 30;
    public int radius = 15;

    public int dx = 2;
    public int dy = 2;

    public Color color;

    public int ID = 10;

    private boolean shouldMove = true;

    public Ball(Color color, int x, int y, int dx, int dy, int ID) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.ID = ID;

        addMouseListener(this);

        new Thread(this).start();
    }

    public void mousePressed(MouseEvent e) {
        shouldMove = false;
    }

    public void mouseReleased(MouseEvent e) {
        shouldMove = true;
    }

    public void mouseEntered(MouseEvent e) {
        // do nothing;
    }

    public void mouseExited(MouseEvent e) {
        // do nothing;
    }

    public void mouseClicked(MouseEvent e) {
        // do nothing;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval(0, 0, 2 * radius, 2 * radius);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(2 * radius, 2 * radius);
    }

    public void run() {

        while (isVisible()) {

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            move();
            repaint();
        }
    }


    public void move() {

        if (!shouldMove) {
            return;
        }

        if (x < radius)
            dx = Math.abs(dx);
        if (x > getParent().getWidth() - radius)
            dx = - Math.abs(dx);
        if (y < radius)
            dy = Math.abs(dy);
        if (y > getParent().getHeight() - radius)
            dy = - Math.abs(dy);

        x += dx;
        y += dy;

         setSize(getPreferredSize());
         setLocation(x, y);
    }
}

class CollisionDetection implements Runnable {

    ArrayList<Ball> balls;

    CollisionDetection(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    public void run() {
        while (true) {
            for (Ball ball1 : balls) {
                for (Ball ball2 : balls) {
                    if (ball1.ID == ball2.ID) {
                        continue;
                    }

                    if (isColliding(ball1, ball2)) {
                        handleCollision(ball1, ball2);
                    }
                }
            }
        }
    }

    private boolean isColliding(Ball ball1, Ball ball2) {
        double distBetweenCenters = Math.sqrt( (ball1.x - ball2.x) * (ball1.x - ball2.x)
                                                + (ball1.y - ball2.y) * (ball1.y - ball2.y) );

        return distBetweenCenters < 2 * ball1.radius;
    }

    private void handleCollision(Ball ball1, Ball ball2) {
        ball1.dx = -ball1.dx;
        ball2.dx = -ball2.dx;

        ball1.dy = -ball1.dy;
        ball2.dy = -ball2.dy;
    }
}

class BouncyBallsPanel extends JPanel {

    ArrayList<Ball> balls;

    public BouncyBallsPanel() {
        setLayout(null);
        balls = new ArrayList<Ball>();
        balls.add(new Ball(Color.red, 50 - (int) Math.round((Math.random() * 25)),
                50 - (int) Math.round((Math.random() * 25)),
                2 - (int) Math.round((Math.random() * 1)),
                2 - (int) Math.round((Math.random() * 1)),
                1));

        balls.add(new Ball(Color.blue, 50 - (int) Math.round((Math.random() * 25)),
                50 - (int) Math.round((Math.random() * 25)),
                2 - (int) Math.round((Math.random() * 1)),
                2 - (int) Math.round((Math.random() * 1)),
                2));

        balls.add(new Ball(Color.black, 50 - (int) Math.round((Math.random() * 25)),
                50 - (int) Math.round((Math.random() * 25)),
                2 - (int) Math.round((Math.random() * 1)),
                2 - (int) Math.round((Math.random() * 1)),
                3));

        balls.add(new Ball(Color.pink, 50 - (int) Math.round((Math.random() * 25)),
                50 - (int) Math.round((Math.random() * 25)),
                2 - (int) Math.round((Math.random() * 1)),
                2 - (int) Math.round((Math.random() * 1)),
                4));


        for (Ball ball : balls) {
            add(ball);
        }

        new Thread(new CollisionDetection(balls)).start();
    }
}

public class BouncyBalls {

    private void createWindow() {
        JFrame bouncyBallsFrame = new JFrame("BouncyBalls");
        bouncyBallsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bouncyBallsFrame.setLayout(new BorderLayout());
        bouncyBallsFrame.add(new BouncyBallsPanel());
        bouncyBallsFrame.setSize(400,400);

        bouncyBallsFrame.setVisible(true);
    }

    private void init() {
        createWindow();
    }

    public static void main(String[] args) {

        BouncyBalls bouncyBalls = new BouncyBalls();
        bouncyBalls.init();

    }
}
