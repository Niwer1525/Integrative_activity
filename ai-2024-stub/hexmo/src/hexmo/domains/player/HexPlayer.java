package hexmo.domains.player;

import hexmo.supervisors.commons.TileType;

/**
 * Represent a player in the game
 */
public class HexPlayer {

    private final String name;
    private HexColor color;

    /**
     * Create a new player with the given name and color
     * @param name The name of the player
     * @param color The color of the player
     */
    public HexPlayer(String name, HexColor color) {
        this.name = name;
        this.color = color;
    }

    /**
     * @return The display name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * @return The color of the player
     */
    public HexColor getColor() {
        return this.color;
    }

    /**
     * @return The color of the player as a TileType
     */
    public TileType getColorAsTileType() {
        return this.color == HexColor.RED ? TileType.RED : TileType.BLUE;
    }

    /**
     * Set the color of the player
     * @param color The new color of the player
     */
    public void setColor(HexColor color) {
        this.color = color;
    }
}