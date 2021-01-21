package del.app.dnote.helper;

import android.widget.*;
import android.graphics.*;
import android.content.*;
import android.util.*;
import android.view.*;

public class CustomEditText extends EditText
{
	private Paint paint;
	private Rect rect;
	private Context context;

	public CustomEditText(Context c ,AttributeSet attr){
		super(c,attr);
		init();
	}
	public CustomEditText(Context c){
		super(c);
		init();
	}
	public void init(){
		this.context = context;
		LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		paint = new Paint();
		setLayoutParams(par);
		rect = new Rect();
		setGravity(Gravity.TOP);
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setTextSize(20);
		paint.setColor(Color.parseColor("#212121"));
		paint.setTypeface(Typeface.MONOSPACE);
		setTextSize(20);
		setTextColor(Color.BLACK);
		setBackgroundColor(Color.TRANSPARENT);
		rect.set(8,0,0,0);
	}
	@Override
    protected void onDraw(Canvas canvas) {
		int height = getHeight();
        int lHeight = getLineHeight();

        int count = height / lHeight;
        if (getLineCount() > count) {
            count = getLineCount();
        }

        int baseline = getLineBounds(0, rect);

        for (int i = 0; i < count; i++) {
            canvas.drawLine(rect.left, baseline + 1, rect.right, baseline + 1, paint);
            baseline += getLineHeight();
        }
        super.onDraw(canvas);
    }
}
