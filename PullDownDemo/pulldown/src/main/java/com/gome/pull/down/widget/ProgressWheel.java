package com.gome.pull.down.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class ProgressWheel extends View {
    Context mContext;
    private int layout_height = 0;
    private int layout_width = 0;
    private int reversBarLength = 200;
    private int reverseBarWidth = 10;
    private int barWidth = 20;
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;
    private int barColor = Color.BLACK;
    private Paint reverseBarPaint = new Paint();
    private Paint barPaint = new Paint();
    private int len1 = 18;
    private int len2 = 18;
    private int len3 = 15;
    private int len4 = 10;
    private RectF circleBounds = new RectF();
    private int spinSpeed = 4;
    private int delayMillis = 0;
    private Handler spinHandler = new Handler() {
        public void handleMessage(Message msg) {
            ProgressWheel.this.invalidate();
            ProgressWheel.this.progress += ProgressWheel.this.spinSpeed;
            if(ProgressWheel.this.progress > 360) {
                ProgressWheel.this.progress = 0;
            }

            ProgressWheel.this.progressReverse += ProgressWheel.this.spinSpeed * 2;
            if(ProgressWheel.this.progressReverse > 360) {
                ProgressWheel.this.progressReverse = 0;
            }

            ProgressWheel.this.spinHandler.sendEmptyMessageDelayed(0, (long)ProgressWheel.this.delayMillis);
        }
    };
    int progressReverse = 0;
    int progress = 0;
    int oneAngle = 90;
    int twoAngle = 105;
    int distance = 10;
    int mydistance = 10;
    private String text = "";
    private String[] splitText = new String[0];
    int fourCount;
    int threeCount;

    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.fourCount = this.len3 + this.len2 + this.distance;
        this.threeCount = this.len2 + this.distance;
        this.mContext = context;
        this.mydistance = this.dip2px(this.mContext, 7.0F);
       // this.parseAttributes(context.obtainStyledAttributes(attrs, styleable.ProgressWheel));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.layout_width = w;
        this.layout_height = h;
        this.setupBounds();
        this.setupPaints();
        this.invalidate();
    }

    private void setupPaints() {
        this.reverseBarPaint.setAntiAlias(true);
        this.reverseBarPaint.setStyle(Style.STROKE);
        this.reverseBarPaint.setStrokeWidth((float)this.barWidth);
        this.reverseBarPaint.setStrokeCap(Cap.ROUND);
        LinearGradient mShader = new LinearGradient(0.0F, 0.0F, 100.0F, 100.0F, new int[]{Color.parseColor("#d3d3d3"), Color.parseColor("#dddddd"), Color.parseColor("#cccccccc")}, (float[])null, TileMode.CLAMP);
        this.reverseBarPaint.setShader(mShader);
        this.barPaint.setColor(this.barColor);
        this.barPaint.setStyle(Style.STROKE);
        this.barPaint.setStrokeWidth((float)this.barWidth);
        this.barPaint.setStrokeCap(Cap.ROUND);
    }

    private void setupBounds() {
        int minValue = Math.min(this.layout_width, this.layout_height);
        int xOffset = this.layout_width - minValue;
        int yOffset = this.layout_height - minValue;
        this.paddingTop = this.getPaddingTop() + yOffset / 2;
        this.paddingBottom = this.getPaddingBottom() + yOffset / 2;
        this.paddingLeft = this.getPaddingLeft() + xOffset / 2;
        this.paddingRight = this.getPaddingRight() + xOffset / 2;
        this.circleBounds = new RectF((float)(this.paddingLeft + this.barWidth), (float)(this.paddingTop + this.barWidth), (float)(this.getLayoutParams().width - this.paddingRight - this.barWidth), (float)(this.getLayoutParams().height - this.paddingBottom - this.barWidth));
    }

    public int dip2px(Context context, float dipValue) {
        float density = this.screenDensity(context);
        return (int)(dipValue * density + 0.5F);
    }

    public float screenDensity(Context context) {
        float density = 0.0F;

        try {
            DisplayMetrics e = context.getResources().getDisplayMetrics();
            density = e.density;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return density;
    }

    private void radioparseAttributes(TypedArray a) {
//        this.barWidth = (int)a.getDimension(styleable.ProgressWheel_barWidth, (float));
//        this.barColor = a.getColor(styleable.ProgressWheel_barColor, this.barColor);
        this.mydistance = this.dip2px(this.mContext, 7.0F);
        a.recycle();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.progress > 70 && this.progress < 270) {
            canvas.drawArc(this.circleBounds, (float)this.progressReverse, (float)this.reversBarLength, false, this.reverseBarPaint);
        }

        canvas.drawArc(this.circleBounds, (float)(this.oneAngle - this.len1 - this.progress), (float)this.len1, false, this.barPaint);
        if(this.progress < 140) {
            this.spinSpeed = 4;
        } else {
            this.spinSpeed = 5;
        }

        if(this.progress <= 18) {
            this.len2 = 18;
            canvas.drawArc(this.circleBounds, (float)(this.oneAngle - this.progress), (float)this.progress, false, this.barPaint);
        } else if(this.progress > this.len1 && this.progress < 280) {
            if(this.threeCount > 0 && this.len3 > 0) {
                canvas.drawArc(this.circleBounds, (float)(this.twoAngle - this.len1 - this.progress), (float)this.len1, false, this.barPaint);
            } else {
                if(this.len2 > 0) {
                    canvas.drawArc(this.circleBounds, (float)(this.twoAngle - this.len1 - this.progress), (float)this.len2, false, this.barPaint);
                }

                if(this.fourCount <= 0 || this.len4 <= 0) {
                    if(this.len2 > 0) {
                        this.len2 -= 2;
                    }

                    if(this.len1 > 10) {
                        --this.len1;
                    }

                    if(this.len1 < 10) {
                        this.len1 = 10;
                    }
                }
            }
        } else if(this.progress > 280 && this.progress <= 310) {
            if(this.len2 < this.len1) {
                ++this.len2;
                if(this.len2 > this.len1) {
                    this.len2 = this.len1;
                }
            }

            if(this.len2 > 0) {
                canvas.drawArc(this.circleBounds, (float)(this.twoAngle - this.len1 - this.progress), (float)this.len2, false, this.barPaint);
            }
        } else if(this.progress > 310) {
            if(this.len2 > 0) {
                --this.len2;
            }

            if(this.len2 > 0) {
                canvas.drawArc(this.circleBounds, (float)(this.twoAngle - this.len1 - this.progress), (float)this.len2, false, this.barPaint);
            }
        }

        int len;
        if(this.progress <= 18) {
            this.distance = this.mydistance;
            this.len3 = 15;
            this.threeCount = this.len2 + this.distance;
        } else if(this.progress > this.len2 && this.progress <= this.len2 + this.len3) {
            len = this.progress - this.len2;
            if(len > this.len3) {
                len = this.len3;
            }

            canvas.drawArc(this.circleBounds, (float)(this.twoAngle - this.progress), (float)len, false, this.barPaint);
        } else if(this.progress > this.len2 + this.len3 && this.progress <= this.len2 + this.len3 + this.distance) {
            canvas.drawArc(this.circleBounds, (float)(this.twoAngle - this.len2 - this.len3), (float)this.len1, false, this.barPaint);
        } else if(this.progress > this.len2 + this.len3 + this.distance && this.progress <= 90) {
            if(this.distance < 12) {
                ++this.distance;
                this.threeCount = this.len2 + this.distance;
            }

            canvas.drawArc(this.circleBounds, (float)(this.twoAngle + this.distance - this.progress), (float)this.len3, false, this.barPaint);
        } else if(this.progress > 90 && this.progress <= 140) {
            if(this.distance > 0) {
                --this.distance;
                this.threeCount = this.len2 + this.distance;
            }

            canvas.drawArc(this.circleBounds, (float)(this.twoAngle + this.distance - this.progress), (float)this.len3, false, this.barPaint);
        } else if(this.progress > 140) {
            this.threeCount = this.len2 + this.distance;
            if(this.threeCount > 0 && this.len3 > 0) {
                canvas.drawArc(this.circleBounds, (float)(this.twoAngle + this.threeCount - this.len1 - this.len2 - this.progress), (float)this.len3, false, this.barPaint);
                --this.threeCount;
                --this.len3;
            }
        }

        if(this.progress < this.len2 + this.len3 + this.distance) {
            this.len4 = 10;
            this.fourCount = this.len3 + this.len2 + this.distance;
        } else if(this.progress > this.len2 + this.len3 + this.distance + this.distance && this.progress <= 90) {
            len = this.progress - (this.len2 + this.len3 + this.distance + this.distance);
            if(len > this.len4) {
                len = this.len4;
            }

            this.fourCount = this.len3 + this.len2 + this.distance;
            canvas.drawArc(this.circleBounds, (float)(this.twoAngle + this.len3 + this.distance + this.distance - this.progress), (float)len, false, this.barPaint);
        } else if(this.progress > 90 && this.progress <= 140) {
            this.fourCount = this.len3 + this.len2 + this.distance;
            canvas.drawArc(this.circleBounds, (float)(this.twoAngle + this.len3 + this.distance + this.distance - this.progress), (float)this.len4, false, this.barPaint);
        } else if(this.progress > 140 && this.fourCount > 0 && this.len4 > 0) {
            canvas.drawArc(this.circleBounds, (float)(this.twoAngle + this.fourCount - this.len2 - 5 - this.progress), (float)this.len4, false, this.barPaint);
            --this.fourCount;
            --this.len4;
        }

        this.progress += this.spinSpeed;
        if(this.progress > 360) {
            this.progress = 0;
        }

        this.progressReverse += this.spinSpeed * 2;
        if(this.progressReverse > 360) {
            this.progressReverse = 0;
        }

        this.invalidate();
    }

    public void spin() {
        this.spinHandler.sendEmptyMessage(0);
    }

    public void stopSpinning() {
        this.spinHandler.removeMessages(0);
    }
}

