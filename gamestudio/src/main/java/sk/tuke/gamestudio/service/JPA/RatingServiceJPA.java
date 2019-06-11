package sk.tuke.gamestudio.service.JPA;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.exceptions.RatingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        entityManager.createQuery("DELETE FROM Rating r WHERE r.player =:playerName")
                .setParameter("playerName", rating.getPlayer()).executeUpdate();
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String gameName) throws RatingException {
        List<Rating> ratings = entityManager.createQuery(
                "SELECT r FROM Rating r WHERE r.game = :gameName ")
                .setParameter("gameName", gameName).getResultList();
        int avgRating =0;
        for (Rating rating : ratings){
            avgRating = avgRating + rating.getRating();
        }
        avgRating = avgRating/ratings.size();
        return avgRating;
    }

    @Override
    public int getRating(String gameName, String player) throws RatingException {
        Rating rating = (Rating)entityManager.createQuery(
                "SELECT r FROM Rating r WHERE r.game = :gameName AND r.player =:player")
                .setParameter("gameName", gameName).setParameter("player", player).getSingleResult();
        if (rating == null){
            return 0;
        }
        return rating.getRating();
    }

}

