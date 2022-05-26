package ru.drifles.crpg.gameobject.walker;

import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.gameobject.Land;
import ru.drifles.crpg.gameobject.tile.Way;

import java.util.Stack;

public class Walker {
    private Position position;
    private Stack<Way> route;
    private RoutingType routingType;
    private int stepNumber;

    private final Land land;

    public Walker(Land land, Position position) {
        this.position = position;
        this.route = null;
        this.routingType = RoutingType.A_STAR;
        this.land = land;
        this.stepNumber = 0;
    }

    public void setTarget(Position target) {
        if (route == null) {
            this.stepNumber = 0;
            this.route = switch (routingType) {
                case BFS -> land.routeBFS(position, target);
                case A_STAR -> land.routeAStar(position, target);
                case DFS -> land.routeDFS(position, target);
                case DIJKSTRA -> land.routeDijkstra(position, target);
                case CUSTOM -> land.routeCustom(position, target);
            };
        }
    }

    public void move() {
        var step = route.peek();

        if (stepNumber == step.cost()) {
            route.pop();
            stepNumber = 0;
            position = new Position(step.destination().x(), step.destination().y());
            if (route.size() == 0)
                route = null;
        } else {
            stepNumber++;
        }
    }

    public void setRoutingType(RoutingType routingType) {
        this.routingType = routingType;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.stepNumber = 0;
        this.route = null;
        this.position = position;
    }

    public Stack<Way> getRoute() {
        return route;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public enum RoutingType {
        BFS, A_STAR, DFS, DIJKSTRA, CUSTOM
    }
}
