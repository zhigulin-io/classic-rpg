package ru.drifles.crpg.gameobject.tile;

import ru.drifles.crpg.common.Position;

public record Way(Position source, Position destination, int cost) {
}
