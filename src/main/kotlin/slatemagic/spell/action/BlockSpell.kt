package slatemagic.spell.action

import net.minecraft.block.Block
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3f
import slatemagic.helper.ColorTools
import slatemagic.shape.SpellShape
import slatemagic.spell.Spell
import slatemagic.spell.SpellContext

class BlockSpell(val block: Block): Spell {

    override fun use(context: SpellContext): SpellContext? {
        context.world.setBlockState(BlockPos(context.pos),block.defaultState)
        return context
    }

    override val name: Text get() = block.name

    override val description: Text get() = Text.of("summon a ").apply { siblings.add(block.name) }

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