/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.shared.data;

public class Stud {

    public int Colour;
    public int XOrigin;
    public int YOrigin;
    public int ZOrigin;
    public int BrickWidth;
    public int BrickHeight;
    public int BrickDepth;

    public Stud() {
    }

    public Stud(int colour, int xOrigin, int yOrigin, int zOrigin, int width, int height, int depth) {
        Colour = colour;
        XOrigin = xOrigin;
        YOrigin = yOrigin;
        ZOrigin = zOrigin;
        BrickWidth = width;
        BrickHeight = height;
        BrickDepth = depth;
    }

    public Stud(Brick brick, int xLocal, int yLocal, int zLocal) {
        this(brick.Colour, brick.XOrigin, brick.YOrigin, brick.ZOrigin, brick.Width, brick.Height, brick.Depth);
    }
}
