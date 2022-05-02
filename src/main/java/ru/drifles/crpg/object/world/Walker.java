package ru.drifles.crpg.object.world;

import ru.drifles.crpg.ClassicRPG;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.object.Drawable;
import ru.drifles.crpg.renderer.Renderer;
import ru.drifles.crpg.renderer.WalkerRenderer;

import java.util.Stack;

public class Walker implements Drawable {
    private final Position position;
    private final Renderer renderer;

    private Stack<Tile> route = null;

    public Walker(Position position) {
        this.position = position;
        this.renderer = new WalkerRenderer(this);
    }

    public Position getPosition() {
        return position;
    }


    public void setTarget(Position target) {
        var start = ClassicRPG.getInstance().getWorld().getTiles()[(int) position.getY()][(int) position.getX()];
        var finish = ClassicRPG.getInstance().getWorld().getTiles()[(int) target.getY()][(int) target.getX()];
        this.route = ClassicRPG.getInstance().getWorld().getNavMesh().routeBFS(start, finish);
    }

    @Override
    public void draw() {
        if (route != null && route.size() != 0) {
            move();
        }

        renderer.render();
    }

    private void move() {
        double dX = route.peek().getPosition().getX() - position.getX();
        double dY = route.peek().getPosition().getY() - position.getY();
        double moveSpeed = 0.1;

        if (Math.abs(dX) < moveSpeed && Math.abs(dY) < moveSpeed) {
            var tile = route.pop();
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
