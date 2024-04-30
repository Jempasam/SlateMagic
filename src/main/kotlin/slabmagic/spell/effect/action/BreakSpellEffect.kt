package slabmagic.spell.effect.action

import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import org.joml.Vector3f
import slabmagic.helper.ColorTools
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.pierce
import slabmagic.spell.spellDesc
import slabmagic.spell.spellName

class BreakSpellEffect(val strength: Float): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        pierce(context)
        val bpos=BlockPos.ofFloored(context.pos)
        val state=context.world.getBlockState(bpos)
        if(!state.isAir && state.getHardness(context.world,bpos)<strength){
            context.world.breakBlock(bpos,true)
            return context
        }
        val color=color
        return null
    }

    override val name: Text get() = spellName("break")
    override val description: Text get() = spellDesc("break",strength)

    override val cost: Int get() = 1+strength.toInt()*3

    override val color: Vector3f get() = ColorTools.vec(0x3E4E4F)

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