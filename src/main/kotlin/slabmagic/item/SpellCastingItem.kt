package slabmagic.item

import net.minecraft.client.item.TooltipType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import slabmagic.components.SlabMagicComponents
import slabmagic.item.helper.SpellItemHelpers
import slabmagic.spell.SpellContext

class SpellCastingItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        if (!world.isClient) {
            val spell = stack.get(SlabMagicComponents.SPELL)?.effect ?: return TypedActionResult.fail(stack)
            val stored = stack.get(SlabMagicComponents.STORED_CONTEXT) ?: SpellContext.Stored.EMPTY
            val context = SpellContext.atEye(user, stored)
            spell.use(context)
            if (stack.isDamageable) stack.damage(1, user, LivingEntity.getSlotForHand(hand))
            else stack.decrementUnlessCreative(1,user)
        }
        return TypedActionResult.success(stack)
    }

    override fun getName(stack: ItemStack) = SpellItemHelpers.spellName(stack)

    override fun appendTooltip(stack: ItemStack, context: TooltipContext, tooltip: MutableList<Text>, type: TooltipType) {
        SpellItemHelpers.spellTooltip(stack, context, tooltip, type)
        super.appendTooltip(stack, context, tooltip, type)
    }

}