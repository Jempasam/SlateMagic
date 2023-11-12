package slatemagic.spell

import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.Vec3f
import slatemagic.helper.ColorTools
import slatemagic.shape.SpellShape

class StubSpell(override val color: Vec3f, override val shape: SpellShape): Spell{

    override fun use(context: SpellContext): SpellContext? = null

    override val name get() = Text.of("Stub Name")

    override val description get() = Text.of("Stub Description")

    override val cost get() = 0

    companion object{
        val INSTANCE=StubSpell(ColorTools.vec(DyeColor.WHITE.fireworkColor), SpellShape())
    }

}