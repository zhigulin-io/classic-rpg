package ru.drifles.crpg.gameobject.walker;

import ru.drifles.crpg.*;
import ru.drifles.crpg.common.Drawable;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.common.Renderer;
import ru.drifles.crpg.gameobject.tile.Tile;
import ru.drifles.crpg.gameobject.tile.Way;

import java.util.*;

public class Walker implements Drawable {
    private final Position position;
    private final Renderer renderer;
    private Stack<Way> route;

    public Walker(Position position) {
        this.position = position;
        this.renderer = new WalkerRenderer(this);
        this.route = null;
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
            this.route = routeBFS(start, target);
        }
    }

    @Override
    public void draw() {
        if (route != null)
            move();

        renderer.render();
    }

    private Stack<Way> routeBFS(Tile startTile, Tile finishTile) {
        var queue = new LinkedList<Tile>();
        var visitedSet = new HashSet<Tile>();
        var route = new Stack<Way>();

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
                if (target.isPassable() && !visitedSet.contains(target)) {
                    target.setSourceWay(way);
                    queue.add(target);
                }
            }
        }

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
}
