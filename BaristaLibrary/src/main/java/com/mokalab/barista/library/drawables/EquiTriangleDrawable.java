package com.mokalab.barista.library.drawables;

/*******************************************************************************
 * Copyright 2014 MokaLab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

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
 * This Drawable draws an equilateral triangle within the set bounds, i.e. setBounds().
 * The triangle will point in the specified direction.
 * The default direction is NORTH.
 */
public class EquiTriangleDrawable extends Drawable {

    private int mColor = Color.BLACK;
    private Paint p;
    private Direction mDirection = Direction.NORTH;
    private ColorStateList mBgDrawable;

    /**
     * Draws an Equilateral Triangle.
     */
    private EquiTriangleDrawable() {
        super();
    }

    /**
     * Draws an Equilateral Triangle.
     */
    public EquiTriangleDrawable(int color, Direction direction) {

        this(color, null, direction);
    }

    /**
     * Draws an Equilateral Triangle.
     */
    public EquiTriangleDrawable(int defColor, ColorStateList colorState, Direction direction) {

        super();

        p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);

        mColor = defColor;
        mBgDrawable = colorState;
        mDirection = direction;
    }

    /**
     * Use this to define the direction of the {@link com.mokalab.barista.library.drawables.EquiTriangleDrawable}.
     */
    public enum Direction {
        NORTH, SOUTH, EAST, WEST;
    }

    /* (non-Javadoc)
    * @see android.graphics.drawable.Drawable#draw(android.graphics.Canvas)
    */
    @Override
    public void draw(Canvas canvas) {

        int color = Color.BLACK;
        if (mColor != -1 && mBgDrawable == null) {
            color = mColor;
        } else if (mBgDrawable != null && mColor != -1) {
            int[] state = getState();
            color = mBgDrawable.getColorForState(state, mColor);
        } else if (mBgDrawable != null) {
            int[] state = getState();
            color = mBgDrawable.getColorForState(state, Color.BLACK);
        }

        p.setColor(color);

        Path path = getEquilateralTriangle();
        canvas.drawPath(path, p);
    }

    @Override
    public void setAlpha(int i) {}

    @Override
    public void setColorFilter(ColorFilter colorFilter) {}

    /* Returns zero
    * @see android.graphics.drawable.Drawable#getOpacity()
    */
    @Override
    public int getOpacity() {

        return 0;
    }

    private Path getEquilateralTriangle() {

        Point startPoint, p2, p3;
        Rect bounds = getBounds();
        int width = bounds.right - bounds.left;
        int height = bounds.bottom - bounds.top;
        switch (mDirection){
            case NORTH:
                startPoint = new Point(bounds.left, bounds.bottom);
                p2 = new Point(startPoint.x + width, startPoint.y);
                p3 = new Point(startPoint.x + (width / 2), startPoint.y - height);
                break;
            case SOUTH:
                startPoint = new Point(bounds.left, bounds.top);
                p2 = new Point(startPoint.x + width,startPoint.y);
                p3 = new Point(startPoint.x + (width / 2), startPoint.y + height);
                break;
            case EAST:
                startPoint = new Point(bounds.left, bounds.top);
                p2 = new Point(startPoint.x, startPoint.y + height);
                p3 = new Point(startPoint.x + width, startPoint.y + (height / 2));
                break;
            case WEST:
            default:
                startPoint = new Point(bounds.right, bounds.top);
                p2 = new Point(startPoint.x, startPoint.y + height);
                p3 = new Point(startPoint.x - width, startPoint.y + (height / 2));
                break;
        }

        Path path = new Path();
        path.moveTo(startPoint.x, startPoint.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);

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

    public void setColor(int color) {
        mColor = color;
    }

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }
}