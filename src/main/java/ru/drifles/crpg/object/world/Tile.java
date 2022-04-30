package ru.drifles.crpg.object.world;

import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.object.Drawable;
import ru.drifles.crpg.renderer.Renderer;
import ru.drifles.crpg.renderer.TileRenderer;

public class Tile implements Drawable {
	private final Position position;
	private final boolean passable;
	private final Renderer renderer;

	public Tile(int x, int y, boolean passable) {
		this.position = new Position(x, y);
		this.passable = passable;
		this.renderer = new TileRenderer(this);
	}

	public boolean isPassable() {
		return passable;
	}

	public Position getPosition() {
		return position;
	}

	@Override
	public void draw() {
		renderer.render();
	}
}
