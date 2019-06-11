package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Comment implements Serializable {
    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    @Id
    @GeneratedValue
    private int ident;

    private String player;
    private String game;
    private String comment;
    private Date commentedon;

    public Comment() {}

    public Comment(String player, String game, String comment, Date commentedon) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedon = commentedon;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentedOn() {
        return commentedon;
    }

    public void setCommentedOn(Date commentedOn) {
        this.commentedon = commentedOn;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Comment{");
        sb.append("player='").append(player).append('\'');
        sb.append(", game='").append(game).append('\'');
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", commentedOn=").append(commentedon);
        sb.append('}');
        return sb.toString();
    }
}
