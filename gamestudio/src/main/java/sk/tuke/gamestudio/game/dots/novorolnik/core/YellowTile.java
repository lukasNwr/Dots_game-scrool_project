package sk.tuke.gamestudio.game.dots.novorolnik.core;

public class YellowTile extends Tile {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    private boolean isMarked;

    public YellowTile() {
        this.isMarked = false;
    }

    @Override
    public boolean isYellow() {
        return true;
    }

    @Override
    public String toString() {
        return super.isMarked() ? ANSI_YELLOW + "  y" + ANSI_RESET :  ANSI_YELLOW + "  0" + ANSI_RESET;
    }
}
