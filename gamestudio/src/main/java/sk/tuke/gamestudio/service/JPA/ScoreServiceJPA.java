package sk.tuke.gamestudio.service.JPA;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.exceptions.ScoreException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addScore(Score score) throws ScoreException {
        entityManager.persist(score);
    }

    @Override
    public List<Score> getBestScores(String gameName) throws ScoreException {
        return entityManager.createQuery(
                "SELECT s FROM Score s WHERE s.game = :gameName ORDER BY s.points DESC")
                .setParameter("gameName", gameName).setMaxResults(10).getResultList();
    }

}
