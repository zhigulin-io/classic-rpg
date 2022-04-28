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

    @Override
    public void draw() {
        pathMap.forEach((navNode, navPaths) -> {
            navNode.draw();
            navPaths.forEach(NavPath::draw);
        });
    }
}
