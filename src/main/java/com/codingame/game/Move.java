package com.codingame.game;

public class Move {
    public int player;
    public int rowFrom, colFrom;
    public int rowTo, colTo;
    public Piece piece;
    public boolean promote;

    public Move(int player, int rowTo, int colTo, Piece piece) {
        this(player, -1, -1, rowTo, colTo, piece, false);
    }

    public Move(int player, int rowFrom, int colFrom, int rowTo, int colTo, Piece piece) {
        this(player, rowFrom, colFrom, rowTo, colTo, piece, false);
    }

    public Move(int player, int rowFrom, int colFrom, int rowTo, int colTo, Piece piece, boolean promote) {
        this.player = player;
        this.rowFrom = rowFrom;
        this.colFrom = colFrom;
        this.rowTo = rowTo;
        this.colTo = colTo;
        this.piece = piece;
        this.promote = promote;
    }

    @Override
    public String toString() {
        if (colFrom != -1) {
            return (5-colFrom)+""+(5-rowFrom)+""+(5-colTo)+""+(5-rowTo)+(promote?"+":"");
        } else {
            return piece.getChr(player)+"*"+(5-colTo)+""+(5-rowTo);
        }
    }
}
