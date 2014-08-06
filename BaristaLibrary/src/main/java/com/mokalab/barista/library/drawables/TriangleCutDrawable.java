package com.mokalab.barista.library.drawables;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * A Drawable that will draw a Triangle Cut within the specified outline of a Rectangle.
 * <br><br>
 * Ex.<br>
 *
 * +++++++++++++ ++++++++
 * +            V       +
 * +                    +
 * ++++++++++++++++++++++
 *
 * <br><br>
 * Author: Kevin Tse
 */
public class TriangleCutDrawable extends Drawable {

    private static final double TRIANGLE_LOCATION = 0.15;

    private Paint p = new Paint();
    private ColorStateList mBgDrawable;

    private int mColor = -1;

    /**
     * Draws a Triangle Cut in a Rectangle.
     */
    private TriangleCutDrawable() {
        super();
    }

    /**
     * Draws a Triangle Cut in a Rectangle.
     */
    public TriangleCutDrawable(int color) {

        this(color, null);
    }

    /**
     * Draws a Triangle Cut in a Rectangle.
     */
    public TriangleCutDrawable(int defColor, ColorStateList colorState) {

        super();

        p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);

        mColor = defColor;
        mBgDrawable = colorState;
    }

    @Override
    public void draw(Canvas canvas) {

        int color = Color.WHITE;
        if (mColor != -1 && mBgDrawable == null) {
            color = mColor;
        } else if (mBgDrawable != null && mColor != -1) {
            int[] state = getState();
            color = mBgDrawable.getColorForState(state, mColor);
        } else if (mBgDrawable != null) {
            int[] state = getState();
            color = mBgDrawable.getColorForState(state, Color.WHITE);
        }

        p.setColor(color);

        Path path = getTriangleHeader();
        canvas.drawPath(path, p);
    }

    @Override
    public void setAlpha(int i) {}

    @Override
    public void setColorFilter(ColorFilter colorFilter) {}

    @Override
    public int getOpacity() {

        return 0;
    }

    /**
     * Override if the Triangle Cut needs to be some place else.
     * By default, it will be near the top left.
     */
    protected Path getTriangleHeader() {

        Point startPoint, p2, p3, p4, p5, p6, p7;
        Rect bounds = getBounds();
        int width = bounds.right - bounds.left;
        int height = bounds.bottom - bounds.top;
        startPoint = new Point(bounds.left, bounds.top);
        p2 = new Point(bounds.left, bounds.bottom);
        p3 = new Point(bounds.right, bounds.bottom);
        p4 = new Point(bounds.right, bounds.top);
        double topRightTriangle = bounds.left + (width * TRIANGLE_LOCATION) + height;
        p5 = new Point( (int) topRightTriangle, bounds.top);
        double bottomTriangle = bounds.left + (width * TRIANGLE_LOCATION);
        p6 = new Point((int) bottomTriangle, bounds.bottom);
        double topLeftTriangle = bounds.left + (width * TRIANGLE_LOCATION) - height;
        p7 = new Point((int) topLeftTriangle, bounds.top);

        Path path = new Path();
        path.moveTo(startPoint.x, startPoint.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p4.x, p4.y);
        path.lineTo(p5.x, p5.y);
        path.lineTo(p6.x, p6.y);
        path.lineTo(p7.x, p7.y);

        return path;
    }

    @Override
    public boolean isStateful() {

        return true;
    }

    @Override
    protected boolean onStateChange(int[] state) {

        invalidateSelf();
        return true;
    }
}
