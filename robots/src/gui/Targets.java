package gui;

import java.awt.*;
import java.util.ArrayList;

public class Targets {

    public volatile ArrayList<Point> targets = new ArrayList<>();

    public void addTarget(Point target){
        targets.add(target);
    }

    public void removeTarget(Point target){
        targets.remove(target);
    }

    public ArrayList<Point> getTargetsList(){
        return targets;
    }

    public String getStringState(){
        StringBuilder state = new StringBuilder();
        for (Point target : targets) {
            state.append(target.x);
            state.append(";");
            state.append(target.y);
            state.append("/");
        }
        return state.toString();
    }
}
