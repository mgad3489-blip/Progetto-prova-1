package it.unibo.towerdefense.application;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import it.unibo.towerdefense.model.GameModel;
import it.unibo.towerdefense.model.GameState;

/**
 * Test class for TowerDefense.
 */
class TowerDefenseTest {

    @Test
    void testMain() {
        // Simple test to ensure main method can be called without exceptions
        assertDoesNotThrow(() -> TowerDefense.main(new String[]{}));
    }

    @Test
    void testGameModelInitialization() {
        GameModel model = new GameModel();
        assertNotNull(model);
        assertEquals(GameState.MENU, model.getState());
        assertEquals(0, model.getCurrentWave());
    }
}