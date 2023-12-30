package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.toggle.ToggleModule;
import com.codingame.view.DiceViewer;
import com.codingame.view.BoardViewer;
import com.codingame.view.PlayerViewer;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject private EndScreenModule endScreenModule;
    @Inject private ToggleModule toggleModule;

    private Game game;
    private BoardViewer boardViewer;
    private PlayerViewer playerViewer;
    private DiceViewer diceViewer;
    
    @Override
    public void init() {
        long originalSeed = gameManager.getSeed();
        game = new Game(gameManager.getRandom(),originalSeed);
        gameManager.setFrameDuration(1500);
        gameManager.setFirstTurnMaxTime(1000);
        gameManager.setTurnMaxTime(100);
        gameManager.setMaxTurns(300);

        drawBackground();
        initViewers();
    }

    private void drawBackground() {
        graphicEntityModule.createRectangle().setWidth(1920).setHeight(1080).setFillColor(0x404040);
    }

    private void initViewers() {
        boardViewer = new BoardViewer(game, graphicEntityModule, toggleModule);
        playerViewer = new PlayerViewer(game, graphicEntityModule, gameManager.getPlayer(0), gameManager.getPlayer(1));
        diceViewer = new DiceViewer(game, graphicEntityModule);
        boardViewer.init();
        playerViewer.init();
        diceViewer.init();
    }

    private void sendInputs(int turn, Player player, Player opponent, List<Move> availableMoves) {
        if (turn <= 2) {
            player.sendInputLine(String.valueOf(player.getIndex()));
            player.sendInputLine(String.valueOf(GameState.SIZE));
        }

        String boardString = game.gameState.getBoardString();
        for (String row : boardString.trim().split("\n")) {
            player.sendInputLine(row);
        }
        StringBuilder sb = new StringBuilder();
        if (game.gameState.hands.get(Game.FIRST_PLAYER).isEmpty()) {
            sb.append('-');
        } else {
            for (Piece piece : game.gameState.hands.get(Game.FIRST_PLAYER)) {
                sb.append(piece.getChr(Game.FIRST_PLAYER));
            }
        }
        player.sendInputLine(sb.toString());
        sb = new StringBuilder();
        if (game.gameState.hands.get(Game.SECOND_PLAYER).isEmpty()) {
            sb.append('-');
        } else {
            for (Piece piece : game.gameState.hands.get(Game.SECOND_PLAYER)) {
                sb.append(piece.getChr(Game.SECOND_PLAYER));
            }
        }
        player.sendInputLine(sb.toString());
        player.sendInputLine(String.valueOf(game.getLastDiceRoll()));
        if (opponent.lastMove != null) {
            player.sendInputLine(opponent.lastMove.toString());
        } else {
            player.sendInputLine("none");
        }
        player.sendInputLine(String.valueOf(availableMoves.size()));
        for (Move move : availableMoves) {
            player.sendInputLine(move.toString());
        }
    }

    private void setWinner(Player player) {
        gameManager.addToGameSummary(GameManager.formatSuccessMessage(player.getNicknameToken() + " won!"));
        player.setScore(10);
        endGame();
    }

    @Override
    public void gameTurn(int turn) {
        int t = turn-1;
        Player player = gameManager.getPlayer(t % gameManager.getPlayerCount());
        Player opponent = gameManager.getPlayer((t+1) % gameManager.getPlayerCount());

        List<Move> availableMoves = game.getAvailableMoves();
        diceViewer.updateFrame();
        boardViewer.updateColumns();
        try {
            Collections.shuffle(availableMoves);
            sendInputs(turn, player, opponent, availableMoves);
            player.execute();
            String output = player.getOutputs().get(0);
            parsePlayerOutput(player, output, availableMoves);
            game.makeMove(player.lastMove);
            playerViewer.updateFrame();
            boardViewer.updateFrame();
            if (game.isGameOver()) {
                setScores(game.getWinner());
                endGame();
            }
        } catch (TimeoutException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " timeout!"));
            player.deactivate(player.getNicknameToken() + " timeout!");
            setScores(opponent.getIndex());
            endGame();
        } catch (InvalidMove e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + ": "+e.getMessage()));
            player.deactivate(player.getNicknameToken() + ": "+e.getMessage());
            setScores(opponent.getIndex());
            endGame();
        }
    }

    private void parsePlayerOutput(Player player, String output, List<Move> availableMoves) throws InvalidMove {
        output = output.trim();
        Move move = null;
        String message = "";
        if (output.contains(" ")) {
            String[] parts = output.split(" ");
            if ("random".equalsIgnoreCase(parts[0])) {
                Collections.shuffle(availableMoves);
                move = availableMoves.get(0);
            } else {
                boolean found = false;
                for (Move m : availableMoves) {
                    if (m.toString().equalsIgnoreCase(parts[0])) {
                        move = m;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new InvalidMove(parts[0] + " is not valid move");
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int i=1; i < parts.length; i++) {
                sb.append(parts[i]).append(' ');
            }
            message = sb.toString().trim();
        } else {
            if ("random".equalsIgnoreCase(output)) {
                Collections.shuffle(availableMoves);
                move = availableMoves.get(0);
            } else {
                boolean found = false;
                for (Move m : availableMoves) {
                    if (m.toString().equalsIgnoreCase(output)) {
                        move = m;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new InvalidMove(output + " is not valid move");
                }
            }
        }
        player.lastMove = move;
        player.lastMessage = message;
    }

    private void setScores(int winner) {
        Player p0 = gameManager.getPlayers().get(0);
        Player p1 = gameManager.getPlayers().get(1);
        if (winner == p0.getIndex()) {
            p0.setScore(1);
            p1.setScore(-1);
        } else if (winner == p1.getIndex()) {
            p0.setScore(-1);
            p1.setScore(1);
        } else {
            p0.setScore(0);
            p1.setScore(0);
        }
    }

    private void endGame() {
        gameManager.endGame();
        Player p0 = gameManager.getPlayers().get(0);
        Player p1 = gameManager.getPlayers().get(1);
        if (p0.getScore() > p1.getScore()) {
            playerViewer.unfadePlayer(p0.getIndex());
            playerViewer.fadePlayer(p1.getIndex());
        } else if (p1.getScore() > p0.getScore()) {
            playerViewer.unfadePlayer(p1.getIndex());
            playerViewer.fadePlayer(p0.getIndex());
        } else {
            playerViewer.unfadePlayer(p0.getIndex());
            playerViewer.unfadePlayer(p1.getIndex());
        }
    }

    @Override
    public void onEnd() {
        int[] scores = { gameManager.getPlayer(0).getScore(), gameManager.getPlayer(1).getScore() };
        String[] text = new String[2];
        if(scores[0] > scores[1]) {
            text[0] = "Won";
            text[1] = "Lost";
            gameManager.addTooltip(gameManager.getPlayer(0), gameManager.getPlayer(0).getNicknameToken() + " won");
        } else if(scores[1] > scores[0]) {
            text[0] = "Lost";
            text[1] = "Won";
            gameManager.addTooltip(gameManager.getPlayer(1), gameManager.getPlayer(1).getNicknameToken() + " won");
        } else {
            text[0] = "Draw";
            text[1] = "Draw";
        }
        endScreenModule.setScores(scores, text);
    }
}
