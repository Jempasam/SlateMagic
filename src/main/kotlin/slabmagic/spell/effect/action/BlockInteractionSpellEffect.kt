package slabmagic.spell.effect.action

import net.minecraft.block.Block
import net.minecraft.util.math.Vec3f
import slabmagic.entity.BlockFollowingEntity
import slabmagic.entity.SlabMagicEntities
import slabmagic.helper.ColorTools
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.EnergyBlockParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.spellDesc
import slabmagic.spell.spellName
import kotlin.math.sqrt

class BlockInteractionSpellEffect(val block: Block, val duration: Int): SpellEffect {

    override fun use(context: SpellContext): SpellContext {
        val levelDuration=(duration* sqrt(context.power.toFloat())).toInt()
        val trap= BlockFollowingEntity(SlabMagicEntities.BLOCK_FOLLOWING, context.world, levelDuration)
        trap.block=block
        trap.setPosition(context.pos)
        context.world.spawnEntity(trap)
        val color=color
        sendParticleEffect(context.world, EnergyBlockParticleEffect(color,color, 0.5f), context.pos)
        return SpellContext.at(trap,context.power)
    }

    override val name get() = spellName("block_interaction",block.name)

    override val description get() = spellDesc("block_interaction", block.name, duration/20f)

    override val cost: Int get() = duration/4

    override val color: Vec3f get() = ColorTools.vec(ColorTools.of(block))

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(8, 0, 0, 50, 1, 0, 0)}
        .also {
            it[0].apply { subCircleCount=1 ; spacing=30 }
            it[2]= SpellShape.Circle(3, 0, 0, 0, 1, 0, 0)
            it[3]= SpellShape.Circle(3, 0 , 0, 0, 1, 0, 0)
        }
    )

}