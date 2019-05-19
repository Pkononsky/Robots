package SOAPTask;

import gui.Bug;
import gui.Targets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Visualizer extends JPanel {

    public java.util.Timer m_timer = initTimer();

    private static java.util.Timer initTimer() {
        java.util.Timer timer = new Timer("events generator", true);
        return timer;
    }

    GameWebService gameWebService;

    public ArrayList<Bug> bugs = new ArrayList<>();
    public Targets targets = new Targets();

    public Visualizer(GameWebService gameWebService) {
        this.gameWebService = gameWebService;

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 25);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateGame();
            }
        }, 0, 50);

        setStartPositions();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
            }
        });

        setDoubleBuffered(true);

    }

    public void updateGame() {
        StringBuilder bugMessage = new StringBuilder();
        for (Bug bug : bugs) {
            bugMessage.append(bug.getStringState());
            bugMessage.append('/');
        }
        String targetMessage = targets.getStringState();

        String answer = gameWebService.getNextBugPositions(bugMessage.toString(), targetMessage, getWidth(), getHeight());

        String[] splitedAnswer = answer.split("#");
        System.out.println(answer);
        if (answer.length() == 1) {
            System.out.println(1);
            return;
        } else if (splitedAnswer.length == 2) {
            setBugs(splitedAnswer[0]);
            setTargets(splitedAnswer[1]);
        } else if (answer.charAt(0) == '#') {
            setTargets(splitedAnswer[0]);
            bugs = new ArrayList<>();
        } else {
            setBugs(splitedAnswer[0]);
            targets.targets = new ArrayList<>();
        }
    }


    public void setBugs(String positions) {
        String[] bugsPositions = positions.split("/");
        bugs = new ArrayList<>();
        for (String bug : bugsPositions) {
            if (bug.equals(""))
                continue;
            String[] state = bug.split(";");
            bugs.add(new Bug(Double.parseDouble(state[0]), Double.parseDouble(state[1]), Double.parseDouble(state[2]), Color.RED, "RED"));
        }
    }

    public void setTargets(String positions) {
        String[] targetsPosition = positions.split("/");
        targets.targets = new ArrayList<>();
        for (String target : targetsPosition) {
            if (target.equals(""))
                continue;
            String[] state = target.split(";");
            targets.addTarget(new Point(Integer.parseInt(state[0]), Integer.parseInt(state[1])));
        }
    }


    public void setStartPositions() {
        bugs = new ArrayList<>();
        bugs.add(new Bug(50, 50, 0, Color.RED, "RED"));
        bugs.add(new Bug(50, 300, 0, Color.RED, "RED"));
//        bugs.add(new Bug(300, 50, 0, Color.ORANGE, "ORANGE"));
//        bugs.add(new Bug(300, 300, 0, Color.PINK, "PINK"));
        targets.addTarget(new Point(150, 50));
    }

    public void setTargetPosition(Point p) {
        targets.addTarget(new Point(p.x, p.y));
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Bug bug : bugs)
            drawRobot(g2d, bug);
        for (Point target : targets.getTargetsList())
            drawTarget(g2d, target.x, target.y);

    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, Bug bug) {
        double m_bugPositionX = bug.getM_bugPositionX();
        double m_bugPositionY = bug.getM_bugPositionY();
        double m_bugDirection = bug.getM_bugDirection();

        Color color = bug.getColor();
        int robotCenterX = (int) (m_bugPositionX);
        int robotCenterY = (int) (m_bugPositionY);

        AffineTransform t = AffineTransform.getRotateInstance(m_bugDirection, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(color);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}



