package slabmagic.spell.effect

import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.Vec3f
import slabmagic.helper.ColorTools
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext

class StubSpellEffect(override val color: Vec3f, override val shape: SpellShape): SpellEffect {

    override fun use(context: SpellContext): SpellContext? = null

    override val name get() = Text.of("Stub Name")

    override val description get() = Text.of("Stub Description")

    override val cost get() = 0

    companion object{
        val INSTANCE= StubSpellEffect(ColorTools.vec(DyeColor.WHITE.fireworkColor), SpellShape())
    }

}