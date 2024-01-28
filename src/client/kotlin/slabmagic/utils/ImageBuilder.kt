package slabmagic.utils

import net.minecraft.util.math.Vec3f
import java.awt.image.BufferedImage
import java.awt.image.WritableRaster
import kotlin.math.abs
import kotlin.math.min

class ImageBuilder {

    private var image: BufferedImage? = null

    private fun target(toPaintOn: BufferedImage?=null): BufferedImage {
        return image
            ?: if (toPaintOn == null) throw Exception("No Root Image")
            else {
                val nimage = BufferedImage(toPaintOn.width, toPaintOn.height, BufferedImage.TYPE_INT_ARGB)
                image = nimage
                nimage
            }
    }

    fun paint(toPaintOn: BufferedImage, x: Int=0, y: Int=0): ImageBuilder{
        val image= target(toPaintOn)
        val graphics=image.createGraphics()
        graphics.drawImage(toPaintOn,x,y,null)
        graphics.dispose()
        return this
    }

    fun apply(selector: Selector, transformer: Transformer): ImageBuilder{
        val image= target()
        val raster= image.raster
        for(x in 0 until image.width){
            for(y in 0 until image.height){
                if(selector.test(raster,x,y)) transformer.apply(raster,x,y)
            }
        }
        return this
    }

    fun result(): BufferedImage = image ?: throw Exception("No Root Image")


    /* SELECTORS AND TRANSFORMERS */
    fun interface Selector{
        fun test(raster: WritableRaster, x: Int, y: Int): Boolean
    }

    fun interface Transformer{
        fun apply(raster: WritableRaster, x: Int, y: Int)
    }

}

/* SELECTORS */
val sAll = ImageBuilder.Selector{ _, _, _ -> true }

val sOpaque = ImageBuilder.Selector{ raster, x, y ->
    val pixel= raster.getPixel(x,y,IntArray(4))
    pixel[3]==255
}

val sVisible = ImageBuilder.Selector{ raster, x, y ->
    val pixel= raster.getPixel(x,y,IntArray(4))
    pixel[3]>0
}

val sOutline = ImageBuilder.Selector{ raster, x, y ->
    fun isVisible(x: Int, y: Int): Boolean{
        if(x<0 || y<0 || x>=raster.width || y>=raster.height) return false
        val pixel= raster.getPixel(x,y,IntArray(4))
        return pixel[3]==0
    }
    !isVisible(x,y) && (isVisible(x+1,y) || isVisible(x-1,y) || isVisible(x,y+1) || isVisible(x,y-1))
}

val sInnerOutline = ImageBuilder.Selector{ raster, x, y ->
    fun isVisible(x: Int, y: Int): Boolean{
        if(x<0 || y<0 || x>=raster.width || y>=raster.height) return false
        val pixel= raster.getPixel(x,y,IntArray(4))
        return pixel[3]==0
    }
    isVisible(x,y) && (!isVisible(x+1,y) || !isVisible(x-1,y) || !isVisible(x,y+1) || !isVisible(x,y-1))
}

fun sCombine(vararg selectors: ImageBuilder.Selector) = ImageBuilder.Selector{ raster, x, y ->
    for(selector in selectors){
        if(!selector.test(raster,x,y)) return@Selector false
    }
    true
}

fun sColor(color: Vec3f, tolerance: Float=0.1f) = ImageBuilder.Selector{ raster, x, y ->
    val pixel= raster.getPixel(x,y,IntArray(4))
    val distance= abs(color.x*255-pixel[0]) + abs(color.y*255-pixel[1]) + abs(color.z*255-pixel[2])
    distance<tolerance
}

fun sTint(tint: Vec3f, tolerance: Float=0.1f) = ImageBuilder.Selector{ raster, x, y ->
    val pixel= raster.getPixel(x,y,IntArray(4))
    val targetTint= Vec3f(pixel[0]/255f,pixel[1]/255f,pixel[2]/255f)
    targetTint.apply { normalize() ; subtract(tint) }
    val distance= abs(targetTint.x) + abs(targetTint.y) + abs(targetTint.z)
    distance<tolerance
}

/* TRANSFORMERS */
fun tSequence(vararg transformers: ImageBuilder.Transformer) = ImageBuilder.Transformer{ raster, x, y ->
    for(transformer in transformers){
        transformer.apply(raster,x,y)
    }
}

fun tTint(tint: Vec3f) = ImageBuilder.Transformer{ raster, x, y ->
    val pixel= raster.getPixel(x,y,IntArray(4))
    val targetLuminosity= (pixel[0]+pixel[1]+pixel[2])/3f
    val newColor= Vec3f(tint.x*targetLuminosity,tint.y*targetLuminosity,tint.z*targetLuminosity)
    raster.setPixel(x,y,intArrayOf(newColor.x.toInt(),newColor.y.toInt(),newColor.z.toInt(),pixel[3]))
}

fun tLuminosity(luminosity: Float) = ImageBuilder.Transformer{ raster, x, y ->
    val pixel= raster.getPixel(x,y,IntArray(4))
    val targetTint= Vec3f(pixel[0].toFloat(), pixel[1].toFloat(), pixel[2].toFloat())
    val luminosity=luminosity*255f
    targetTint.apply { normalize() ; multiplyComponentwise(luminosity,luminosity,luminosity) }
    raster.setPixel(x,y,intArrayOf(targetTint.x.toInt(),targetTint.y.toInt(),targetTint.z.toInt(),pixel[3]))
}


fun tImBlend(im: BufferedImage, ox: Int=0, oy: Int=0, opacity: Float=1f) = tImage(im,ox,oy){ source, target ->
    val alpha=source[3]/255f*opacity
    val ralpha=1f-alpha
    for(i in 0..<3) target[i] = (target[i]*ralpha + source[i]*alpha).toInt()
    target[3]= min(255, target[3]+source[3])
    target
}

fun tImMultiply(im: BufferedImage, ox: Int=0, oy: Int=0) = tImage(im,ox,oy){ source, target ->
    for(i in 0..<4) target[i] = (target[i]*source[i]/255f).toInt()
    target
}

private fun tImage(bufferedImage: BufferedImage, ox: Int=0, oy: Int=0, run: (IntArray, IntArray)->IntArray) = ImageBuilder.Transformer{ raster, x, y ->
    val sx= x-ox
    val sy= y-oy
    if(sx<0 || sy<0 || sx>=bufferedImage.width || sy>=bufferedImage.height) return@Transformer
    val sourcePixel= bufferedImage.raster.getPixel(sx,sy,IntArray(4))
    val targetPixel= raster.getPixel(x,y,IntArray(4))
    raster.setPixel(x,y,run(sourcePixel,targetPixel))
}