package slabmagic.simulator

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.text.Text

object FalseStatusEffect: StatusEffect(StatusEffectCategory.BENEFICIAL, 0) {
    override fun getName() = Text.of("an effect")
}