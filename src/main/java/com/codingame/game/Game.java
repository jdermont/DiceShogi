package com.codingame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public static final int FIRST_PLAYER = 0;
    public static final int SECOND_PLAYER = 1;
    public static final int NONE = -1;

    public final GameState gameState;
    private final Random random;
    private final long originalSeed;
    private int lastDiceRoll;
    private Move lastMove;
    private boolean allMovesLegal;

    public Game(Random random, long originalSeed) {
        this.random = random;
        this.originalSeed = originalSeed;
        gameState = new GameState();
        gameState.init();
    }

    public void makeMove(Move move) {
        gameState.makeMove(move);
        lastMove = move;
    }

    public boolean isGameOver() {
        if (gameState.rounds >= 300) {
            return true;
        }
        return gameState.generateMoves().isEmpty();
    }

    public int getWinner() {
        List<Move> moves = gameState.generateMoves();
        if (moves.isEmpty()) { // either checkmated or stalemated player loses
            return gameState.currentPlayer^1;
        }
        return NONE;
    }

    public int getLastDiceRoll() {
        return lastDiceRoll;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public boolean allMovesWereLegal() {
        return allMovesLegal;
    }

    private void rollDice() {
        if (originalSeed == 0) {
            lastDiceRoll = 6;
        } else {
            lastDiceRoll = random.nextInt(6)+1;
        }
    }

    public List<Move> getAvailableMoves() {
        rollDice();
        List<Move> moves = gameState.generateMoves();
        if (lastDiceRoll == 6) {
            allMovesLegal = true;
            return moves;
        }
        List<Move> availableMoves = new ArrayList<>();
        for (Move move : moves) {
            int index = lastDiceRoll-1;
            index = 4 - index;
            if (move.colTo == index) {
                availableMoves.add(move);
            }
        }
        if (availableMoves.isEmpty()) {
            allMovesLegal = true;
            return moves;
        }
        allMovesLegal = false;
        return availableMoves;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(gameState);
        sb.append('\n').append(lastDiceRoll);
        return sb.toString();
    }
}
