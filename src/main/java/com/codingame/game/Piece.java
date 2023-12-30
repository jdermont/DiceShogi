package com.codingame.game;

public enum Piece {
    KING("K","k"),
    ROOK("R","r"),
    PROMOTED_ROOK("+R","+r"),
    BISHOP("B","b"),
    PROMOTED_BISHOP("+B","+b"),
    GOLD("G","g"),
    SILVER("S","s"),
    PROMOTED_SILVER("+S","+s"),
    PAWN("P","p"),
    PROMOTED_PAWN("+P","+p");

    private final String[] chr;

    Piece(String p1chr, String p2chr) {
        chr = new String[] { p1chr, p2chr };
    }

    String getChr(int player) {
        return chr[player];
    }

    Piece demote() {
        switch (this) {
            case ROOK:
            case PROMOTED_ROOK:
                return ROOK;
            case BISHOP:
            case PROMOTED_BISHOP:
                return BISHOP;
            case SILVER:
            case PROMOTED_SILVER:
                return SILVER;
            case PAWN:
            case PROMOTED_PAWN:
                return PAWN;
            default: return this;
        }
    }

    Piece promote() {
        switch (this) {
            case ROOK: return PROMOTED_ROOK;
            case BISHOP: return PROMOTED_BISHOP;
            case SILVER: return PROMOTED_SILVER;
            case PAWN: return PROMOTED_PAWN;
            default: return this;
        }
    }

    boolean canPromote() {
        switch (this) {
            case ROOK:
            case BISHOP:
            case SILVER:
            case PAWN: return true;
            default: return false;
        }
    }

    boolean doesGoldMoves() {
        switch (this) {
            case PROMOTED_SILVER:
            case PROMOTED_PAWN:
            case GOLD: return true;
            default: return false;
        }
    }

    boolean doesKingMoves() {
        switch (this) {
            case PROMOTED_ROOK:
            case PROMOTED_BISHOP:
            case KING: return true;
            default: return false;
        }
    }

    boolean doesRookMoves() {
        switch (this) {
            case ROOK:
            case PROMOTED_ROOK: return true;
            default: return false;
        }
    }

    boolean doesBishopMoves() {
        switch (this) {
            case BISHOP:
            case PROMOTED_BISHOP: return true;
            default: return false;
        }
    }
}
