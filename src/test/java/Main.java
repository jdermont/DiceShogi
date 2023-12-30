import com.codingame.game.Game;
import com.codingame.game.Move;
import com.codingame.gameengine.runner.MultiplayerGameRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.addAgent(Player1.class);
        gameRunner.addAgent(Player2.class);

        gameRunner.setLeagueLevel(1);

        gameRunner.start();
    }
}
