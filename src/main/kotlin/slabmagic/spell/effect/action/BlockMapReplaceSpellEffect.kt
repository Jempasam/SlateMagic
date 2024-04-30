package slabmagic.spell.effect.action

import net.minecraft.block.Block
import net.minecraft.text.Text
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

class BlockMapReplaceSpellEffect(override val name: Text, override val description: Text, val replaceMap: Map<Block,Block>): SpellEffect {

    constructor(name: Text, description: Text, vararg replaceMap: Pair<Block,Block>): this(name, description, replaceMap.toMap())
    override fun use(context: SpellContext): SpellContext {
        pierce(context)
        val bpos=BlockPos.ofFloored(context.pos)
        val result= replaceMap[context.world.getBlockState(bpos).block]
        if(result!=null){
            context.world.setBlockState(bpos,result.defaultState)
            val color=color
            sendParticleEffect(context.world, EnergyBlockParticleEffect(color,color,0.5f), Vec3d.ofCenter(bpos))
        }

        return context
    }

    override val cost: Int get() = 5

    override val color: Vector3f get() = ColorTools.vec(ColorTools.of(replaceMap.values.first()))

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(8, 0, 0, 20, 1, 0, 0)}
        .also {
            it[0].apply { subCircleCount=1 ; spacing=30 }
            it[2]= SpellShape.Circle(4, 0, 0, 0, 1, 0, 0)
            it[3]= SpellShape.Circle(4, 0 , 0, 0, 1, 0, 0)
        }
    )

}