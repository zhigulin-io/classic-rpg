package ru.drifles.crpg.jmh;

import org.junit.jupiter.api.Test;
import ru.drifles.crpg.model.Land;
import ru.drifles.crpg.model.Position;
import ru.drifles.crpg.model.Way;

import java.io.IOException;
import java.net.URISyntaxException;

public class SmallLandMetricsTest {
    private static final Land emptyLand;
    private static final Land fullLand;
    private static final Land semiLand;

    private static final Position source = new Position(0, 0);
    private static final Position destination = new Position(15, 15);


    static {
        try {
            emptyLand = new Land("/level_small_1.world");
            fullLand = new Land("/level_small_2.world");
            semiLand = new Land("/level_small_3.world");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void aStar() {
        var emptyRoute = emptyLand.routeAStar(source, destination);
        System.out.println("EMPTY A* (CNT) = " + emptyRoute.size());
        System.out.println("EMPTY A* (LEN) = " + emptyRoute.stream().mapToInt(Way::cost).sum());

        var semiRoute = semiLand.routeAStar(source, destination);
        System.out.println("SEMI A* (CNT) = " + semiRoute.size());
        System.out.println("SEMI A* (LEN) = " + semiRoute.stream().mapToInt(Way::cost).sum());

        var fullRoute = fullLand.routeAStar(source, destination);
        System.out.println("FULL A* (CNT) = " + fullRoute.size());
        System.out.println("FULL A* (LEN) = " + fullRoute.stream().mapToInt(Way::cost).sum());

        System.out.println();
    }

    @Test
    public void bfs() {
        var emptyRoute = emptyLand.routeBFS(source, destination);
        System.out.println("EMPTY BFS (CNT) = " + emptyRoute.size());
        System.out.println("EMPTY BFS (LEN) = " + emptyRoute.stream().mapToInt(Way::cost).sum());

        var semiRoute = semiLand.routeBFS(source, destination);
        System.out.println("SEMI BFS (CNT) = " + semiRoute.size());
        System.out.println("SEMI BFS (LEN) = " + semiRoute.stream().mapToInt(Way::cost).sum());

        var fullRoute = fullLand.routeBFS(source, destination);
        System.out.println("FULL BFS (CNT) = " + fullRoute.size());
        System.out.println("FULL BFS (LEN) = " + fullRoute.stream().mapToInt(Way::cost).sum());

        System.out.println();
    }

    @Test
    public void custom() {
        var emptyRoute = emptyLand.routeCustom(source, destination);
        System.out.println("EMPTY CUSTOM (CNT) = " + emptyRoute.size());
        System.out.println("EMPTY CUSTOM (LEN) = " + emptyRoute.stream().mapToInt(Way::cost).sum());

        var semiRoute = semiLand.routeCustom(source, destination);
        System.out.println("SEMI CUSTOM (CNT) = " + semiRoute.size());
        System.out.println("SEMI CUSTOM (LEN) = " + semiRoute.stream().mapToInt(Way::cost).sum());

        var fullRoute = fullLand.routeCustom(source, destination);
        System.out.println("FULL CUSTOM (CNT) = " + fullRoute.size());
        System.out.println("FULL CUSTOM (LEN) = " + fullRoute.stream().mapToInt(Way::cost).sum());

        System.out.println();
    }

    @Test
    public void dfs() {
        var emptyRoute = emptyLand.routeDFS(source, destination);
        System.out.println("EMPTY DFS (CNT) = " + emptyRoute.size());
        System.out.println("EMPTY DFS (LEN) = " + emptyRoute.stream().mapToInt(Way::cost).sum());

        var semiRoute = semiLand.routeDFS(source, destination);
        System.out.println("SEMI DFS (CNT) = " + semiRoute.size());
        System.out.println("SEMI DFS (LEN) = " + semiRoute.stream().mapToInt(Way::cost).sum());

        var fullRoute = fullLand.routeDFS(source, destination);
        System.out.println("FULL DFS (CNT) = " + fullRoute.size());
        System.out.println("FULL DFS (LEN) = " + fullRoute.stream().mapToInt(Way::cost).sum());

        System.out.println();
    }

    @Test
    public void dijkstra() {
        var emptyRoute = emptyLand.routeDijkstra(source, destination);
        System.out.println("EMPTY DIJKSTRA (CNT) = " + emptyRoute.size());
        System.out.println("EMPTY DIJKSTRA (LEN) = " + emptyRoute.stream().mapToInt(Way::cost).sum());

        var semiRoute = semiLand.routeDijkstra(source, destination);
        System.out.println("SEMI DIJKSTRA (CNT) = " + semiRoute.size());
        System.out.println("SEMI DIJKSTRA (LEN) = " + semiRoute.stream().mapToInt(Way::cost).sum());

        var fullRoute = fullLand.routeDijkstra(source, destination);
        System.out.println("FULL DIJKSTRA (CNT) = " + fullRoute.size());
        System.out.println("FULL DIJKSTRA (LEN) = " + fullRoute.stream().mapToInt(Way::cost).sum());

        System.out.println();
    }
}
