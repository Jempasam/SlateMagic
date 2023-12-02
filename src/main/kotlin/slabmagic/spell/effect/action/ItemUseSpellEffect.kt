package slabmagic.spell.effect.action

import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3f
import slabmagic.helper.ColorTools
import slabmagic.particle.EnergyBlockParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.simulator.PlayerSimulator
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.pierce

class ItemUseSpellEffect(val stack: ItemStack): SpellEffect {

    override fun use(context: SpellContext): SpellContext?{
        pierce(context)
        val bpos=BlockPos(context.pos)
        val player= PlayerSimulator(context.world, bpos)
        if(player.useAt(stack.copy(), bpos, context.direction)){
            context.world.spawnParticles(
                EnergyBlockParticleEffect(color,color,0.5f),
                bpos.x+0.5, bpos.y+0.5, bpos.z+0.5,
                1,
                0.0,0.0,0.0,
                0.0
            )
            return context
        }
        else return null
    }

    override val name: Text get() = stack.name

    override val description: Text get() = Text.literal("simulate using a ").append(stack.name)

    override val cost: Int get() = 5

    override val color: Vec3f get() = ColorTools.vec(ColorTools.of(stack.item))

    override val shape: SpellShape get() = SpellShape("17100045013412040d000f013500031a0060011d001118025b012c0e")

}