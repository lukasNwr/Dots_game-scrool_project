package sk.tuke.gamestudio.service.JDBC;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.exceptions.CommentException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {

    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String SELECT_SCORE = "SELECT player, game, comment, commentedon FROM comments WHERE game = ? ;";
    public static final String INSERT_SCORE = "INSERT INTO comments (player, game, comment, commentedon) VALUES (?, ?, ?, ?)";

    @Override
    public void addComment(Comment comment) throws CommentException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(INSERT_SCORE)){
                ps.setString(1, comment.getPlayer());
                ps.setString(2, comment.getGame());
                ps.setString(3, comment.getComment());
                ps.setDate(4, new Date(comment.getCommentedOn().getTime()));

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new CommentException("Error saving comment", e);
        }

    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_SCORE)){
                ps.setString(1, game);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        Comment comment = new Comment(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getTimestamp(4)
                        );
                        comments.add(comment);
                    }
                }
            }
        } catch (SQLException e) {
            throw new CommentException("Error loading comments", e);
        }
        return comments;

    }
}
