package SOAPTask;

import gui.Bug;

import javax.jws.WebService;
import java.awt.*;
import java.util.ArrayList;

@WebService(endpointInterface = "SOAPTask.GameWebService")
public class GameWebServiceImpl implements GameWebService {

    GameLogic gameLogic = new GameLogic();

    @Override
    public String getNextBugPositions(String bugsMessage, String targetsMessage, int screenWidth, int screenHeight) {
        ArrayList<Bug> bugs = getBugsFromMessage(bugsMessage);
        ArrayList<Point> targets = getTargetsFromMessage(targetsMessage);

        gameLogic.bugs = bugs;
        gameLogic.targets.targets = targets;
        gameLogic.currentWidth = screenWidth;
        gameLogic.currentHeight = screenHeight;
        gameLogic.onModelUpdateEvent();

        StringBuilder answer = new StringBuilder();
        for (Bug bug : gameLogic.bugs) {
            answer.append(bug.getStringState());
            answer.append("/");
        }
        answer.append("#");
        answer.append(gameLogic.targets.getStringState());

        return answer.toString();
    }

    private ArrayList<Bug> getBugsFromMessage(String message){
        String[] bugsPositions = message.split("/");
        ArrayList<Bug> bugs = new ArrayList<>();
        for (String bug : bugsPositions){
            if (bug.equals(""))
                continue;
            String[] state = bug.split(";");
            bugs.add(new Bug(Double.parseDouble(state[0]), Double.parseDouble(state[1]), Double.parseDouble(state[2]), Color.RED, ""));
        }
        return bugs;
    }

    private ArrayList<Point> getTargetsFromMessage(String message){
        String[] targetsPositions = message.split("/");
        ArrayList<Point> targets = new ArrayList<>();
        for (String target : targetsPositions){
            if (target.equals(""))
                continue;
            String[] state = target.split(";");
            targets.add(new Point(Integer.parseInt(state[0]), Integer.parseInt(state[1])));
        }
        return targets;
    }


}
