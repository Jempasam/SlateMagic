package slabmagic.block.properties

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import slabmagic.spell.build.visited.BlockPartVisited
import slabmagic.spell.build.visited.SlabPartVisited
import slabmagic.spell.build.visitor.AmbiguousPartVisitorException
import slabmagic.spell.build.visitor.PartVisitor

interface VisitableBlock {
    fun visit(visitor: PartVisitor, visited: SlabPartVisited): SlabPartVisited?
}


private fun PartVisitor.visitAt(visited: SlabPartVisited): SlabPartVisited?{
    val world=visited.block.world
    val pos=visited.block.pos

    // Performance Optimization with fast Tests
    if(world.isAir(pos))return null

    // Get block
    val state=world.getBlockState(pos)
    val block=state.block
    if(block !is VisitableBlock)return null

    // Visit
    val newVisited=block.visit(this,visited) ?: return null

    // Find next
    var finded: SlabPartVisited?=null
    for(next_direction in Direction.entries)if(next_direction!=newVisited.block.direction.opposite){
        val next_pos=pos.offset(next_direction)
        val subVisited=newVisited.copy(
            block=newVisited.block.copy( pos=next_pos, direction=next_direction)
        )
        val next=visitAt(subVisited)
        if(next!=null){
            if(finded!=null){
                val except= AmbiguousPartVisitorException(newVisited,finded,next)
                exception(visited,except)
                throw except
            }
            finded=next
        }
    }
    if(finded==null)end(newVisited)
    return finded ?: newVisited
}

fun PartVisitor.visitAt(world: World, pos: BlockPos, direction: Direction): SlabPartVisited {
    val visited=SlabPartVisited(BlockPartVisited(world,pos,direction), 0, {})
    return visitAt(visited) ?: visited
}
