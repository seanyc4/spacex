package com.seancoyle.spacex.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


/**
 *  https://gist.github.com/reza-id/d7a5f0f93bdd8c445cfbacf915daa978
 *  https://github.com/dannyroa/espresso-samples/blob/master/RecyclerView/app/src/androidTest/java/com/dannyroa/espresso_samples/recyclerview/RecyclerViewMatcher.java
 *  Edited slightly for my use case
 */


fun matchDrawable(@DrawableRes resourceId: Int): Matcher<View> {
    return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has image drawable resource $resourceId")
        }

        public override fun matchesSafely(imageView: ImageView): Boolean {
            return sameBitmap(imageView.context, imageView.drawable, resourceId)
        }
    }
}

private fun sameBitmap(context: Context, drawable: Drawable?, resourceId: Int): Boolean {
    var firstDrawable = drawable
    var otherDrawable: Drawable? = ContextCompat.getDrawable(context, resourceId)
    if (firstDrawable == null || otherDrawable == null) {
        return false
    }
    if (firstDrawable is StateListDrawable && otherDrawable is StateListDrawable) {
        firstDrawable = firstDrawable.current
        otherDrawable = otherDrawable.current
    }

    val bitmap = if (firstDrawable is VectorDrawable) vectorToBitmap(firstDrawable)
    else (firstDrawable as BitmapDrawable).bitmap
    val otherBitmap = if (otherDrawable is VectorDrawable) vectorToBitmap(otherDrawable)
    else (otherDrawable as BitmapDrawable).bitmap
    return bitmap.sameAs(otherBitmap)
}

private fun vectorToBitmap(vectorDrawable: VectorDrawable): Bitmap {
    val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return bitmap
}


fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
    return RecyclerViewMatcher(recyclerViewId)
}

class RecyclerViewMatcher(val recyclerViewId: Int) {
    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null
            override fun describeTo(description: Description?) {
                val id = if (targetViewId == -1) recyclerViewId else targetViewId
                var idDescription = id.toString()
                if (resources != null) {
                    idDescription = try {
                        resources!!.getResourceName(id)
                    } catch (var4: Resources.NotFoundException) {
                        String.format("%s (resource name not found)", id)
                    }
                }
                description!!.appendText("with id: $idDescription")
            }

            override fun matchesSafely(view: View?): Boolean {
                resources = view?.resources
                if (childView == null) {
                    val recyclerView = view?.rootView?.findViewById(recyclerViewId) as RecyclerView
                    childView = if (recyclerView != null) {
                        recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                    } else {
                        return false
                    }
                }
                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView: View? = childView?.findViewById(targetViewId)
                    view === targetView
                }
            }
        }
    }
}
