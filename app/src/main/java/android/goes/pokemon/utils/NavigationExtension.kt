package android.goes.pokemon.utils

import android.goes.pokemon.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

val Fragment.navController get() = findNavController()

fun NavController.navigateAndAnimate(
    navigationId: Int,
    bundle: Bundle? = null,
    slideDirection: NavigationSlideDirection = NavigationSlideDirection.LEFT
) {
    val options = NavOptions.Builder().apply {
        setEnterAnim(R.anim.nav_slide_in_right)
        setExitAnim(R.anim.nav_slide_out_left)
        setPopEnterAnim(R.anim.nav_slide_in_left)
        setPopExitAnim(R.anim.nav_slide_out_right)
    }.build()

    navigate(navigationId, bundle, options)
}

enum class NavigationSlideDirection {
    TOP, LEFT
}