import com.ai.astar.domain.AStar;
import com.ai.astar.domain.Point;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AStarTest {

    @Test
    public void shouldBuildAnInstanceOfAStarObject() {
        AStar aStar = AStar
                .builder(6,7)
                .from(2,1)
                .to(2,5)
                .create();
        assertNotNull(aStar);
        assertEquals(10, aStar.getDiagonalCost());          //Default Diagonal Cost
        assertEquals(10,aStar.getHvCost());                 //Default cost
        assertEquals(false,aStar.isEnableDiagonalMove());    //Default diagonal movement OFF
        assertEquals(new Point(2,1), aStar.getInitialNode());
        assertEquals(new Point(2,5), aStar.getFinalNode());
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailForInvalidInitialPoint() {
        AStar aStar = AStar
                .builder(6,7)
                .from(8,1)
                .to(2,5)
                .create();
    }
    @Test(expected = RuntimeException.class)
    public void shouldFailForInvalidInitialPosition() {
        AStar aStar = AStar
                .builder(6,7)
                .from(new Point(5,8))
                .to(2,5)
                .create();
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailForInvalidFinalPoint() {
        AStar aStar = AStar
                .builder(6,7)
                .from(2,1)
                .to(2,10)
                .create();
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailForInvalidFinalPosition() {
        AStar aStar = AStar
                .builder(6,7)
                .from(2,1)
                .to(new Point(10,3))
                .create();
    }

    @Test
    public void shouldBuildTheBestPathUsingAStarStrategy() {
        List<Point> path = AStar
                .builder(8,5)
                .from(2,1)
                .to(6,4)
                .canMoveOverDiagonals()
                .withObstacles(new int[][]{{3, 2}, {3, 3}, {4, 2},{5,2}})
                .create()
                .findPath();

        assertNotNull(path);
        assertFalse(path.isEmpty());

        path.forEach(System.out::println);

//        Should print
//        Point [row=2, col=1]
//        Point [row=2, col=2]
//        Point [row=2, col=3]
//        Point [row=3, col=4]
//        Point [row=4, col=4]
//        Point [row=5, col=4]
//        Point [row=6, col=4]
    }
}
