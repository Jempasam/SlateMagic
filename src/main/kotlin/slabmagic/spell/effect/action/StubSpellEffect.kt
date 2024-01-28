package slabmagic.spell.effect.action

import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.Vec3f
import slabmagic.helper.ColorTools
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.spellDesc
import slabmagic.spell.spellName

class StubSpellEffect(override val color: Vec3f, override val shape: SpellShape): SpellEffect {

    override fun use(context: SpellContext): SpellContext? = null

    override val name get() = spellName("stub")

    override val description get() = spellDesc("stub")



    override val cost get() = 0

    companion object{
        val INSTANCE= StubSpellEffect(ColorTools.vec(DyeColor.WHITE.fireworkColor), SpellShape())
    }

    object Named: SpellEffect{
        val ASSEMBLED= AssembledSpell(listOf(), Named)
        override fun use(context: SpellContext) = null
        override val name = Text.of("A Spell")
        override val description = Text.of("cast a spell")
        override val cost = 0
        override val color = ColorTools.MAGENTA
        override val shape = SpellShape()

    }

    object AnotherNamed: SpellEffect{
        val ASSEMBLED= AssembledSpell(listOf(), AnotherNamed)
        override fun use(context: SpellContext) = null
        override val name = Text.of("Another Spell")
        override val description = Text.of("cast another spell")
        override val cost = 0
        override val color = ColorTools.MAGENTA
        override val shape = SpellShape()

    }
}