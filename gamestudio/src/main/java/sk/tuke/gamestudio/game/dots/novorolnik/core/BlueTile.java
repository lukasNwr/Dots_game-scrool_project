package sk.tuke.gamestudio.game.dots.novorolnik.core;

public class BlueTile extends Tile {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    private boolean isMarked;

    public BlueTile() {
        this.isMarked = false;
    }

    @Override
    public boolean isBlue() {
        return true;
    }

    @Override
    public String toString() {
        return super.isMarked() ? ANSI_BLUE + "  b" + ANSI_RESET :  ANSI_BLUE + "  0" + ANSI_RESET;
    }
}
