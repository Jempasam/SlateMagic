package slabmagic.spell.effect.action

import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import slabmagic.helper.ColorTools
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.EnergyBlockParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.pierce
import slabmagic.spell.spellDescIt
import slabmagic.spell.spellName

class BlockReplaceSpellEffect(val replaced: Set<Block>, val block: Block): SpellEffect {

    override fun use(context: SpellContext): SpellContext {
        pierce(context)
        val bpos=BlockPos.ofFloored(context.pos)
        if(replaced.contains(context.world.getBlockState(bpos).block)){
            context.world.setBlockState(bpos,block.defaultState)
            val color=color
            sendParticleEffect(context.world, EnergyBlockParticleEffect(color,color,0.5f), Vec3d.ofCenter(bpos))
        }

        return context
    }

    override val name get() = spellName("block_replace", block.name)

    override val description get() = spellDescIt("block_replace", replaced, block.name){ arrayOf(it.name) }

    override val cost: Int get() = 5

    override val color: Vector3f get() = ColorTools.vec(ColorTools.of(block))

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(8, 0, 0, 20, 1, 0, 0)}
        .also {
            it[0].apply { subCircleCount=1 ; spacing=30 }
            it[2]= SpellShape.Circle(4, 0, 0, 0, 1, 0, 0)
            it[3]= SpellShape.Circle(4, 0 , 0, 0, 1, 0, 0)
        }
    )

}