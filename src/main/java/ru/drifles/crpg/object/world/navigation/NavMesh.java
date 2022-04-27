package ru.drifles.crpg.object.world.navigation;

import ru.drifles.crpg.object.world.Tile;
import ru.drifles.crpg.object.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class NavMesh {
    private final Map<Tile, List<Tile>> pathMap = new HashMap<>();

    public NavMesh(World world) {
        Arrays.stream(world.getTiles())
                .flatMap(Arrays::stream)
                .filter(Tile::isPassable)
                .forEach(tile -> {
                    var ways = world.getNeighbors(tile).stream()
                            .filter(Tile::isPassable)
                            .collect(Collectors.toList());
                    pathMap.put(tile, ways);
                });
    }
}
