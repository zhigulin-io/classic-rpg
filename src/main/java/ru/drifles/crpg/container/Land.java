package ru.drifles.crpg.container;

import ru.drifles.crpg.common.Drawable;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.gameobject.tile.Tile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public final class Land implements Drawable {

    private final Map<Position, Tile> graph = new HashMap<>();

    public Land() {
        var worldURL = getClass().getResource("/level1.world");

        try (var file = new BufferedReader(new FileReader(new File(Objects.requireNonNull(worldURL).toURI())))) {
            String top = null;
            String mid = file.readLine();
            String bot = file.readLine();
            int y = 0;

            while (mid != null) {
                for (int x = 0; x < mid.length(); x++) {
                    var symbol = mid.charAt(x);
                    if (symbol != '#' && symbol != '.')
                        continue;

                    var tile = graph.computeIfAbsent(new Position(x, y), p -> new Tile(p, symbol != '#'));

                    if (top != null && x < top.length()) {
                        var s = top.charAt(x);
                        if (s == '.' || s == '#')
                            tile.addWay(graph.computeIfAbsent(new Position(x, y - 1), p -> new Tile(p, s != '#')));
                    }

                    if (bot != null && x < bot.length()) {
                        var s = bot.charAt(x);
                        if (s == '.' || s == '#')
                            tile.addWay(graph.computeIfAbsent(new Position(x, y + 1), p -> new Tile(p, s != '#')));
                    }

                    if (x - 1 >= 0) {
                        var s = mid.charAt(x - 1);
                        if (s == '.' || s == '#') {
                            tile.addWay(graph.computeIfAbsent(new Position(x - 1, y), p -> new Tile(p, s != '#')));
                        }
                    }

                    if (x + 1 < mid.length()) {
                        var s = mid.charAt(x + 1);
                        if (s == '.' || s == '#') {
                            tile.addWay(graph.computeIfAbsent(new Position(x + 1, y), p -> new Tile(p, s != '#')));
                        }
                    }
                }

                top = mid;
                mid = bot;
                bot = file.readLine();
                y++;
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void draw() {
        graph.values().forEach(Tile::draw);
    }

    public Map<Position, Tile> getGraph() {
        return graph;
    }
}