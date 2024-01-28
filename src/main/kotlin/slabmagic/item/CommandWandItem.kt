package slabmagic.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class CommandWandItem(settings: Settings) : Item(settings) {

    fun getCommand(stack: ItemStack) = stack.nbt?.getString("command")
    fun setCommand(stack: ItemStack, command: String) = stack.orCreateNbt.putString("command", command)

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val command=getCommand(context.stack)
        val player=context.player
        if(command!=null && player!=null){
            val world=context.world
            if(world is ServerWorld){
                world.server.commandManager.executeWithPrefix(player.commandSource.withMaxLevel(3).withPosition(Vec3d.ofCenter(context.blockPos)), command)
            }
            return ActionResult.SUCCESS
        }
        return ActionResult.FAIL
    }

    override fun useOnEntity(stack: ItemStack, player: PlayerEntity, entity: LivingEntity, hand: Hand): ActionResult {
        val command=getCommand(stack)
        if(command!=null){
            val world=player.world
            if(world is ServerWorld){
                world.server.commandManager.executeWithPrefix(entity.commandSource.withMaxLevel(3), command)
            }
            return ActionResult.SUCCESS
        }
        return ActionResult.FAIL
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack=player.getStackInHand(hand)
        val command=getCommand(stack)
        if(command!=null){
            if(world is ServerWorld){
                world.server.commandManager.executeWithPrefix(player.commandSource.withMaxLevel(3), command)
            }
            return TypedActionResult.success(stack)
        }
        return TypedActionResult.fail(stack)
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, tooltip, context)
        val command=getCommand(stack)
        if(command!=null){
            tooltip.add(Text.of(command))
        }
        else tooltip.add(Text.literal("No command").styled{ it.withColor(DyeColor.GRAY.fireworkColor) })
    }


}