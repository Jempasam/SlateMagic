package slabmagic.block

import com.google.common.base.Predicates
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random

class PlayerDetectorBlock(val type: Class<out Entity>, val range: Double, val cadency: Int, settings: Settings): AbstractTickBlock(settings) {

    override fun time(random: Random) = cadency

    override fun test(state: BlockState, world: ServerWorld, pos: BlockPos): Boolean{
        val box = Box.of(Vec3d.ofCenter(pos),range,range,range)
        val finded = world.getEntitiesByClass(type, box, Predicates.alwaysTrue())
        return finded.size>0
    }

}