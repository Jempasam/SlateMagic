package slabmagic.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import slabmagic.block.entity.NodeBlockEntity
import slabmagic.block.entity.visitAt
import slabmagic.spell.build.parts.SPELL
import slabmagic.spell.build.visitor.AssemblingNodeVisitor
import slabmagic.spell.build.visitor.VisitorException
import kotlin.math.max

class ActivatorItem(settings: Settings) : Item(settings) {

    fun setPower(stack: ItemStack, power: Int) {
        stack.orCreateNbt.putInt("power", max(1,power))
    }

    fun getPower(stack: ItemStack) = stack.nbt?.getInt("power") ?: 1

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if(user.isSneaking){
            val stack=user.getStackInHand(hand)
            setPower(stack,(getPower(stack)+1)%10)
            if(!world.isClient)user.sendMessage(Text.of("Power: ${getPower(stack)}"),true)
            return TypedActionResult.success(stack)
        }
        else return super.use(world, user, hand)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if(context.player?.isSneaking ?: false)return ActionResult.PASS

        if(context.world.getBlockEntity(context.blockPos) is NodeBlockEntity){
            if(!context.world.isClient){
                val visitor= AssemblingNodeVisitor()
                try{
                    visitor.visitAt(context.world,context.blockPos,context.side.opposite)
                }catch (e: VisitorException){
                    context.player?.sendMessage(Text.of(e.message),true)
                    return ActionResult.FAIL
                }
                val result=visitor.result
                val visited=visitor.lastVisited
                if(result!=null && visited!=null && result.first.type==SPELL){
                    val spell=SPELL.get(result.first)
                    visited.cast(spell.effect,getPower(context.stack))
                }
            }
            return ActionResult.SUCCESS
        }
        else return ActionResult.FAIL
    }

    override fun getName(stack: ItemStack): Text {
        return Text.translatable(translationKey, getPower(stack))
    }
}