package SOAPTask;

import gui.Bug;
import gui.Targets;

import java.awt.Point;
import java.util.*;

public class GameLogic {
    public ArrayList<Bug> bugs = new ArrayList<>();
    public Targets targets = new Targets();

    public volatile double currentWidth = 0;
    public volatile double currentHeight = 0;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public boolean isRobotAbroad(double x, double y, double direction) {

        double distToLeftBoarder = x - Math.abs(Math.cos(direction)) * 15;
        double distToUpBoarder = y - Math.abs(Math.sin(direction)) * 15;
        double distToRightBoarder = x + Math.abs(Math.cos(direction)) * 15;
        double distToDownBoarder = y + Math.abs(Math.sin(direction)) * 15;

        if (currentWidth != 0 && currentHeight != 0) {

            if ((distToLeftBoarder <= 0 || distToLeftBoarder >= currentWidth) ||
                    (distToRightBoarder <= 0 || distToRightBoarder >= currentWidth) ||
                    (distToDownBoarder <= 0 || distToDownBoarder >= currentHeight) ||
                    (distToUpBoarder <= 0 || distToUpBoarder >= currentHeight)) {
                return true;
            }
        }
        return false;
    }

    private void checkBugsToDie() {
        ArrayList<Bug> bugsToDelete = new ArrayList<>();
        Collections.shuffle(bugs);
        for (Bug bug : bugs) {
            double bugX = bug.getM_bugPositionX();
            double bugY = bug.getM_bugPositionY();
            double dir = bug.getM_bugDirection();

            if (bugsToDelete.contains(bug)) {
                continue;
            }

            if (isRobotAbroad(bugX, bugY, dir)) {
                bugsToDelete.add(bug);
                continue;
            }

            for (Bug another_bug : bugs) {
                if (bug.equals(another_bug))
                    continue;
                double another_bugX = another_bug.getM_bugPositionX();
                double another_bugY = another_bug.getM_bugPositionY();
                double distance = distance(bugX, bugY, another_bugX, another_bugY);
                if (distance < 15) {
                    bugsToDelete.add(another_bug);
                }
            }
        }

        for (Bug bug : bugsToDelete) {
            bugs.remove(bug);
            bug.bugDied();
        }
    }

    public void onModelUpdateEvent() {
        if (targets.getTargetsList().size() == 0) {
            return;
        }

        checkBugsToDie();

        for (Bug bug : bugs) {
            double m_bugPositionX = bug.getM_bugPositionX();
            double m_bugPositionY = bug.getM_bugPositionY();
            double m_bugDirection = bug.getM_bugDirection();

            double distance = Double.POSITIVE_INFINITY;
            Point target = new Point(0, 0);
            for (Point m_target : targets.getTargetsList()) {
                double new_dist = distance(m_target.x, m_target.y, m_bugPositionX, m_bugPositionY);
                if (new_dist < distance) {
                    distance = new_dist;
                    target = m_target;
                }
            }

            double m_targetPositionX = target.x;
            double m_targetPositionY = target.y;

            double velocity = maxVelocity;
            double angleToTarget = angleTo(m_bugPositionX, m_bugPositionY, m_targetPositionX, m_targetPositionY);
            double angularVelocity = 0;
            double angleBetweenTargetRobot = asNormalizedRadians(angleToTarget - m_bugDirection);
            if (angleBetweenTargetRobot < Math.PI) {
                angularVelocity = maxAngularVelocity;
            } else {
                angularVelocity = -maxAngularVelocity;
            }

            if (distance < 0.7) {
                targets.removeTarget(target);
            }

            if (Math.abs(angleToTarget - m_bugDirection) < 0.05) {
                moveRobot(bug, velocity, angularVelocity, 10);
            } else {
                if (distance < 15) {
                    moveRobot(bug, 0, angularVelocity, 10);
                } else {
                    moveRobot(bug, velocity / 2, angularVelocity, 10);
                }
            }
        }
    }

    private void moveRobot(Bug bug, double velocity, double angularVelocity, double duration) {
        double m_bugPositionX = bug.getM_bugPositionX();
        double m_bugPositionY = bug.getM_bugPositionY();
        double m_bugDirection = bug.getM_bugDirection();

        double newX = m_bugPositionX + velocity * duration * Math.cos(m_bugDirection);
        double newY = m_bugPositionY + velocity * duration * Math.sin(m_bugDirection);

        bug.setM_bugPositionX(newX);
        bug.setM_bugPositionY(newY);
        double newDirection = m_bugDirection + angularVelocity * duration * 4;
        bug.setM_bugDirection(asNormalizedRadians(newDirection));
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

}