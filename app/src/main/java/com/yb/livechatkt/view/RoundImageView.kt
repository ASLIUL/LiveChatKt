package com.yb.livechatkt.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.yb.livechatkt.R

class RoundImageView(context: Context, attributeSet: AttributeSet) : AppCompatImageView(
    context,
    attributeSet,
    0
) {


    private var SCALE_TYPE = ScaleType.CENTER_CROP
    private var BITMAP_CONFIG = Bitmap.Config.ARGB_8888
    private var COLORDRAWABLE_DIMENSION = 2
    private var DEFAULT_BORDER_WIDTH = 0
    private var DEFAULT_BORDER_COLOR = Color.BLACK
    private var mDrawableRect = RectF()
    private var mBorderRect = RectF()

    private var mShaderMatrix = Matrix()
    private var mBitmapPaint = Paint()
    private var mBorderPaint = Paint()

    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH

    private var mBitmap: Bitmap? = null
    private var mBitmapShader: BitmapShader? = null
    private var mBitmapWidth = 0
    private var mBitmapHeight = 0

    private var mDrawableRadius = 0f
    private var mBorderRadius = 0f

    private var mColorFilter: ColorFilter? = null

    private var mReady = false
    private var mSetupPending = false


    init {
        val a = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.RoundImageView, 0, 0
        )

        mBorderWidth = a.getDimensionPixelSize(
            R.styleable.RoundImageView_border_width_round,
            DEFAULT_BORDER_WIDTH
        )
        mBorderColor = a.getColor(
            R.styleable.RoundImageView_border_color_round,
            DEFAULT_BORDER_COLOR
        )

        a.recycle()
        initView()
    }


    override fun getScaleType(): ScaleType {
        return SCALE_TYPE
    }

    override fun setScaleType(scaleType: ScaleType?) {
        require(scaleType == SCALE_TYPE) { "请设置图片的ScaleType为$SCALE_TYPE" }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        require(!adjustViewBounds) { "adjustViewBounds not supported." }
    }

    override fun onDraw(canvas: Canvas?) {
        if (drawable == null) {
            return
        }

        canvas!!.drawCircle(
            width / 2.toFloat(), height / 2.toFloat(), mDrawableRadius,
            mBitmapPaint
        )
        if (mBorderWidth != 0) {
            canvas.drawCircle(
                width / 2.toFloat(), height / 2.toFloat(), mBorderRadius,
                mBorderPaint
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    fun getBorderColor(): Int {
        return mBorderColor
    }

    fun setBorderColor(borderColor: Int) {
        if (borderColor == mBorderColor) {
            return
        }
        mBorderColor = borderColor
        mBorderPaint.color = mBorderColor
        invalidate()
    }

    fun getBorderWidth(): Int {
        return mBorderWidth
    }

    fun setBorderWidth(borderWidth: Int) {
        if (borderWidth == mBorderWidth) {
            return
        }
        mBorderWidth = borderWidth
        setup()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        mBitmap = bm
        setup()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setColorFilter(cf: ColorFilter?) {
        if (cf === mColorFilter) {
            return
        }

        mColorFilter = cf
        mBitmapPaint.colorFilter = mColorFilter
        invalidate()
    }

    private fun initView() {

        scaleType = SCALE_TYPE
        mReady = true
        if (mSetupPending) {
            setup()
            mSetupPending = false
        }
    }

    private fun setup() {
        if (!mReady) {
            mSetupPending = true
            return
        }
        if (mBitmap == null) {
            return
        }
        mBitmapShader = BitmapShader(
            mBitmap!!, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        mBitmapPaint.isAntiAlias = true
        mBitmapPaint.shader = mBitmapShader
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = mBorderColor
        mBorderPaint.strokeWidth = mBorderWidth.toFloat()
        mBitmapHeight = mBitmap!!.height
        mBitmapWidth = mBitmap!!.width
        mBorderRect[0f, 0f, width.toFloat()] = height.toFloat()
        mBorderRadius = Math.min(
            (mBorderRect.height() - mBorderWidth) / 2,
            (mBorderRect.width() - mBorderWidth) / 2
        )
        mDrawableRect[mBorderWidth.toFloat(), mBorderWidth.toFloat(), mBorderRect.width()
                - mBorderWidth] = mBorderRect.height() - mBorderWidth
        mDrawableRadius = Math.min(
            mDrawableRect.height() / 2,
            mDrawableRect.width() / 2
        )
        updateShaderMatrix()
        invalidate()
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f
        mShaderMatrix.set(null)
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
            * mBitmapHeight
        ) {
            scale = mDrawableRect.height() / mBitmapHeight.toFloat()
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmapWidth.toFloat()
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
        }
        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(
            (dx + 0.5f).toInt() + mBorderWidth.toFloat(),
            (dy + 0.5f).toInt() + mBorderWidth.toFloat()
        )
        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else try {
            val bitmap: Bitmap
            bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(
                    COLORDRAWABLE_DIMENSION,
                    COLORDRAWABLE_DIMENSION,
                    BITMAP_CONFIG
                )
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight, BITMAP_CONFIG
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: OutOfMemoryError) {
            null
        }
    }

}