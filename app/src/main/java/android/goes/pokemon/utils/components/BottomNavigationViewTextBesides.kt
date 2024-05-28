package android.goes.pokemon.utils.components

import android.content.Context
import android.goes.pokemon.R
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.view.forEach
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class BottomNavigationViewTextBesides @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : BottomNavigationView(context, attrs) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupCustomItems()
    }

    private fun setupCustomItems() {
        val menuView = getChildAt(0) as BottomNavigationMenuView
        menuView.forEach { item ->
            val itemView = item as BottomNavigationItemView
            val customView = LayoutInflater.from(context).inflate(R.layout.item_bottom_nav_menu, menuView, false) as LinearLayout
            val iconView = itemView.findViewById<ImageView>(R.id.icon)
            val titleView = itemView.findViewById<TextView>(R.id.text)

            customView.findViewById<ShapeableImageView>(R.id.icon).setImageDrawable(iconView.drawable)
            customView.findViewById<MaterialTextView>(R.id.text).text = titleView.text

            itemView.removeAllViews()
            itemView.addView(customView)
        }
    }
}
