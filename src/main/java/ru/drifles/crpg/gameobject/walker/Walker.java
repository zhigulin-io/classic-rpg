package ru.drifles.crpg.gameobject.walker;

import ru.drifles.crpg.*;
import ru.drifles.crpg.common.Drawable;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.common.Renderer;
import ru.drifles.crpg.gameobject.tile.Tile;

import java.util.*;

public class Walker implements Drawable {
    private final Position position;
    private final Renderer renderer;
    private Stack<Tile> route;

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

    private Stack<Tile> routeBFS(Tile startTile, Tile finishTile) {
        var queue = new LinkedList<Tile>();
        var visitedSet = new HashSet<Tile>();
        var wayPoints = new Stack<Tile>();
        var ways = new HashMap<Tile, Tile>();

        queue.add(startTile);
        visitedSet.add(startTile);

        var lastAdded = startTile;

        while (!queue.isEmpty()) {
            var currentTile = queue.remove();
            if (currentTile == null)
                break;

            if (currentTile.equals(finishTile)) {
                wayPoints.push(currentTile);
                break;
            }

            for (var way : currentTile.getWays()) {
                var to = way.to();
                if (to.isPassable() && !visitedSet.contains(to)) {
                    visitedSet.add(to);
                    ways.put(to, currentTile);
                    queue.add(to);
                    lastAdded = to;
                }
            }
        }

        var tile = ways.get(finishTile);
        if (tile == null) {
            tile = ways.get(lastAdded);
            wayPoints.push(lastAdded);
        }

        while (tile != null) {
            if (tile != startTile)
                wayPoints.push(tile);
            tile = ways.get(tile);
        }

        return wayPoints;
    }

    private void move() {
        float dX = route.peek().getPosition().getX() - position.getX();
        float dY = route.peek().getPosition().getY() - position.getY();

        float moveSpeed = 0.1f;
        if (Math.abs(dX) < moveSpeed && Math.abs(dY) < moveSpeed) {
            var tile = route.pop();
            if (route.size() == 0)
                route = null;
            position.setXY(tile.getPosition().getX(), tile.getPosition().getY());
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
