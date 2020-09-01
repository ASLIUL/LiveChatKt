package com.yb.livechatkt.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.yb.livechatkt.R
import kotlin.math.min

class RoundImageView(context: Context, attributeSet: AttributeSet?) : AppCompatImageView(context,attributeSet) {
    constructor(context: Context):this(context,null)

     //是否为圆形
    private var isCircle:Boolean = false
    //border inner——border是否覆盖图片
    private var isCoverSrc:Boolean = false
    //边框宽度
    private var borderWidth:Int = 0
    //边框颜色
    private var borderColor:Int = Color.WHITE
    //内边框宽度
    private var innerBorderWidth:Int = 0
    //内边框颜色
    private var innerBorderColor:Int = Color.WHITE

    //统一设置圆角半径，优先级高于单独设置的圆角半径
    private var cornerRadius:Int = 0
    //左上角圆角半径
    private var cornerTopLeftRadius:Int = 0
    //右上角圆角半径
    private var cornerTopRightRadius:Int = 0
    //右下角圆角半径
    private var cornerBottomRightRadius:Int = 0
    //左下
    private var cornerBottomLeftRadius:Int = 0

    //遮罩颜色
    private var maskColor:Int = Color.GRAY

    private var xfermode:Xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

    private var imageWidth:Int = 0
    private var imageHeight:Int = 0
    private var radius:Float = 0f

    private var borderRadiusArray:FloatArray = FloatArray(8)
    private var srcRadiusArray:FloatArray = FloatArray(8)

    //圆形占的矩形区域
    private var srcRectF:RectF = RectF()
    //边框的矩形区域
    private var borderRectF:RectF = RectF()

    private var paint:Paint = Paint()
    private var path:Path = Path() // 用于裁剪图片的path
    private var srcPath:Path = Path()//图片区域的path

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet,
            R.styleable.NiceImageView,0,0)
        isCoverSrc = typedArray.getBoolean(R.styleable.NiceImageView_is_cover_src,false)
        isCircle = typedArray.getBoolean(R.styleable.NiceImageView_is_circle,false)
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.NiceImageView_border_width,0)
        borderColor = typedArray.getColor(R.styleable.NiceImageView_border_color,Color.WHITE)
        innerBorderWidth = typedArray.getDimensionPixelSize(R.styleable.NiceImageView_inner_border_width,0)
        innerBorderColor = typedArray.getColor(R.styleable.NiceImageView_inner_border_color,Color.WHITE)
        cornerRadius = typedArray.getDimensionPixelSize(R.styleable.NiceImageView_corner_radius,0)
        cornerTopLeftRadius = typedArray.getDimensionPixelSize(R.styleable.NiceImageView_corner_top_left_radius,0)
        cornerTopRightRadius = typedArray.getDimensionPixelSize(R.styleable.NiceImageView_corner_top_right_radius,0)
        cornerBottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.NiceImageView_corner_bottom_left_radius,0)
        cornerBottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.NiceImageView_corner_bottom_right_radius,0)
        maskColor = typedArray.getColor(R.styleable.NiceImageView_mask_color,Color.GRAY)

        typedArray.recycle()

        calcuateRadii()
        //clearInnerBorderWidth()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageWidth = w
        imageHeight = h

        //initBorderRectF()
        initSrcRectF()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.saveLayer(srcRectF,null,Canvas.ALL_SAVE_FLAG)
        if (isCoverSrc){
            val sx = 1.0f * (imageWidth - 2 * borderWidth -2 * innerBorderWidth) / imageWidth
            val sy = 1.0f * (imageHeight - 2 * borderWidth -2 * innerBorderWidth) / imageHeight
            canvas?.scale(sx,sy,imageWidth / 2.0f ,imageHeight / 2.0f)
        }
        super.onDraw(canvas)
        paint.reset()
        path.reset()
        if (isCircle){
            path.addCircle(imageWidth / 2.0f,imageHeight / 2.0f , radius,Path.Direction.CCW)
        }else{
            path.addRoundRect(srcRectF,srcRadiusArray,Path.Direction.CCW)
        }
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.xfermode = xfermode
        srcPath.addRect(srcRectF,Path.Direction.CCW)
        canvas?.drawPath(srcPath,paint)
        paint.xfermode = null

        if (maskColor != 0){
            paint.color = maskColor
            canvas?.drawPath(path,paint)
        }
        canvas?.restore()
        drawBorders(canvas)
    }

    private fun drawBorders(canvas: Canvas?){
        if (isCircle){
            if (borderWidth > 0){
                drawCircleBorder(canvas,borderWidth,borderColor,radius - borderWidth / 2.0f)
            }
            if (innerBorderWidth > 0){
                drawCircleBorder(canvas,innerBorderWidth,innerBorderColor,radius - borderWidth - innerBorderWidth /2.0f)
            }
        }else{
            if (borderWidth > 0){
                drawRectFBorder(canvas,borderWidth,borderColor,borderRectF,borderRadiusArray)
            }
        }
    }

    private fun drawCircleBorder(canvas: Canvas?,borderWidth:Int,borderColor:Int,radius:Float){
        initBorderPaint(borderWidth,borderColor)
        path.addCircle(imageWidth / 2.0f,imageHeight / 2.0f,radius,Path.Direction.CCW)
        canvas?.drawPath(path,paint)
    }

    private fun drawRectFBorder(canvas: Canvas?,borderWidth: Int,borderColor: Int,rectF: RectF,radiusArray: FloatArray){
        initBorderPaint(borderWidth,borderColor)
        path.addRoundRect(rectF,radiusArray,Path.Direction.CCW)
        canvas?.drawPath(path, paint)
    }

    private fun initBorderPaint(borderWidth: Int,borderColor: Int){
        path.reset()
        paint.strokeWidth = borderWidth.toFloat()
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
    }

    private fun initSrcRectF(){
        if (isCircle){
            radius = min(imageWidth,imageHeight) / 2.0f
            srcRectF.set(imageWidth / 2.0f - radius,imageHeight /2.0f - radius,imageWidth / 2.0f + radius,imageHeight / 2.0f +radius)
        }else{
            srcRectF.set(0f,0f,imageWidth.toFloat(),imageHeight.toFloat())
            if (isCoverSrc){
                srcRectF = borderRectF
            }
        }
    }

    private fun calcuateRadii(){
        if (isCircle) return
        if (cornerRadius > 0){
            var i:Int = 0
            borderRadiusArray.forEach {
                cornerRadius = it.toInt()
                srcRadiusArray[i] = cornerRadius - borderWidth / 2.0f
                i++
            }
        }
    }



}





















