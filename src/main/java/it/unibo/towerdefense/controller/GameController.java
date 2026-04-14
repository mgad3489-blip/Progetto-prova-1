package it.unibo.towerdefense.controller;

import it.unibo.towerdefense.model.GameModel;
import it.unibo.towerdefense.model.GameState;
import it.unibo.towerdefense.model.TowerType;
import it.unibo.towerdefense.model.Tower;
import it.unibo.towerdefense.model.BuildingSpot;
import it.unibo.towerdefense.view.GameView;

import javax.swing.Timer;

/**
 * Game controller – TowerSiege.
 */
public class GameController {

    private static final int TICK_DELAY_MS = 16;

    private final GameModel model;
    private final GameView view;
    private Timer gameLoop;
    private TowerType selectedTowerType;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.selectedTowerType = TowerType.BASIC;

        this.gameLoop = new Timer(TICK_DELAY_MS, e -> {
            if (model.getState() == GameState.PLAYING || model.getState() == GameState.VICTORY) {
                model.update();
            }
            view.displayGameState(model, GameController.this);

            if (model.isVictoryRedirectReady()) {
                gameLoop.stop();
                view.closeGameFrame();
                model.setState(GameState.LEVEL_SELECT);
                view.showLevelSelect(GameController.this, model);
            }
        });
    }

    public void start() {
        view.displayWelcome();
        view.showStartMenu(this);
    }

    public void beginGame() {
        view.closeGameFrame();
        model.setState(GameState.LEVEL_SELECT);
        view.showLevelSelect(this, model);
    }

    public void startLevel(int level) {
        model.loadLevel(level);
        model.start();
        view.closeGameFrame();
        if (!gameLoop.isRunning()) gameLoop.start();
    }

    public void togglePause() {
        if (model.getState() == GameState.PLAYING) {
            model.pause();
            view.showPauseMenu(this);
        } else if (model.getState() == GameState.PAUSED) {
            model.resume();
            view.hidePauseMenu();
        }
    }

    public void restartGame() {
        model.start();
        view.hidePauseMenu();
        if (!gameLoop.isRunning()) gameLoop.start();
    }

    public void backToMenu() {
        gameLoop.stop();
        view.hidePauseMenu();
        view.closeGameFrame();
        view.showStartMenu(this);
    }

    public void backToLevelSelect() {
        gameLoop.stop();
        view.hidePauseMenu();
        view.closeGameFrame();
        model.setState(GameState.LEVEL_SELECT);
        view.showLevelSelect(this, model);
    }

    public void forceNextWave() {
        if (model.getState() == GameState.PLAYING) model.startNextWave();
    }

    public void triggerRainOfFire() { model.castRainOfFire(); }
    public void triggerGlobalFreeze() { model.castGlobalFreeze(); }

    /** Left-click on spot: build or upgrade */
    public void interactWithSpot(BuildingSpot spot) {
        if (model.getState() != GameState.PLAYING || spot == null) return;
        if (!spot.isOccupied()) {
            Tower t = new Tower(selectedTowerType);
            model.buildTowerOnSpot(t, spot);
        } else {
            model.upgradeTower(spot.getTower());
        }
    }

    /** Right-click on spot: sell tower */
    public void sellTowerAtSpot(BuildingSpot spot) {
        if (model.getState() != GameState.PLAYING || spot == null) return;
        if (spot.isOccupied()) {
            model.sellTower(spot);
        }
    }

    public void setSelectedTowerType(TowerType type) {
        if (type != null) this.selectedTowerType = type;
    }

    public TowerType getSelectedTowerType() { return selectedTowerType; }
}