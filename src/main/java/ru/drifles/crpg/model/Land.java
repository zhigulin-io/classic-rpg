package ru.drifles.crpg.model;

import ru.drifles.crpg.view.Camera;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public final class Land {

    private final Tile[][] graph;

    public Land(String levelName) throws URISyntaxException, IOException {
        var worldURL = getClass().getResource(levelName);

        try (var file = new BufferedReader(new FileReader(new File(Objects.requireNonNull(worldURL).toURI())))) {
            this.graph = new Tile[(int) file.lines().count()][];
        }

        try (var file = new BufferedReader(new FileReader(new File(Objects.requireNonNull(worldURL).toURI())))) {
            for (int y = 0; y < graph.length; y++) {
                var line = file.readLine();

                graph[y] = new Tile[line.length()];
                for (int x = 0; x < line.length(); x++)
                    graph[y][x] = new Tile(new Position(x, y), line.charAt(x) == '.', new ArrayList<>());
            }
        }

        Camera.setTilesNumber(graph.length);

        for (int y = 0; y < graph.length; y++) {
            if (graph[y].length > Camera.getTilesNumber())
                Camera.setTilesNumber(graph[y].length);

            for (int x = 0; x < graph[y].length; x++) {
                var tile = graph[y][x];
                if (tile.passable()) {
                    int leftX = x - 1;
                    int rightX = x + 1;
                    int topY = y - 1;
                    int bottomY = y + 1;

                    var top = topY >= 0 ? graph[topY][x] : null;
                    var bottom = bottomY < graph.length ? graph[bottomY][x] : null;
                    var right = rightX < graph[y].length ? graph[y][rightX] : null;
                    var left = leftX >= 0 ? graph[y][leftX] : null;
                    var topLeft = leftX >= 0 && topY >= 0 ? graph[topY][leftX] : null;
                    var topRight = topY >= 0 && rightX < graph[topY].length ? graph[topY][rightX] : null;
                    var bottomLeft = leftX >= 0 && bottomY < graph.length ? graph[bottomY][leftX] : null;
                    var bottomRight =
                            bottomY < graph.length && rightX < graph[bottomY].length
                                    ? graph[bottomY][rightX]
                                    : null;

                    if (top != null && top.passable())
                        tile.ways().add(new Way(tile.position(), top.position(), 10));
                    if (bottom != null && bottom.passable())
                        tile.ways().add(new Way(tile.position(), bottom.position(), 10));
                    if (right != null && right.passable())
                        tile.ways().add(new Way(tile.position(), right.position(), 10));
                    if (left != null && left.passable())
                        tile.ways().add(new Way(tile.position(), left.position(), 10));

                    if (topLeft != null && topLeft.passable()
                            && top != null && top.passable()
                            && left != null && left.passable())
                        tile.ways().add(new Way(tile.position(), topLeft.position(), 14));

                    if (bottomLeft != null && bottomLeft.passable()
                            && bottom != null && bottom.passable()
                            && left != null && left.passable())
                        tile.ways().add(new Way(tile.position(), bottomLeft.position(), 14));

                    if (topRight != null && topRight.passable()
                            && top != null && top.passable()
                            && right != null && right.passable())
                        tile.ways().add(new Way(tile.position(), topRight.position(), 14));

                    if (bottomRight != null && bottomRight.passable()
                            && bottom != null && bottom.passable()
                            && right != null && right.passable())
                        tile.ways().add(new Way(tile.position(), bottomRight.position(), 14));
                }
            }
        }
    }

    public Tile[][] getGraph() {
        return graph;
    }

    public Stack<Way> routeAStar(Position source, Position destination) {
        var openList = new PriorityQueue<MetricRecord>();
        var visitedSet = new HashSet<Position>();
        var backStepMap = new HashMap<Position, Way>();
        var positionDistanceMap = new HashMap<Position, Integer>();

        openList.add(new MetricRecord(source, heuristicDistance(source, destination)));
        positionDistanceMap.put(source, 0);

        while (!openList.isEmpty()) {
            var record = openList.poll();
            visitedSet.add(record.position);

            if (record.position.equals(destination))
                break;

            for (var way : graph[record.position.y()][record.position.x()].ways()) {
                var target = way.destination();

                if (visitedSet.contains(target))
                    continue;

                var newTargetDistance = positionDistanceMap.get(record.position) + way.cost();

                var targetRecord = new MetricRecord(target, newTargetDistance + heuristicDistance(target, destination));
                if (!openList.contains(targetRecord)) {
                    backStepMap.put(target, way);
                    positionDistanceMap.put(target, newTargetDistance);
                    openList.add(targetRecord);
                } else if (newTargetDistance < positionDistanceMap.get(target)) {
                    positionDistanceMap.replace(target, newTargetDistance);
                    backStepMap.replace(target, way);
                }
            }
        }

        return buildRoute(destination, backStepMap);
    }

    public Stack<Way> routeCustom(Position source, Position destination) {
        var rightWave = raycastFrom(destination, RaycastDirection.RIGHT);
        var topRightWave = raycastFrom(destination, RaycastDirection.TOP_RIGHT);
        var topWave = raycastFrom(destination, RaycastDirection.TOP);
        var topLeftWave = raycastFrom(destination, RaycastDirection.TOP_LEFT);
        var leftWave = raycastFrom(destination, RaycastDirection.LEFT);
        var bottomLeftWave = raycastFrom(destination, RaycastDirection.BOTTOM_LEFT);
        var bottomWave = raycastFrom(destination, RaycastDirection.BOTTOM);
        var bottomRightWave = raycastFrom(destination, RaycastDirection.BOTTOM_RIGHT);

        var openList = new PriorityQueue<MetricRecord>();
        var visitedSet = new HashSet<Position>();
        var backStepMap = new HashMap<Position, Way>();
        var positionDistanceMap = new HashMap<Position, Integer>();

        openList.add(new MetricRecord(source, heuristicDistance(source, destination)));
        positionDistanceMap.put(source, 0);

        while (!openList.isEmpty()) {
            var record = openList.poll();
            visitedSet.add(record.position);

            if (record.position.equals(destination))
                break;

            var dX = destination.x() - record.position.x();
            var dY = destination.y() - record.position.y();

            if (dX == 0) {
                if (dY > 0 && dY <= destination.y() - topWave.y()) {
                    backStepMap.put(destination, new Way(record.position, destination, 10 * dY));
                    break;
                } else if (dY < 0 && dY >= destination.y() - bottomWave.y()){
                    backStepMap.put(destination, new Way(record.position, destination, 10 * -dY));
                    break;
                }
            } else if (dY == 0) {
                if (dX > 0 && dX <= destination.x() - leftWave.x()) {
                    backStepMap.put(destination, new Way(record.position, destination, 10 * dX));
                    break;
                } else if (dX < 0 && dX >= destination.x() - rightWave.x()) {
                    backStepMap.put(destination, new Way(record.position, destination, 10 * -dX));
                    break;
                }
            } else if (Math.abs(dX) == Math.abs(dY)) {
                var isLeft = dX > 0;
                var isTop = dY > 0;

                if (        isLeft &&  isTop && dY <= destination.y() - topLeftWave.y()
                        ||  isLeft && !isTop && dY >= destination.y() - bottomLeftWave.y()
                        || !isLeft &&  isTop && dY <= destination.y() - topRightWave.y()
                        || !isLeft && !isTop && dY >= destination.y() - bottomRightWave.y()
                ) {
                    backStepMap.put(destination, new Way(record.position, destination, 14 * Math.abs(dY)));
                    break;
                }
            }

            for (var way : graph[record.position.y()][record.position.x()].ways()) {
                var target = way.destination();

                if (visitedSet.contains(target))
                    continue;

                var newTargetDistance = positionDistanceMap.get(record.position) + way.cost();

                var targetRecord = new MetricRecord(target, newTargetDistance + heuristicDistance(target, destination));
                if (!openList.contains(targetRecord)) {
                    backStepMap.put(target, way);
                    positionDistanceMap.put(target, newTargetDistance);
                    openList.add(targetRecord);
                } else if (newTargetDistance < positionDistanceMap.get(target)) {
                    positionDistanceMap.replace(target, newTargetDistance);
                    backStepMap.replace(target, way);
                }
            }
        }

        return buildRoute(destination, backStepMap);
    }

    public Stack<Way> routeBFS(Position source, Position destination) {
        var queue = new LinkedList<Position>();
        var visitedSet = new HashSet<Position>();
        var backStepMap = new HashMap<Position, Way>();

        queue.add(source);

        while (!queue.isEmpty()) {
            var position = queue.remove();

            if (position.equals(destination))
                break;

            visitedSet.add(position);

            for (var way : graph[position.y()][position.x()].ways()) {
                var target = way.destination();
                if (!visitedSet.contains(target) && !queue.contains(target)) {
                    backStepMap.put(target, way);
                    queue.add(target);
                }
            }
        }

        return buildRoute(destination, backStepMap);
    }

    public Stack<Way> routeDFS(Position source, Position destination) {
        var stack = new Stack<Position>();
        var visitedSet = new HashSet<Position>();
        var backStepMap = new HashMap<Position, Way>();

        stack.add(source);

        while (!stack.isEmpty()) {
            var position = stack.pop();

            if (position.equals(destination))
                break;

            visitedSet.add(position);

            for (var way : graph[position.y()][position.x()].ways()) {
                var target = way.destination();
                if (!visitedSet.contains(target) && !stack.contains(target)) {
                    backStepMap.put(target, way);
                    stack.add(target);
                }
            }
        }

        return buildRoute(destination, backStepMap);
    }

    public Stack<Way> routeDijkstra(Position source, Position destination) {
        var openList = new PriorityQueue<MetricRecord>();
        var visitedSet = new HashSet<Position>();
        var backStepMap = new HashMap<Position, Way>();
        var positionDistanceMap = new HashMap<Position, Integer>();

        openList.add(new MetricRecord(source, 0.0));
        positionDistanceMap.put(source, 0);

        while (!openList.isEmpty()) {
            var record = openList.poll();
            visitedSet.add(record.position);

            if (record.position.equals(destination))
                break;

            for (var way : graph[record.position.y()][record.position.x()].ways()) {
                var target = way.destination();

                if (visitedSet.contains(target))
                    continue;

                var newTargetDistance = positionDistanceMap.get(record.position) + way.cost();
                var targetRecord = new MetricRecord(target, (double) newTargetDistance);

                if (!openList.contains(targetRecord)) {
                    backStepMap.put(target, way);
                    positionDistanceMap.put(target, newTargetDistance);
                    openList.add(targetRecord);
                } else if (newTargetDistance < positionDistanceMap.get(target)) {
                    positionDistanceMap.replace(target, newTargetDistance);
                    backStepMap.replace(target, way);
                }
            }
        }

        return buildRoute(destination, backStepMap);
    }

    private Position raycastFrom(Position position, RaycastDirection direction) {
        var x = position.x();
        var y = position.y();

        var diagonal = switch (direction) {
            case TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT -> true;
            case TOP, LEFT, RIGHT, BOTTOM -> false;
        };

        while(  y + direction.dY() >= 0 && y + direction.dY() < graph.length
                && x + direction.dX() >= 0 && x + direction.dX() < graph[y + direction.dY()].length
                && graph[y + direction.dY()][x + direction.dX()].passable()
        ) {
            if (diagonal && (!graph[y + direction.dY()][x].passable() || !graph[y][x + direction.dX()].passable()))
                break;

            x += direction.dX();
            y += direction.dY();
        }

        return new Position(x, y);
    }

    private double heuristicDistance(Position source, Position destination) {
        var a = (source.x() - destination.x()) * 10;
        var b = (source.y() - destination.y()) * 10;
        return Math.sqrt(a * a + b * b);
    }

    private Stack<Way> buildRoute(Position destination, Map<Position, Way> backStepMap) {
        System.out.println("BackStepMap Size = " + backStepMap.size());
        var route = new Stack<Way>();

        var backStep = backStepMap.get(destination);
        if (backStep == null)
            return null;

        while (backStep != null) {
            route.push(backStep);
            backStep = backStepMap.get(backStep.source());
        }

        return route;
    }

    private record MetricRecord(Position position, Double metric) implements Comparable<MetricRecord> {
        @Override
        public int compareTo(MetricRecord o) {
            return (int) (this.metric - o.metric);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MetricRecord record = (MetricRecord) o;
            return position.equals(record.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position);
        }
    }

    private enum RaycastDirection {
        RIGHT(1, 0),
        TOP_RIGHT(1, -1),
        TOP(0, -1),
        TOP_LEFT(-1, -1),
        LEFT(-1, 0),
        BOTTOM_LEFT(-1 ,1),
        BOTTOM(0, 1),
        BOTTOM_RIGHT(1, 1);

        private final int dX;
        private final int dY;

        RaycastDirection(int dX, int dY) {
            this.dX = dX;
            this.dY = dY;
        }

        public int dX() {
            return dX;
        }

        public int dY() {
            return dY;
        }
    }
}
