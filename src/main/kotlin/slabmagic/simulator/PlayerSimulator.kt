package slabmagic.simulator

import com.mojang.authlib.GameProfile
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

class PlayerSimulator(world: World, pos: BlockPos)
    : PlayerEntity(world, pos, 0f, GameProfile(UUID.randomUUID(),"Player"), null)
{
    override fun isSpectator() = false

    override fun isCreative() = false

    fun useAt(stack: ItemStack, pos: BlockPos, rot: Vec2f): Boolean{
        val position=Vec3d.ofCenter(pos)
        setRotation(rot.x,rot.y)
        setPosition(position)
        setStackInHand(Hand.MAIN_HAND,stack)
        if(!stack.item.useOnBlock(ItemUsageContext(this,Hand.MAIN_HAND, BlockHitResult(position,Direction.UP,pos,true))).isAccepted){
            val result=stack.item.use(world,this, Hand.MAIN_HAND).result.isAccepted
            stack.item.finishUsing(mainHandStack,world,this)
            return result
        }
        else return true
    }
}