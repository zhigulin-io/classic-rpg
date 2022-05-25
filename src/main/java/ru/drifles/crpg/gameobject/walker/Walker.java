package ru.drifles.crpg.gameobject.walker;

import ru.drifles.crpg.ClassicRPG;
import ru.drifles.crpg.common.Drawable;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.common.Renderer;
import ru.drifles.crpg.common.Time;
import ru.drifles.crpg.gameobject.tile.Tile;
import ru.drifles.crpg.gameobject.tile.Way;

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
            var land = ClassicRPG.getInstance().getWorld().getLand();
            var start = land.getGraph().get(position);


            this.route = switch (routingType) {
                case BFS -> land.routeBFS(start, target);
                case A_STAR -> land.routeAStar(start, target);
                case DFS -> land.routeDFS(start, target);
                case DIJKSTRA -> land.routeDijkstra(start, target);
            };
        }
    }

    @Override
    public void draw() {
        if (route != null)
            move();

        renderer.render();
    }

    private void move() {
        float dX = route.peek().to().getPosition().getX() - position.getX();
        float dY = route.peek().to().getPosition().getY() - position.getY();

        float moveSpeed = (float) (5.0f * Time.delta);
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

            if (dY < 0)
                position.setY(position.getY() - moveSpeed);
            else if (dY > 0)
                position.setY(position.getY() + moveSpeed);
        }
    }

    public void setRoutingType(RoutingType routingType) {
        this.routingType = routingType;
    }

    public enum RoutingType {
        BFS, A_STAR, DFS, DIJKSTRA
    }
}
