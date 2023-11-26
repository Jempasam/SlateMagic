package slatemagic.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World
import slatemagic.spell.SpellContext

class SpellCastingItem(settings: Settings) : Item(settings), SpellItem {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        if (!world.isClient) {
            val spell = getSpell(stack)
            val power = getPower(stack)
            val markeds = getMarkeds(stack)
            val context = SpellContext.atEye(user, power)
            context.markeds.addAll(markeds)
            spell.effect.use(context)
            if (stack.isDamageable) stack.damage(1, user) { it.sendToolBreakStatus(hand) }
            else stack.decrement(1)
        }
        return TypedActionResult.success(stack)
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
        super<Item>.appendStacks(group, stacks)
        if(isIn(group))appendStacks(this,stacks)
    }
}