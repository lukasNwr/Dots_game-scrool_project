package sk.tuke.gamestudio.service.JPA;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.exceptions.CommentException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addComment(Comment comment) throws CommentException {
        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComments(String gameName) throws CommentException {
        return entityManager.createQuery(
                "SELECT c FROM Comment c WHERE c.game = :gameName ORDER BY c.commentedon DESC ")
                .setParameter("gameName", gameName).getResultList();
    }
}
