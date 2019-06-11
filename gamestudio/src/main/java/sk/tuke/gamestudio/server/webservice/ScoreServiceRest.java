package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.exceptions.ScoreException;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/score")
public class ScoreServiceRest {

    @Autowired
    private ScoreService scoreService;

    @POST
    @Consumes("application/json")
    public Response addScore(Score score) throws ScoreException {
        scoreService.addScore(score);
        return Response.ok().build();
    }

    //http://localhost:8080/rest/score/dots
    @GET
    @Path("/{game}")
    @Produces("application/json")
    public List<Score> getBestScores(@PathParam("game") String game) throws ScoreException {
        return scoreService.getBestScores(game);
    }
}
