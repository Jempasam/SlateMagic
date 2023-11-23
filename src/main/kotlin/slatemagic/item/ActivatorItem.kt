package slatemagic.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import slatemagic.block.entity.NodeBlockEntity
import slatemagic.block.entity.fetchSpellPart
import slatemagic.spell.SpellContext
import slatemagic.spell.build.SPELL
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
                val (pos,direction,part)= fetchSpellPart(context.world,context.blockPos,context.side.opposite)
                if(part.type == SPELL){
                    val spell=SPELL.get(part)
                    spell.use(SpellContext.at(
                        context.world as ServerWorld,
                        Vec3d.ofCenter(pos.offset(direction)),
                        Vec2f(direction.offsetY*-90f,direction.asRotation()),
                        getPower(context.stack)
                    ))
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