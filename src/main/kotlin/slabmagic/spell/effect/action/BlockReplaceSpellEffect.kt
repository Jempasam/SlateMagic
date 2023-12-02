package slabmagic.spell.effect.action

import net.minecraft.block.Block
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3f
import slabmagic.helper.ColorTools
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.pierce

class BlockReplaceSpellEffect(val replaced: Set<Block>, val block: Block): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val bpos=BlockPos(context.pos)
        pierce(context)
        if(replaced.contains(context.world.getBlockState(bpos).block))
            context.world.setBlockState(bpos,block.defaultState)
        return context
    }

    override val name: Text get() = block.name.apply { siblings.add(Text.of(" Transmutation")) }

    override val description: Text get(){
        val ret=Text.literal("replace ")
        for(r in replaced)ret.siblings.apply {
            add(r.name)
            add(Text.literal(", "))
        }
        ret.siblings.removeLast()
        ret.siblings.add(Text.literal(" with "))
        ret.siblings.add(block.name)
        return ret
    }

    override val cost: Int get() = 5

    override val color: Vec3f get() = ColorTools.vec(ColorTools.of(block))

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(8, 0, 0, 20, 1, 0, 0)}
        .also {
            it[0].apply { subCircleCount=1 ; spacing=30 }
            it[2]= SpellShape.Circle(4, 0, 0, 0, 1, 0, 0)
            it[3]= SpellShape.Circle(4, 0 , 0, 0, 1, 0, 0)
        }
    )

}