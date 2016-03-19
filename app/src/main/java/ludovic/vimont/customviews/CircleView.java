package ludovic.vimont.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleView extends ImageView {
    private int viewWidth, viewHeight;
    private boolean hasABorder = false;
    private int borderWidth = 0, borderColor = 0XDDD1D1D1;

    private Bitmap image;
    private Paint paint, paintBorder;

    public CircleView(Context context) {
        super(context);
        setup();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        paint = new Paint();
        paint.setAntiAlias(true);

        if(hasABorder) {
            paintBorder = new Paint();
            setBorderColor(borderColor);
            paintBorder.setAntiAlias(true);
            this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
            paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
        }
    }

    public void setBorderColor(int borderColor) {
        if (paintBorder != null) {
            paintBorder.setColor(borderColor);
        }
        this.invalidate();
    }

    private void loadBitmap() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        image = (bitmapDrawable != null) ? bitmapDrawable.getBitmap() : null;
    }


    @Override
    public void onDraw(final Canvas canvas) {
        loadBitmap();

        if (image != null) {
            final int circleCenter = viewWidth / 2;
            final int radius = circleCenter - 25;

            paint.setShader(computeBitmapShader());
            paint.setColor(0x80000000);
            if(hasABorder) {
                canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter + borderWidth - 4.0f, paintBorder);
            }
            canvas.drawCircle(circleCenter, circleCenter, radius, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    private BitmapShader computeBitmapShader() {
        BitmapShader shader = new BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = getImageMatrix();
        RectF drawableRect = new RectF(0, 0, image.getWidth(), image.getHeight());
        RectF viewRect = new RectF(0, 0, getWidth(), getHeight());
        matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
        shader.setLocalMatrix(matrix);
        return shader;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

        viewWidth  =  width  - (borderWidth * 2);
        viewHeight =  height - (borderWidth * 2);

        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        result = (specMode == MeasureSpec.EXACTLY) ? specSize : viewWidth;
        return result;
    }

    private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        result = (specMode == MeasureSpec.EXACTLY) ? specSize : viewHeight;
        return result;
    }
}
