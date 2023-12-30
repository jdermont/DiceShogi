package com.codingame.view;

import com.codingame.game.Piece;
import com.codingame.gameengine.module.entities.Sprite;

public class PieceSprite {
    public Piece piece;
    public Sprite sprite;

    public PieceSprite(Piece piece, Sprite sprite) {
        this.piece = piece;
        this.sprite = sprite;
    }
}