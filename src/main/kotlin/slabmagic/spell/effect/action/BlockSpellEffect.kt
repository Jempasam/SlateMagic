package slabmagic.spell.effect.action

import net.minecraft.block.Block
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3f
import slabmagic.helper.ColorTools
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.spellDesc
import slabmagic.spell.spellName

class BlockSpellEffect(val block: Block): SpellEffect {

    override fun use(context: SpellContext): SpellContext {
        val bpos=BlockPos(context.pos)
        if(context.world.getBlockState(bpos).material.isReplaceable){
            context.world.breakBlock(bpos,true)
            context.world.setBlockState(bpos,block.defaultState)
        }
        return context
    }

    override val name: Text get() = spellName("set_block", block.name)
    override val description: Text get() = spellDesc("set_block", block.name)

    override val cost: Int get() = 10

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