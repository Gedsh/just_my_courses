package pan.alexander.pictureoftheday.framework.ui.main

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import pan.alexander.pictureoftheday.R

private const val DEFAULT_RADIUS = 320
private const val DEFAULT_CENTER_ANGLE = -90
private const val DEFAULT_TEXT_COLOR = Color.WHITE

class ArcTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var radius = DEFAULT_RADIUS
    private var centerAngle = DEFAULT_CENTER_ANGLE
    private var customTextSize = resources.displayMetrics.density * 16

    private var customTextColor = DEFAULT_TEXT_COLOR
    private var fontFamily: Typeface? = null

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pathArc = Path()
    private val oval = RectF()

    private var offset = .0f

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcTextView)

        radius = typedArray.getDimensionPixelSize(R.styleable.ArcTextView_radius, radius)
        centerAngle = typedArray.getInteger(R.styleable.ArcTextView_centerAngle, centerAngle)
        customTextSize = typedArray.getDimensionPixelSize(
            R.styleable.ArcTextView_textSize, customTextSize.toInt()
        ).toFloat()

        customTextColor = typedArray.getColor(R.styleable.ArcTextView_textColor, DEFAULT_TEXT_COLOR)

        val fontRes = typedArray.getResourceId(R.styleable.ArcTextView_arcFontFamily, -1)
        fontFamily = fontRes.takeIf { it > 0 }?.let { ResourcesCompat.getFont(context, it) }

        typedArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        offset = customTextSize * 0.75f

        val lp = layoutParams
        lp.width = if (radius > 0) (radius * 2 + offset * 2).toInt() else 0
        lp.height = if (radius > 0) (radius + offset).toInt() else 0

        paintText.color = customTextColor
        paintText.typeface = fontFamily
        paintText.textSize = customTextSize
    }

    override fun onDraw(canvas: Canvas?) {
        val textWidth = paintText.measureText(text.toString())
        val circumference =
            (2 * Math.PI * radius).toFloat()

        val textAngle = textWidth * 360 / circumference

        val startAngle = centerAngle - textAngle / 2

        oval.set(offset, offset, radius * 2 + offset, radius * 2 + offset)

        pathArc.reset()
        pathArc.addArc(oval, startAngle, 350f)

        canvas?.drawTextOnPath(text.toString(), pathArc, 0f, 0f, paintText)
    }

}
