package com.example.applisondage.CustomTransformation

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class CircleCropTransformation : BitmapTransformation() {
    // to crop profile picture in circle and to be have no blank, full filling image
    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val size = Math.min(toTransform.width, toTransform.height)
        val x = (toTransform.width - size) / 2
        val y = (toTransform.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(toTransform, x, y, size, size)

        val bitmap = pool.get(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            isAntiAlias = true
        }

        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)

        squaredBitmap.recycle()

        return bitmap
    }

    override fun equals(other: Any?): Boolean {
        return other is CircleCropTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    companion object {
        private const val VERSION = 1
        private const val ID = "com.example.applisondage.com.example.applisondage.CustomTransformation.CircleCropTransformation.$VERSION"
        private val ID_BYTES = ID.toByteArray()
    }
}