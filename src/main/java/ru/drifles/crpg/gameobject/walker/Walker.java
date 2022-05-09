package ru.drifles.crpg.gameobject.walker;

import ru.drifles.crpg.ClassicRPG;
import ru.drifles.crpg.common.Drawable;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.common.Renderer;
import ru.drifles.crpg.gameobject.tile.Tile;
import ru.drifles.crpg.gameobject.tile.Way;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Walker implements Drawable {
    private final Position position;
    private final Renderer renderer;
    private Stack<Way> route;
    private RoutingType routingType;

    public Walker(Position position) {
        this.position = position;
        this.renderer = new WalkerRenderer(this);
        this.route = null;
        this.routingType = RoutingType.A_STAR;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Tile tile) {
        this.route = null;
        this.position.setXY(tile.getPosition().getX(), tile.getPosition().getY());
    }

    public void setTarget(Tile target) {
        if (route == null) {
            var start = ClassicRPG.getInstance().getWorld().getLand().getGraph().get(position);

            this.route = switch (routingType) {
                case BFS -> routeBFS(start, target);
                case A_STAR -> routeAStar(start, target);
            };
        }
    }

    @Override
    public void draw() {
        if (route != null)
            move();

        renderer.render();
    }

    private Stack<Way> routeAStar(Tile startTile, Tile finishTile) {
        var openQueue = new PriorityQueue<Tile>();
        var closeQueue = new PriorityQueue<Tile>();

        startTile.setSourceWay(null);
        finishTile.setSourceWay(null);

        startTile.setG(0);
        startTile.setH(heuristicDistance(startTile, finishTile));
        startTile.setF(startTile.getG() + startTile.getH());
        openQueue.add(startTile);

        while (!openQueue.isEmpty()) {
            var tile = openQueue.poll();
            closeQueue.add(tile);

            if (tile.equals(finishTile))
                break;

            for (var way : tile.getWays()) {
                var target = way.to();

                if (!target.isPassable() || closeQueue.contains(target))
                    continue;

                var newG = tile.getG() + 1.0;
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

    private Stack<Way> routeBFS(Tile startTile, Tile finishTile) {
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

    private void move() {
        float dX = route.peek().to().getPosition().getX() - position.getX();
        float dY = route.peek().to().getPosition().getY() - position.getY();

        float moveSpeed = 0.1f;
        if (Math.abs(dX) < moveSpeed && Math.abs(dY) < moveSpeed) {
            var tile = route.pop();
            if (route.size() == 0)
                route = null;
            position.setXY(tile.to().getPosition().getX(), tile.to().getPosition().getY());
        } else {
            if (dX < 0)
                position.setX(position.getX() - moveSpeed);
            else if (dX > 0)
                position.setX(position.getX() + moveSpeed);
            else if (dY < 0)
                position.setY(position.getY() - moveSpeed);
            else if (dY > 0)
                position.setY(position.getY() + moveSpeed);
        }
    }

    public void setRoutingType(RoutingType routingType) {
        this.routingType = routingType;
    }

    public enum RoutingType {
        BFS, A_STAR
    }
}
