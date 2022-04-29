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

    /*
     * Обычный обход графа в ширину
     */
    public Set<Tile> breadthFirstSearch(Tile startTile) {
        var queue = new ArrayDeque<NavNode>();
        var visitedSet = new HashSet<Tile>();
        var startNode = getNavNodeByTile(startTile);

        if (startNode == null)
            return visitedSet;

        queue.add(startNode);
        visitedSet.add(startTile);

        while (!queue.isEmpty()) {
            var currentNode = queue.removeFirst();

            for (var path : pathMap.get(currentNode)) {
                if (!visitedSet.contains(path.getTo().getTile())) {
                    visitedSet.add(path.getTo().getTile());
                    queue.add(path.getTo());
                }
            }
        }

        return visitedSet;
    }

    /*
     * Обход в ширину для поиска кратчайших путей
     */
    public Map<Tile, Double> augmentedBreadthFirstSearch(Tile startTile) {
        var queue = new ArrayDeque<NavNode>();
        var visitedSet = new HashSet<Tile>();
        var distances = new HashMap<Tile, Double>();
        var startNode = getNavNodeByTile(startTile);

        if (startNode == null)
            return distances;

        distances.put(startTile, 0.0);
        queue.add(startNode);
        visitedSet.add(startTile);

        while (!queue.isEmpty()) {
            var currentNode = queue.removeFirst();

            for (var path : pathMap.get(currentNode)) {
                var target = path.getTo().getTile();
                if (!visitedSet.contains(target)) {
                    visitedSet.add(target);
                    distances.put(target, distances.get(currentNode.getTile()) + 1.0);
                    queue.add(path.getTo());
                }
            }
        }

        return distances;
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
