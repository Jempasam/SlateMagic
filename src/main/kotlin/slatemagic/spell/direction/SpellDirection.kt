package slatemagic.spell.direction

import net.minecraft.util.math.Vec3d
import slatemagic.spell.SpellContext

interface SpellDirection {
    fun get(context: SpellContext): Vec3d

    val cost: Int
}