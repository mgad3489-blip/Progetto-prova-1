package it.unibo.towerdefense.application;

import it.unibo.towerdefense.controller.GameController;
import it.unibo.towerdefense.model.GameModel;
import it.unibo.towerdefense.view.GameView;

/**
 * Main class for the Tower Defense game.
 */
public class TowerDefense {

    /**
     * Main method to start the game.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Tower Defense Game Starting...");
        String mapPath = null;
        if (args.length > 0) {
            mapPath = args[0];
        }
        GameModel model = new GameModel(mapPath);
        GameView view = new GameView();
        GameController controller = new GameController(model, view);
        controller.start();
    }
}