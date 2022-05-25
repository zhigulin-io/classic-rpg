package ru.drifles.crpg.container;

import ru.drifles.crpg.common.Drawable;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.gameobject.tile.Tile;
import ru.drifles.crpg.gameobject.tile.Way;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public final class Land implements Drawable {

    private final Map<Position, Tile> graph = new HashMap<>();

    public Land(String levelName) {
        var worldURL = getClass().getResource(levelName);

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

                    char leftNeighbor = 0;
                    char rightNeighbor = 0;

                    if (x - 1 >= 0) {
                        var s = mid.charAt(x - 1);
                        leftNeighbor = s;
                        if (s == '.' || s == '#') {
                            tile.addWay(graph.computeIfAbsent(new Position(x - 1, y), p -> new Tile(p, s != '#')), 10);
                        }
                    }

                    if (x + 1 < mid.length()) {
                        var s = mid.charAt(x + 1);
                        rightNeighbor = s;
                        if (s == '.' || s == '#') {
                            tile.addWay(graph.computeIfAbsent(new Position(x + 1, y), p -> new Tile(p, s != '#')), 10);
                        }
                    }

                    if (top != null && x < top.length()) {
                        var s = top.charAt(x);
                        if (s == '.' || s == '#')
                            tile.addWay(graph.computeIfAbsent(new Position(x, y - 1), p -> new Tile(p, s != '#')), 10);

                        if (x - 1 >= 0 && leftNeighbor == '.' && s == '.') {
                            var d = top.charAt(x - 1);
                            tile.addWay(
                                    graph.computeIfAbsent(new Position(x - 1, y - 1), p -> new Tile(p, d != '#')),
                                    14
                            );
                        }

                        if (x + 1 < top.length() && rightNeighbor == '.' && s == '.') {
                            var d = top.charAt(x + 1);
                            tile.addWay(
                                    graph.computeIfAbsent(new Position(x + 1, y - 1), p -> new Tile(p, d != '#')),
                                    14
                            );
                        }
                    }

                    if (bot != null && x < bot.length()) {
                        var s = bot.charAt(x);
                        if (s == '.' || s == '#')
                            tile.addWay(graph.computeIfAbsent(new Position(x, y + 1), p -> new Tile(p, s != '#')), 10);

                        if (x - 1 >= 0 && leftNeighbor == '.' && s == '.') {
                            var d = bot.charAt(x - 1);
                            tile.addWay(
                                    graph.computeIfAbsent(new Position(x - 1, y + 1), p -> new Tile(p, d != '#')),
                                    14
                            );
                        }

                        if (x + 1 < bot.length() && rightNeighbor == '.' && s == '.') {
                            var d = bot.charAt(x + 1);
                            tile.addWay(
                                    graph.computeIfAbsent(new Position(x + 1, y + 1), p -> new Tile(p, d != '#')),
                                    14
                            );
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

    public Stack<Way> routeAStar(Tile startTile, Tile finishTile) {
        var openQueue = new PriorityQueue<Tile>();
        var visitedSet = new HashSet<Tile>();

        startTile.setSourceWay(null);
        finishTile.setSourceWay(null);

        startTile.setG(0);
        startTile.setH(heuristicDistance(startTile, finishTile));
        startTile.setF(startTile.getG() + startTile.getH());
        openQueue.add(startTile);

        while (!openQueue.isEmpty()) {
            var tile = openQueue.poll();
            visitedSet.add(tile);

            if (tile.equals(finishTile))
                break;

            for (var way : tile.getWays()) {
                var target = way.to();

                if (!target.isPassable() || visitedSet.contains(target))
                    continue;

                var newG = tile.getG() + way.cost();
                if (!openQueue.contains(target) || newG < target.getG()) {
                    target.setSourceWay(way);
                    target.setG(newG);
                    target.setH(heuristicDistance(target, finishTile));
                    target.setF(target.getG() + target.getH());
                    if (!openQueue.contains(target))
                        openQueue.add(target);
                }
            }
        }

        return buildRoute(finishTile);
    }

    private double heuristicDistance(Tile from, Tile to) {
        var a = from.getPosition().getX() - to.getPosition().getX();
        var b = from.getPosition().getY() - to.getPosition().getY();
        return Math.sqrt(a * a + b * b);
    }

    public Stack<Way> routeBFS(Tile startTile, Tile finishTile) {
        var queue = new LinkedList<Tile>();
        var visitedSet = new HashSet<Tile>();

        startTile.setSourceWay(null);
        finishTile.setSourceWay(null);

        queue.add(startTile);

        while (!queue.isEmpty()) {
            var currentTile = queue.remove();

            if (currentTile.equals(finishTile))
                break;

            visitedSet.add(currentTile);

            for (var way : currentTile.getWays()) {
                var target = way.to();
                if (target.isPassable() && !visitedSet.contains(target) && !queue.contains(target)) {
                    target.setSourceWay(way);
                    queue.add(target);
                }
            }
        }

        return buildRoute(finishTile);
    }

    public Stack<Way> routeDFS(Tile from, Tile to) {
        var stack = new Stack<Tile>();
        var visitedSet = new HashSet<Tile>();

        from.setSourceWay(null);
        to.setSourceWay(null);

        stack.add(from);

        while (!stack.isEmpty()) {
            var tile = stack.pop();

            if (tile.equals(to))
                break;

            visitedSet.add(tile);

            for (var way : tile.getWays()) {
                var target = way.to();
                if (target.isPassable() && !visitedSet.contains(target) && !stack.contains(target)) {
                    target.setSourceWay(way);
                    stack.add(target);
                }
            }
        }

        return buildRoute(to);
    }

    public Stack<Way> routeDijkstra(Tile from, Tile to) {

        var openQueue = new PriorityQueue<Tile>();
        var closeQueue = new PriorityQueue<Tile>();

        from.setSourceWay(null);
        to.setSourceWay(null);

        from.setF(0);
        openQueue.add(from);

        while (!openQueue.isEmpty()) {
            var tile = openQueue.poll();
            closeQueue.add(tile);

            if (tile.equals(to))
                break;

            for (var way : tile.getWays()) {
                var target = way.to();

                if (!target.isPassable() || closeQueue.contains(target))
                    continue;

                var newF = tile.getF() + way.cost();
                if (!openQueue.contains(target) || newF < target.getF()) {
                    target.setSourceWay(way);
                    target.setF(newF);
                    if (!openQueue.contains(target))
                        openQueue.add(target);
                }
            }
        }

        return buildRoute(to);
    }

    private Stack<Way> buildRoute(Tile finishTile) {
        var route = new Stack<Way>();

        var source = finishTile.getSourceWay();
        if (source == null)
            return null;

        while (source != null) {
            route.push(source);
            source = source.from().getSourceWay();
        }

        return route;
    }
}
