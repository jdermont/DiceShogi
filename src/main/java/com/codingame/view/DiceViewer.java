package com.codingame.view;

import com.codingame.game.Game;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;

public class DiceViewer {
    private GraphicEntityModule graphicEntityModule;
    private Sprite[] dices;

    private Game game;

    public DiceViewer(Game game, GraphicEntityModule graphicEntityModule) {
        this.game = game;
        this.graphicEntityModule = graphicEntityModule;
    }

    public void init() {
        dices = new Sprite[6];
        for (int i=0; i < 6; i++) {
            Sprite dice = graphicEntityModule.createSprite();
            dice.setBaseWidth(192);
            dice.setBaseHeight(192);
            dice.setX(200);
            dice.setY(60);
            dice.setAnchorX(0.5f);
            dice.setAlpha(0.0f, Curve.IMMEDIATE);
            dice.setImage(getImage(i+1));
            dice.setZIndex(1);
            dices[i] = dice;
        }
    }

    public void updateFrame() {
        for (int i=0; i < 6; i++) {
            if (game.getLastDiceRoll()-1 == i) {
                dices[i].setAlpha(1.0f, Curve.IMMEDIATE);
                dices[i].setX(game.gameState.currentPlayer == 0 ? 224 : 1672, Curve.IMMEDIATE);
                dices[i].setY(game.gameState.currentPlayer == 0 ? 60 : (1080-60-192), Curve.IMMEDIATE);
            } else {
                dices[i].setAlpha(0.0f, Curve.IMMEDIATE);
            }
        }
    }

    private String getImage(int diceRoll) {
        switch (diceRoll) {
            case 1: return "d1.png";
            case 2: return "d2.png";
            case 3: return "d3.png";
            case 4: return "d4.png";
            case 5: return "d5.png";
            case 6: return "d6.png";
        }
        return "empty.png";
    }
}
