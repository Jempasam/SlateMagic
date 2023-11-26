package slatemagic.spell.direction

import net.minecraft.util.math.Vec3d
import slatemagic.spell.SpellContext

class ForwardSpellDirection(val distance: Double): SpellDirection {
    override fun get(context: SpellContext): Vec3d {
        val directionVector=Vec3d.fromPolar(context.direction.x,context.direction.y)
        val leveledDistance= distance*(1.0+0.5*context.power)
        return directionVector.multiply(leveledDistance)
    }

    override val cost: Int get() = 1+distance.toInt()
}