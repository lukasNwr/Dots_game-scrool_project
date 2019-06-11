package sk.tuke.gamestudio.game.dots.novorolnik.core;

public class RedTile extends Tile {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    private boolean isMarked;

    public RedTile() {
        this.isMarked = false;
    }

    @Override
    public boolean isRed() {
        return true;
    }

    @Override
    public String toString() {
        return super.isMarked() ? ANSI_RED + "  r" + ANSI_RESET : ANSI_RED + "  0" + ANSI_RESET;
    }
}
