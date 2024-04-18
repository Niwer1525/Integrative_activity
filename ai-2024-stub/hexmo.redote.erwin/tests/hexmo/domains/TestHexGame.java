package hexmo.domains;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import hexmo.domains.board.tiles.AxialCoordinates;
import hexmo.domains.board.tiles.HexTile;
import hexmo.domains.player.HexColor;

public class TestHexGame {

    /*
     * Test creating new game with board sizes
    */

    @Test
    public void test_newGameR0_WithFirstPlayer() {
        HexGame game = new HexGame(0);
        assertEquals(1, // This should be 1 since there is at least the center tile
            game.getTiles().size()
        );
        assertNotNull(game.getTurnPlayer());
    }

    @Test
    public void test_newGameRAlgo() {
        HexGame game = new HexGame(1);
        assertEquals(7,
            game.getTiles().size()
        );
        HexGame game2 = new HexGame(2);
        assertEquals(19,
            game2.getTiles().size()
        );
    }

    @Test
    public void test_newGameR3_WithFirstPlayer() {
        HexGame game = new HexGame(3);
        assertEquals(37,
            game.getTiles().size()
        );
        assertNotNull(game.getTurnPlayer());
    }

    @Test
    public void test_newGameR4_WithFirstPlayer() {
        HexGame game = new HexGame(4);
        assertEquals(61,
            game.getTiles().size()
        );
        assertNotNull(game.getTurnPlayer());
    }

    @Test
    public void test_newGameR5_WithFirstPlayer() {
        HexGame game = new HexGame(5);
        assertEquals(91,
            game.getTiles().size()
        );
        assertNotNull(game.getTurnPlayer());
    }

    /*
     * Current tile activation
     */

    @Test
    public void test_activateTile_withoutSwap() {
        HexGame game = new HexGame(3);
        HexTile tile = game.play();
        assertEquals(HexColor.RED,
            tile.getColor()
        );
    }

    @Test
    public void test_activateTile_withSwap() {
        HexGame game = new HexGame(3);
        HexTile tile = game.play(true);
        assertEquals(HexColor.BLUE, tile.getColor());
    }

    @Test
    public void test_activateTile_withMultipleSwap() {
        HexGame game = new HexGame(3);
        HexTile tile = game.play(true);
        assertEquals(HexColor.BLUE, tile.getColor());
        tile = game.play();
        tile = game.play(true);
        tile = game.play();
        tile = game.play(true);
        assertNull(tile);
    }

    @Test
    public void test_activateTile_Moved() {
        HexGame game = new HexGame(3);
        game.moveTo(255, 0);

        /* 
         * This will should be 0 0 case since the "moveTo" 
         * should be unsuccessful...
        */
        HexTile tile = game.play(true);
        assertEquals(0, game.getActiveTile().getQ());
        assertEquals(0, game.getActiveTile().getR());
        assertEquals(HexColor.BLUE, tile.getColor()); // Should be the defaut color
    }

    @Test
    public void test_firstTurn() {
        HexGame game = new HexGame(3);
        assertTrue(game.isFirstTurn());
        
        game.moveTo(0, 0);
        assertTrue(game.isFirstTurn());

        game.play(true);
        assertFalse(game.isFirstTurn());

        game.play(true);
        assertFalse(game.isFirstTurn());
    }

    @Test
    public void test_getMessages() {
        HexGame game = new HexGame(3);
        assertEquals(3, game.getGameMessages().length);

        /* Main tile */
        assertArrayEquals(new String[] { 
            "P1 (rouge) vs P2 (bleu)",
            "Au tour de P1 (rouge)",
            "Case active (q: 0 r: 0 0) Libre"
        }, game.getGameMessages());

        /* Move to [1, 0] */
        game.moveTo(1, 0);
        assertArrayEquals(new String[] { 
            "P1 (rouge) vs P2 (bleu)",
            "Au tour de P1 (rouge)",
            "Case active (q: 1 r: 0 -1) Libre"
        }, game.getGameMessages());

        /* Play and swap */
        game.play(true);
        assertArrayEquals(new String[] { 
            "P1 (bleu) vs P2 (rouge)",
            "Au tour de P2 (rouge)",
            "Case active (q: 1 r: 0 -1) Bleu"
        }, game.getGameMessages());

        /* Move to [2, 0] */
        game.moveTo(1, 0);
        assertArrayEquals(new String[] { 
            "P1 (bleu) vs P2 (rouge)",
            "Au tour de P2 (rouge)",
            "Case active (q: 2 r: 0 -2) Libre"
        }, game.getGameMessages());

        /* Play and swap (Swap shouldn't affect */
        game.play(true);
        assertArrayEquals(new String[] {
            "P1 (bleu) vs P2 (rouge)",
            "Au tour de P1 (bleu)",
            "Case active (q: 2 r: 0 -2) Rouge"
        }, game.getGameMessages());
    }

    @Test
    public void test_toString() {
        HexGame game = new HexGame(3);
        assertEquals("Game: P1 (rouge) vs P2 (bleu)", game.toString());
        
        game.play(true);
        assertEquals("Game: P1 (bleu) vs P2 (rouge)", game.toString());
    }

    /* 
     * Test playing
     * */

    @Test
    public void test_moveToAndPlay() {
        HexGame game = new HexGame(3);

        // Move to [0, 2]
        game.moveTo(0, 1);
        game.moveTo(0, 1);
        game.play(); // RED PLAYS AND BLUE REFUSE SWAP
        assertEquals(new AxialCoordinates(0, 2), game.getActiveTile().getCoords());
    
        // Move to [-2, 0]
        game.moveTo(-1, -1);
        game.moveTo(-1, -1);
        game.play(); // BLUE PLAYS
        assertEquals(new AxialCoordinates(-2, 0), game.getActiveTile().getCoords());

        // Move to [-2, 2]
        game.moveTo(0, 1);
        game.moveTo(0, 1);
        game.play(); // RED PLAYS
        assertEquals(new AxialCoordinates(-2, 2), game.getActiveTile().getCoords());
    }

    @Test
    public void test_playColorsNoSwap() {
        HexGame game = new HexGame(3);

        // Move to [0, 2]
        game.moveTo(0, 1);
        game.moveTo(0, 1);
        game.play(); // RED PLAYS AND BLUE REFUSE SWAP
        
        // Move to [-2, 0]
        game.moveTo(-1, -1);
        game.moveTo(-1, -1);
        game.play(); // BLUE PLAYS
        
        // Move to [-2, 2]
        game.moveTo(0, 1);
        game.moveTo(0, 1);
        game.play(); // RED PLAYS

        assertEquals(HexColor.RED, game.getTileAt(0, 2).getColor());
        assertEquals(HexColor.BLUE, game.getTileAt(-2, 0).getColor());
        assertEquals(HexColor.RED, game.getTileAt(-2, 2).getColor());
    }

    @Test
    public void test_playColorsSwap() {
        HexGame game = new HexGame(3);

        // Move to [-2, 0]
        game.moveTo(-1, 0);
        game.moveTo(-1, 0);
        game.play(true); // RED PLAYS AND BLUE ACCEPT SWAP
        
        // Move to [-1, 0]
        game.moveTo(1, 0);
        game.play(); // BLUE PLAYS
        
        // Move to [-2, 2]
        game.moveTo(0, 1);
        game.moveTo(0, 1);
        game.play(); // RED PLAYS

        assertEquals(HexColor.RED, game.getTileAt(-2, 0).getColor());
        assertEquals(HexColor.RED, game.getTileAt(-1, 2).getColor());
        assertEquals(HexColor.BLUE, game.getTileAt(-1, 0).getColor());
    }

    @Test
    public void test_errorMessageWhenTileAlreadyOccupied() {
        HexGame game = new HexGame(3);

        // Move to [-2, 0]
        game.moveTo(-1, 0);
        game.moveTo(-1, 0);
        game.play(true); // RED PLAYS AND BLUE REFUSES SWAP

        // Try to move to [-2, 0] again
        game.moveTo(-1, 0);
        game.moveTo(-1, 0);
        game.play(); // BLUE PLAYS

        assertEquals(HexColor.RED, game.getTileAt(-2, 0).getColor());
        assertEquals(HexGame.TILE_ALREADY_CLAIMED, game.getGameMessages()[2]);
        assertEquals(HexColor.UNKNOWN, game.getTileAt(0, 2).getColor());
        assertEquals(HexColor.UNKNOWN, game.getTileAt(-2, 2).getColor());
    }
}