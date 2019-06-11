package sk.tuke.gamestudio.service.JPA;

import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.xml.bind.SchemaOutputResolver;

@Transactional
public class UserServiceJPA implements UserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User login(String username, String password) {
        User user = (User)entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username AND u.password=:password")
                .setParameter("username", username).setParameter("password", password).getSingleResult();

        if(user == null) {
            System.out.println("User is null");
        }

        return user;
    }

    @Override
    public User register(String username, String passwd) {
        User user = new User(username, passwd);
        entityManager.persist(user);
        return user;
    }
}
