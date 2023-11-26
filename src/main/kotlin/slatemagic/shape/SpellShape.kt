package slatemagic.shape

import slatemagic.utils.ByteConversion
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SpellShape(private val circles: Array<Circle>) {

    companion object{
        fun of(vararg circles: Circle) = SpellShape(arrayOf(*circles))
    }

    init{
        require(circles.size==4)
    }

    data class Circle(
        /**
         * >0 : Number of corners
         * =0 : Invisible circle with one corner
         * <0 : Invisible circle with no corner
         */
        var cornerCount: Byte,

        /**
         * >0: Spacing before inner circle
         * 0: No spacing
         * <0: Spacing before inner circle that is now an outer circle
         */
        var spacing: Byte,

        /**
         * >0: Number of sub circle
         * 0: No sub circle
         * <0: No sub circle
         */
        var subCircleCount: Byte,

        /**
         * >0: Radius of sub circle
         * 0: No sub circle
         * <0: No sub circle
         */
        var subCircleRadius: Byte,

        /**
         * >0: Number of repetition
         * 0: No circle
         * <0: No circle
         */
        var repetition: Byte,

        /**
         * >0: Succion depth
         * 0: No succion
         * <0: negative succion depth, inversed succion
         */
        var succionDepth: Byte,

        /**
         * >0: Number of succion
         * 0: No succion
         * <0: No succion
         */
        var succionCount: Byte
    )

    constructor(str: String): this(){
        require(str.length==7*2*4)
        val array=ByteConversion.parse(str)
        for(i in array.indices){
            val circle=circles[i/(7)]
            when(i%7){
                0 -> circle.cornerCount=array[i]
                1 -> circle.spacing=array[i]
                2 -> circle.subCircleCount=array[i]
                3 -> circle.subCircleRadius=array[i]
                4 -> circle.repetition=array[i]
                5 -> circle.succionDepth=array[i]
                6 -> circle.succionCount=array[i]
            }
        }
    }

    constructor() : this(Array(4){ Circle(10,10,0,0,1,0,0) }) {
    }

    operator fun get(index: Int): Circle = circles[index]

    operator fun set(index: Int, value: Circle){ circles[index]=value }

    private fun draw(painter:Painter, x:Double, y:Double, radius:Double, angle:Double, start: Int): Int {
        var nradius=radius
        var ci=start

        while(ci<circles.size){
            if(nradius<0.05) return ci

            // Circle Data
            val circle= circles[ci]
            val nangle=(angle+PI/2*ci).let { if(ci%2==0) it else 2*PI-it }
            val angleStep=2*PI/circle.cornerCount
            val succionDepth=circle.succionDepth.toDouble()*nradius/100.0
            val succionStep=if(circle.succionCount==0.toByte()) 0 else circle.cornerCount/circle.succionCount

            // Circle traces
            for (i in 0 until circle.repetition) {
                val repetitionOffset=i*0.1
                // Draw Shape
                if(circle.cornerCount<=0.toByte())break
                for (j in 0 .. circle.cornerCount) {
                    val id=j%circle.cornerCount
                    // Succion
                    val pointRadius=
                        if(succionStep>0 && id%succionStep==0) nradius-succionDepth-repetitionOffset
                        else nradius-repetitionOffset

                    val px=x + pointRadius * cos(nangle+angleStep*id)
                    val py=y + pointRadius * sin(nangle+angleStep*id)
                    painter.add(px, py)
                }
                painter.stop()
            }

            // Draw Sub Circles
            if(circle.subCircleCount>0){
                if(circle.cornerCount<=0.toByte())break
                for (j in 0 until circle.subCircleCount) {
                    val id=(j*circle.subCircleCount/circle.cornerCount)%circle.cornerCount
                    // Succion
                    val pointRadius=
                        if(succionStep>0 && id%succionStep==0) radius-succionDepth
                        else nradius

                    val corner= j*circle.cornerCount/circle.subCircleCount
                    val px= x + pointRadius * cos(nangle+angleStep*corner)
                    val py= y + pointRadius * sin(nangle+angleStep*corner)

                    val subRadius=circle.subCircleRadius/100.0*nradius
                    draw(painter, px, py, subRadius, nangle-j*PI*2/circle.subCircleCount, ci+1)
                }
            }
            nradius -= circle.spacing/100.0
            ci++
        }
        return circles.size
    }

    fun toCode(): String{
        val ret=Array<Byte>(7*4){0}
        for(i in ret.indices){
            val circle=circles[i/(7)]
            when(i%7){
                0 -> ret[i]=circle.cornerCount
                1 -> ret[i]=circle.spacing
                2 -> ret[i]=circle.subCircleCount
                3 -> ret[i]=circle.subCircleRadius
                4 -> ret[i]=circle.repetition
                5 -> ret[i]=circle.succionDepth
                6 -> ret[i]=circle.succionCount
            }
        }
        return ByteConversion.serialize(ret)
    }

    fun draw(painter:Painter, angle: Double=0.0){
        draw(painter, 0.0,0.0, 1.0, angle, 0)
    }

    interface Painter{
        fun add(x: Double, y: Double)
        fun stop()
    }
}