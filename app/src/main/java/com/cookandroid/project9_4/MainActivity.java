package com.cookandroid.project9_4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    final static int LINE = 1, CIRCLE = 2, RECT = 3;
    static int curShape = LINE;


    static List<Shape> shapeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new MyGraphicView(this));
        setTitle("Project9_4 (도형 리스트 관리)");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "선 추가");
        menu.add(0, 2, 0, "원 추가");
        menu.add(0, 3, 0, "사각형 추가");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: curShape = LINE; return true;
            case 2: curShape = CIRCLE; return true;
            case 3: curShape = RECT; return true;
        }
        return super.onOptionsItemSelected(item);
    }


    abstract static class Shape {
        int startX, startY, stopX, stopY;
        public abstract void draw(Canvas canvas, Paint paint);
    }

    static class Line extends Shape {
        Line(int sx, int sy, int ex, int ey) { startX = sx; startY = sy; stopX = ex; stopY = ey; }
        @Override public void draw(Canvas canvas, Paint paint) {
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    static class Circle extends Shape {
        Circle(int sx, int sy, int ex, int ey) { startX = sx; startY = sy; stopX = ex; stopY = ey; }
        @Override public void draw(Canvas canvas, Paint paint) {
            int radius = (int) Math.sqrt(Math.pow(stopX - startX, 2) + Math.pow(stopY - startY, 2));
            canvas.drawCircle(startX, startY, radius, paint);
        }
    }

    static class Rect extends Shape {
        Rect(int sx, int sy, int ex, int ey) { startX = sx; startY = sy; stopX = ex; stopY = ey; }
        @Override public void draw(Canvas canvas, Paint paint) {

            canvas.drawRect(Math.min(startX, stopX), Math.min(startY, stopY),
                    Math.max(startX, stopX), Math.max(startY, stopY), paint);
        }
    }


    private static class MyGraphicView extends View {
        int startX = -1, startY = -1, stopX = -1, stopY = -1;

        public MyGraphicView(Context context) { super(context); }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getX(); startY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    stopX = (int) event.getX(); stopY = (int) event.getY();
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    stopX = (int) event.getX(); stopY = (int) event.getY();

                    Shape shape = null;
                    if (curShape == LINE) shape = new Line(startX, startY, stopX, stopY);
                    else if (curShape == CIRCLE) shape = new Circle(startX, startY, stopX, stopY);
                    else if (curShape == RECT) shape = new Rect(startX, startY, stopX, stopY);

                    if (shape != null) shapeList.add(shape);
                    invalidate();
                    break;
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);


            for (Shape s : shapeList) {
                s.draw(canvas, paint);
            }


            if (startX != -1) {
                if (curShape == LINE) canvas.drawLine(startX, startY, stopX, stopY, paint);
                else if (curShape == CIRCLE) {
                    int r = (int) Math.sqrt(Math.pow(stopX - startX, 2) + Math.pow(stopY - startY, 2));
                    canvas.drawCircle(startX, startY, r, paint);
                }
                else if (curShape == RECT) {
                    canvas.drawRect(Math.min(startX, stopX), Math.min(startY, stopY),
                            Math.max(startX, stopX), Math.max(startY, stopY), paint);
                }
            }
        }
    }
}