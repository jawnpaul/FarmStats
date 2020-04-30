package ng.com.knowit.farmstats.utility

import android.view.View
import android.view.ViewGroup
import java.util.*

object Utils {
    fun <T : View> findViewsWithType(
        root: View,
        type: Class<T>
    ): List<T> {
        val views: MutableList<T> = ArrayList()
        findViewsWithType(root, type, views)
        return views
    }

    private fun <T : View> findViewsWithType(
        view: View,
        type: Class<T>,
        views: MutableList<T>
    ) {
        if (type.isInstance(view)) {
            type.cast(view)?.let { views.add(it) }
        }
        if (view is ViewGroup) {
            val viewGroup = view
            for (i in 0 until viewGroup.childCount) {
                findViewsWithType<T>(viewGroup.getChildAt(i), type, views)
            }
        }
    }
}