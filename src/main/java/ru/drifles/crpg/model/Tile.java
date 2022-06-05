package ru.drifles.crpg.model;

import java.util.List;

public record Tile(Position position, boolean passable, List<Way> ways) {
}
