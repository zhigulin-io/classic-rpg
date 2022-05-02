package ru.drifles.crpg.object.world.navigation;

import ru.drifles.crpg.object.Drawable;
import ru.drifles.crpg.object.world.Tile;
import ru.drifles.crpg.object.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class NavMesh implements Drawable {
    private final Map<NavNode, List<NavPath>> pathMap = new HashMap<>();

    public NavMesh(World world) {
        Arrays.stream(world.getTiles())
                .flatMap(Arrays::stream)
                .filter(Tile::isPassable)
                .map(NavNode::new)
                .forEach(point -> {
                    var ways = world.getNeighbors(point.getTile()).stream()
                            .filter(Tile::isPassable)
                            .map(NavNode::new)
                            .map(neighbor -> new NavPath(point, neighbor))
                            .collect(Collectors.toList());
                    pathMap.put(point, ways);
                });
    }

    public Stack<Tile> routeBFS(Tile startTile, Tile finishTile) {
        var queue = new ArrayDeque<NavNode>();
        var visitedSet = new HashSet<Tile>();
        var startNode = getNavNodeByTile(startTile);
        var wayPoints = new Stack<Tile>();
        var ways = new HashMap<Tile, Tile>();

        if (startNode == null)
            return wayPoints;

        queue.add(startNode);
        visitedSet.add(startTile);

        while (!queue.isEmpty()) {
            var currentNode = queue.removeFirst();
            if (currentNode.getTile().equals(finishTile))
                break;

            for (var path : pathMap.get(currentNode)) {
                var target = path.getTo().getTile();
                if (!visitedSet.contains(target)) {
                    visitedSet.add(target);
                    ways.put(target, currentNode.getTile());
                    queue.add(path.getTo());
                }
            }
        }

        wayPoints.push(finishTile);
        Tile tile = ways.get(finishTile);
        while (tile != null) {
            if (tile != startTile)
                wayPoints.push(tile);
            tile = ways.get(tile);
        }

        return wayPoints;
    }

    @Override
    public void draw() {
        pathMap.forEach((navNode, navPaths) -> {
            navNode.draw();
            navPaths.forEach(NavPath::draw);
        });
    }

    private NavNode getNavNodeByTile(Tile tile) {
        for (var navNode : pathMap.keySet()) {
            if (navNode.getTile().equals(tile))
                return navNode;
        }
        return null;
    }
}
