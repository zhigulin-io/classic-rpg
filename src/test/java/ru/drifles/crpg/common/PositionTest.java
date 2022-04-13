package ru.drifles.crpg.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    public void shouldReturnZeroZeroWhenDefaultConstructorIsCalled() {
        var position = new Position();

        assertEquals(0, position.getX());
        assertEquals(0, position.getY());
    }

    @Test
    public void shouldReturnSameXAndYWhenConstructorWithSingleArgIsCalled() {
        var xy = 10;

        var position = new Position(xy);

        assertEquals(xy, position.getX());
        assertEquals(xy, position.getY());
    }

    @Test
    public void shouldReturnCorrectXAndYWhenConstructorWithTwoArgsIsCalled() {
        var x = 10;
        var y = 20;

        var position = new Position(x, y);

        assertEquals(x, position.getX());
        assertEquals(y, position.getY());
    }

    @Test
    public void shouldReturnUpdatedCoordinatesAfterCoordinatesIsUpdated() {
        var x = 10;
        var y = 20;
        var xy = 100;

        var position = new Position();

        position.setX(x);
        position.setY(y);

        assertEquals(x, position.getX());
        assertEquals(y, position.getY());

        position.setXY(xy, xy);
        assertEquals(xy, position.getX());
        assertEquals(xy, position.getY());
    }
}