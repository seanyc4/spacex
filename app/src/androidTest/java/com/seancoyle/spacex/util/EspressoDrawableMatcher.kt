package com.seancoyle.spacex.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.VectorDrawable
import android.media.Image
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.content.ContextCompat
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


object EspressoDrawableMatcher {

    fun hasDrawable(): BoundedMatcher<View, ImageView>{
        return object: BoundedMatcher<View, ImageView>(ImageView::class.java){

            override fun describeTo(description: Description?) {
                description?.appendText("has drawable")
            }

            override fun matchesSafely(item: ImageView?): Boolean {
                return item?.getDrawable() != null;
            }

        }
    }

   /* fun withIcon(@DrawableRes resourceId: Int): Matcher<View> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has image drawable resource $resourceId")
            }

            public override fun matchesSafely(actionMenuItemView: ImageView): Boolean {
                return sameBitmap(actionMenuItemView.context, actionMenuItemView.drawable, resourceId)
            }
        }
    }

    private fun sameBitmap(context: Context, drawable: Drawable?, resourceId: Int): Boolean {
        var drawable = drawable
        var otherDrawable: Drawable? = ContextCompat.getDrawable(context, resourceId)
        if (drawable == null || otherDrawable == null) {
            return false
        }
        if (drawable is StateListDrawable && otherDrawable is StateListDrawable) {
            drawable = drawable.current
            otherDrawable = otherDrawable.current
        }

        val bitmap = if (drawable is VectorDrawable) vectorToBitmap(drawable)
        else (drawable as BitmapDrawable).bitmap
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
    }*/
}
