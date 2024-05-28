package android.goes.pokemon.utils

import android.annotation.SuppressLint
import android.goes.pokemon.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import kotlin.math.round

fun View?.snack(
    message: CharSequence,
    actionText: CharSequence? = null,
    displayLength: Int = Snackbar.LENGTH_SHORT,
    @ColorInt backgroundColor: Int = Color.BLACK,
    @ColorInt textColor: Int = Color.WHITE,
    isAnchored: Boolean = false,
    anchoredView: View? = this,
    action: ((View) -> Unit)? = null
) {
    this?.let {
        Snackbar.make(it, message, displayLength).apply {
            this.setBackgroundTint(backgroundColor)

            with(view) {
                if (isAnchored) {
                    anchorView = anchoredView ?: this
                }

                findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
                    gravity = Gravity.CENTER
                    maxLines = 2
                    ellipsize = TextUtils.TruncateAt.END

                    this.setTextColor(
                        if (textColor != Color.WHITE) textColor
                        else Color.WHITE
                    )
                }

                if (!actionText.isNullOrEmpty()) {
                    findViewById<TextView>(com.google.android.material.R.id.snackbar_action)

                    setAction(actionText) {
                        action?.invoke(this)
                    }
                }
            }
        }.show()
    }
}

@SuppressLint("CheckResult")
fun ImageView.loadImage(
    source: Any?,
    corner: ImageCornerOptions? = ImageCornerOptions.NORMAL,
    radius: Int? = null,
    overrideSize: Int? = null,
    placeHolder: Drawable? = ColorDrawable(Color.LTGRAY),
    scaleType: ImageView.ScaleType? = null,
    skipMemory: Boolean = false,
    onLoadFailed: () -> Unit = {},
    onResourceReady: () -> Unit = {},
) {
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

    when (corner) {
        ImageCornerOptions.NORMAL -> {
            requestOptions.transform(CenterCrop())
        }
        ImageCornerOptions.CIRCLE -> {
            val circleRadius = resources.getDimensionPixelSize(R.dimen._72dp)
            requestOptions.transform(CenterCrop(), RoundedCorners(circleRadius))
        }
        ImageCornerOptions.ROUNDED -> {
            val cornerRadius = radius?.let {
                round(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        it.toFloat(),
                        resources.displayMetrics
                    )
                ).toInt()
            } ?: resources.getDimensionPixelSize(R.dimen._12dp)
            requestOptions.transform(CenterCrop(), RoundedCorners(cornerRadius))
        }

        null -> requestOptions.centerInside()
    }

    source?.let { imageSource ->
        when (scaleType) {
            ImageView.ScaleType.FIT_CENTER -> requestOptions.fitCenter()
            ImageView.ScaleType.CENTER_INSIDE -> requestOptions.centerInside()
            else -> Unit
        }

        overrideSize?.let { requestOptions.override(it) }

        requestOptions.skipMemoryCache(skipMemory)

        if (skipMemory) {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
        }

        requestOptions.placeholder(placeHolder).error(placeHolder)

        Glide.with(context)
            .load(imageSource)
            .apply(requestOptions)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadFailed.invoke()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    onResourceReady.invoke()
                    return false
                }

            })
            .into(this)
    }
}

// Image Shape Options
enum class ImageCornerOptions {
    NORMAL, CIRCLE, ROUNDED
}

fun ChipGroup.addChip(
    chipId: Int,
    chipText: String?,
    chipTypeFace: Int = R.font.montserrat,
    chipIcon: Drawable? = null,
    chipAttributes: (Chip.() -> Unit)? = null,
    clickAction: (View) -> Unit = {},
    checkedAction: (view: View, isChecked: Boolean) -> Unit = { _, _ -> }
) {
    val chip = Chip(context).apply {
        text = chipText
        typeface = ResourcesCompat.getFont(context, chipTypeFace)
        id = chipId

        if (chipIcon != null) {
            setChipIcon(chipIcon)
        }

        if (chipAttributes != null) {
            chipAttributes()
        }

        isClickable = true
        isFocusable = true

        setOnClickListener(clickAction)
        setOnCheckedChangeListener(checkedAction)
    }

    addView(chip)
}