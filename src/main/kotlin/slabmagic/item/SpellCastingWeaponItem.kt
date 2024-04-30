package slabmagic.item

import net.minecraft.client.item.TooltipType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.BlockTags
import net.minecraft.text.Text
import slabmagic.item.helper.SpellItemHelpers
import slabmagic.spell.SpellContext
import slabmagic.components.SlabMagicComponents as SCS

class SpellCastingWeaponItem(settings: Settings) : SwordItem(MATERIAL, settings) {//, 3, -2.4f

    object MATERIAL: ToolMaterial{
        override fun getDurability(): Int = 50
        override fun getMiningSpeedMultiplier(): Float = 2.0f
        override fun getAttackDamage(): Float = 2.0f
        override fun getInverseTag() = BlockTags.INCORRECT_FOR_IRON_TOOL
        override fun getEnchantability(): Int = 20
        override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.DIAMOND)

    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        if(super.postHit(stack, target, attacker)){
            if (!target.world.isClient) {
                val spell= stack.get(SCS.SPELL) ?: return false
                val stored= stack.get(SCS.STORED_CONTEXT) ?: SpellContext.Stored.EMPTY
                val context = SpellContext.at(target,stored)
                spell.effect.use(context)
            }
            return true
        }
        return false
    }

    override fun getName(stack: ItemStack) = SpellItemHelpers.spellName(stack)

    override fun appendTooltip(stack: ItemStack, context: TooltipContext, tooltip: MutableList<Text>, type: TooltipType) {
        SpellItemHelpers.spellTooltip(stack, context, tooltip, type)
        super.appendTooltip(stack, context, tooltip, type)
    }
}