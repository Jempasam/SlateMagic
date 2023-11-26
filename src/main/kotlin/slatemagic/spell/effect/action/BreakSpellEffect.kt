package slatemagic.spell.effect.action

import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3f
import slatemagic.helper.ColorTools
import slatemagic.shape.SpellShape
import slatemagic.spell.SpellContext
import slatemagic.spell.effect.SpellEffect

class BreakSpellEffect(val strength: Float): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val bpos=BlockPos(context.pos)
        val state=context.world.getBlockState(bpos)
        if(!state.isAir && state.getHardness(context.world,bpos)<strength){
            context.world.breakBlock(bpos,true)
            return context
        }
        return null
    }

    override val name: Text get() = Text.of("Break")

    override val description: Text get() = Text.of("break with a strength of $strength")

    override val cost: Int get() = 1+strength.toInt()*3

    override val color: Vec3f get() = ColorTools.vec(0x3E4E4F)

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(8, 20, 0, 30, 1, 40, 4)}.also {
            it[1].apply {
                subCircleCount=2
                subCircleRadius=60
            }
            it[2].spacing=40
            it[3].succionDepth=20
        }
    )

}