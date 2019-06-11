package sk.tuke.gamestudio.service.JDBC;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.exceptions.RatingException;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {

    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String Remove_Rating = "DELETE FROM rating WHERE player = ? AND game = ?;";
    public static final String SELECT_Rating_Avg = "SELECT AVG(rating) FROM rating WHERE game = ?;";
    public static final String INSERT_Rating = "INSERT INTO rating (player, game, rating, ratedon) VALUES (?, ?, ?, ?)";
    public static final String SELECT_Rating = "SELECT rating FROM rating WHERE player = ? AND game = ? ;";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(Remove_Rating)) {
                ps.setString(1, rating.getPlayer());
                ps.setString(2, rating.getGame());
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement(INSERT_Rating)) {
                ps.setString(1, rating.getPlayer());
                ps.setString(2, rating.getGame());
                ps.setInt(3, rating.getRating());
                ps.setDate(4, new Date(rating.getRatedon().getTime()));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatingException("Error saving rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        int avgRating;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_Rating_Avg)) {
                ps.setString(1, game);
                try {
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    avgRating = rs.getInt(1);
                } catch (SQLException e) {
                    throw new RatingException("Error loading avg rating", e);
                }

            }
        } catch (SQLException e) {
            throw new RatingException("Error loading avg rating", e);
        }
        return avgRating;
    }


    @Override
    public int getRating(String game, String player) throws RatingException {
        int rating;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_Rating)) {
                ps.setString(1, player);
                ps.setString(2, game);
                try {
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    rating = rs.getInt(1);
                } catch (SQLException e) {
                    throw new RatingException("Error loading rating", e);
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading rating", e);
        }
        return rating;
    }
}
