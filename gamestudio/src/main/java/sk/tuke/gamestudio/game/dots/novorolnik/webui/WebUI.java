package sk.tuke.gamestudio.game.dots.novorolnik.webui;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.dots.novorolnik.core.Field;
import sk.tuke.gamestudio.game.dots.novorolnik.core.GameState;
import sk.tuke.gamestudio.game.dots.novorolnik.core.Tile;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.exceptions.RatingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Formatter;

public class WebUI {
    private Field field;
    private ScoreService scoreService;
    private RatingService ratingService;
    private int scoreValue;
    private boolean gameEnded = false;
    String username = "DefaultUser";

    public WebUI(ScoreService scoreService, RatingService ratingService) {
        this.ratingService = ratingService;
        this.scoreService = scoreService;
        createField();
    }

    private int getScoreValue() {
        return scoreValue;
    }

    public void processUsername(String name) {
        username = name;
    }


    public void processCommand(String command, String rowString, String columnString) {
        if (command.equals("new")) {
            createField();
            return;
        } else if (command.equals("confirm")) {
            field.finish();
            scoreValue = field.getScoreValue();
            field.resetMarkedTile();
        } else if (command.equals("mark")) {
            System.out.println("row: " + rowString + " col: " + columnString);
            if (!field.getTile(Integer.parseInt(rowString), Integer.parseInt(columnString)).isMarked()) {
                field.markTile(Integer.parseInt(rowString), Integer.parseInt(columnString));
                return;
            } else {
                field.resetTile(Integer.parseInt(rowString), Integer.parseInt(columnString));
                field.resetMarkedTile();
                return;
            }
        } else {
            username = command;
            return;
        }

    }

    private String prepareImage(Tile tile) {
        String imageString = "";
        if (tile.isBlue()) {
            imageString = "blue";
        } else if (tile.isGreen()) {
            imageString = "green";
        } else if (tile.isRed()) {
            imageString = "red";
        } else if (tile.isYellow()) {
            imageString = "yellow";
        }

        if (tile.isMarked()) imageString += "_marked";

        return imageString;
    }

    public String renderAsHtml() {

        Formatter sb = new Formatter();
        sb.format("<p>Score: " + scoreValue + "</p>\n");
        sb.format("<p>Moves: " + field.getMoves() + "<p>\n");
        sb.format("<table class='field'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.format("<tr class='tr1'>\n");
            for (int column = 0; column < field.getColCount(); column++) {
                sb.format("<td class='td1'>\n");
                sb.format("<a href='?command=mark&row=" + row + "&column=" + column + "'>");
                sb.format("<img src='/images/dots/novorolnik/"
                        + prepareImage(field.getTile(row, column)) + ".png' width='75px' height='75px'>");
                sb.format("</a>\n");
            }
        }
        sb.format("</table>\n");
        return sb.toString();
    }

    public void createField() {
        field = new Field(5, 5, 5);
    }

    public boolean isGameWon() {
        return field.getGameState() == GameState.END;
    }

    public boolean isGameLost() {
        return field.getGameState() == GameState.LOST;
    }

    public boolean isGameEnded() {
        return isGameLost() || isGameWon();
    }

    public int getScore() {
        if (isGameEnded() && !gameEnded) {
            gameEnded = true;
            if (isGameWon()) {

                scoreService.addScore(
                        // TODO: request username from currently playing user,
                        //  since because the game will be running on server, system username will be always the same!
                        new Score("dots-novorolnik", username, scoreValue, new Date()));
            }
        }
        return scoreValue;
    }

    public int getAvgRating() {
        int avgRating = 0;
        try {
            avgRating = ratingService.getAverageRating("dots-novorolnik");
        }catch (RatingException e){
            System.out.println(e.getCause());
        }
        return avgRating;
    }

    public String getUserName() {
        if(username == null) return "null";
         else return username;
    }


}
