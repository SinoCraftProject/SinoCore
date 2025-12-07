package games.moegirl.sinocraft.sinocore.api.gui.layout;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rectangle {
    Point origin;
    Size size;

    public Rectangle(int x, int y, int width, int height) {
        this(new Point(x, y), new Size(width, height));
    }
}
