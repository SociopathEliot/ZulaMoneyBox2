package sl.kacinz.onluanmer.presentation.ui.fragments.customdesign

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class LoadingProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val segmentCount = 10
    private val segmentWidth = 50f
    private val segmentHeight = 24f
    private val radiusRatio = 0.42f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        isDither = true
        isAntiAlias = true
    }

    private val alphas = FloatArray(segmentCount) { 0f }
    private val animators = mutableListOf<ValueAnimator>()

    private val brightColor = Color.parseColor("#FFF87C")  // ближе к светлому жёлтому
    private val darkColor = Color.parseColor("#7B5F0F")    // мягкий тёмный золотистый

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAlphaAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animators.forEach { it.cancel() }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = min(width, height)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = size * radiusRatio

        val angleBetween = 360f / segmentCount

        for (i in 0 until segmentCount) {
            val angleDeg = angleBetween * i - 90f
            val angleRad = Math.toRadians(angleDeg.toDouble())

            val cx = centerX + radius * cos(angleRad).toFloat()
            val cy = centerY + radius * sin(angleRad).toFloat()

            val left = cx - segmentWidth / 2
            val top = cy - segmentHeight / 2
            val right = cx + segmentWidth / 2
            val bottom = cy + segmentHeight / 2

            canvas.save()
            canvas.rotate(angleDeg + 90f, cx, cy)

            // Цвет с альфа-анимацией
            val blendedColor = blendColors(darkColor, brightColor, alphas[i])
            paint.color = blendedColor

            // Рисуем скруглённую таблетку
            canvas.drawRoundRect(RectF(left, top, right, bottom), segmentHeight, segmentHeight, paint)

            canvas.restore()
        }
    }

    private fun startAlphaAnimation() {
        for (i in 0 until segmentCount) {
            val animator = ValueAnimator.ofFloat(0.2f, 1f, 0.2f).apply {
                duration = 1200
                startDelay = i * 100L
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener {
                    alphas[i] = it.animatedValue as Float
                    invalidate()
                }
            }
            animator.start()
            animators.add(animator)
        }
    }

    private fun blendColors(from: Int, to: Int, ratio: Float): Int {
        val inverse = 1f - ratio
        val r = (Color.red(from) * inverse + Color.red(to) * ratio).toInt()
        val g = (Color.green(from) * inverse + Color.green(to) * ratio).toInt()
        val b = (Color.blue(from) * inverse + Color.blue(to) * ratio).toInt()
        return Color.rgb(r, g, b)
    }
}
