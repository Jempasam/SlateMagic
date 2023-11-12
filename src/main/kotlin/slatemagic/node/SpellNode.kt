package slatemagic.node

interface SpellNode<T> {

    data class Argument(val type: ArgumentType<*>, val min: Int, val max: Int)

    val arguments: List<Argument>

    fun make(arguments: List<Any>): T

}