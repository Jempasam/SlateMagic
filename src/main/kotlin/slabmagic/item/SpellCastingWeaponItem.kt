package slabmagic.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.item.*
import net.minecraft.recipe.Ingredient
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World
import slabmagic.spell.SpellContext

class SpellCastingWeaponItem(settings: Settings) : SwordItem(MATERIAL, 3, -2.4f, settings), SpellItem {

    object MATERIAL: ToolMaterial{
        override fun getDurability(): Int = 50
        override fun getMiningSpeedMultiplier(): Float = 2.0f
        override fun getAttackDamage(): Float = 2.0f
        override fun getMiningLevel(): Int = 1
        override fun getEnchantability(): Int = 20
        override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.DIAMOND)

    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        if(super.postHit(stack, target, attacker)){
            if (!target.world.isClient) {
                val spell = getSpell(stack)
                val power = getPower(stack)
                val markeds = getMarkeds(stack)
                val context = SpellContext.at(target,power)
                context.markeds.addAll(markeds)
                spell.effect.use(context)
            }
            return true
        }
        return false
    }

    override fun getName(stack: ItemStack) = Text.translatable(translationKey, getSpellName(stack))

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        val spell=getSpellName(stack)
        val color=getColor(stack)
        val power=getPower(stack)
        tooltip.add(Text.literal(spell+" "+power).setStyle(Style.EMPTY.withColor(color)))
        super.appendTooltip(stack, world, tooltip, context)
    }

    override fun appendStacks(group: ItemGroup, stacks: DefaultedList<ItemStack>) {
        super<SwordItem>.appendStacks(group, stacks)
        if(isIn(group))appendStacks(this,stacks)
    }
}