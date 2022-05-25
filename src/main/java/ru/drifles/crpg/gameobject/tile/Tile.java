package ru.drifles.crpg.gameobject.tile;

import ru.drifles.crpg.common.Position;

import java.util.List;

public record Tile(Position position, boolean passable, List<Way> ways) {
}
