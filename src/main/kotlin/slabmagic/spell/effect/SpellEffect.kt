package slabmagic.spell.effect

import net.minecraft.text.Text
import org.joml.Vector3f
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext

/**
 * A spell effect is a single effect that can be applied to a spell.
 */
interface SpellEffect {

    /**
     * Use the spell effect at the given context.
     * @return the new context, or null if the spell failed.
     */
    fun use(context: SpellContext): SpellContext?

    /**
     * @return the name of the spell effect.
     */
    val name: Text

    /**
     * @return the description of the spell effect.
     */
    val description: Text

    /**
     * @return the cost of the spell effect.
     */
    val cost: Int

    /**
     * @return the color of the spell effect.
     */
    val color: Vector3f

    /**
     * @return the shape of the spell effect.
     */
    val shape: SpellShape

}