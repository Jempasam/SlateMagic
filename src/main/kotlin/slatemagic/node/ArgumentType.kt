package slatemagic.node

import net.minecraft.text.Text
import java.util.function.Predicate

interface ArgumentType<T> : Predicate<T> {

    val name: Text

    val description: Text

    val color: Int

}