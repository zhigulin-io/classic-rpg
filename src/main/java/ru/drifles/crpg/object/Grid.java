package ru.drifles.crpg.object;

import java.util.ArrayList;
import java.util.List;

public class Grid implements Drawable {

    private final List<GridCell> cells = new ArrayList<>();

    public Grid() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                cells.add(new GridCell(j, i));
            }
        }
    }

    @Override
    public void draw() {
        cells.forEach(GridCell::draw);
    }
}
