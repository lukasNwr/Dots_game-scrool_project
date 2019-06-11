package sk.tuke.gamestudio.entity;

import javax.persistence.*;

@Entity
@Table(name = "player")
@NamedQuery(name = "User.login",
        query = "SELECT u FROM User u WHERE u.username=:username AND u.password=:password")
public class User {
    @Id
    @GeneratedValue
    private int ident;

    @Column(unique = true)
    private String username;
    private String password;

    @Transient
    private String verifiedPasswd;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return ident;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "ident=" + ident +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
