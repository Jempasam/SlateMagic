package slatemagic.shape.painter.vertex

interface VertexPainterConsumer {
    fun add(x: Float, y: Float, z: Float, red: Float, green: Float, blue: Float, corner: Int)
}