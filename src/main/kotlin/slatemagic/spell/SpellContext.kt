package slatemagic.spell

import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d

data class SpellContext private constructor(
    var world: ServerWorld,
    private var _pos: Vec3d,
    var direction: Vec2f,
    var power: Int,
    private var _entity: Entity?
) {

    val markeds: MutableList<Vec3d> = mutableListOf()

    val entity: Entity?
        get() = _entity

    var pos: Vec3d
        get() = _pos
        set(value) {
            _pos=value
            _entity=null
        }

    fun copy(world: ServerWorld=this.world, pos: Vec3d=this.pos, direction: Vec2f=this.direction, power: Int=this.power): SpellContext{
        val ret=SpellContext(world, pos, direction, power, if(pos===_pos)_entity else null)
        ret.markeds.addAll(markeds)
        return ret
    }

    companion object{

        fun at(world: ServerWorld, pos: Vec3d, rot : Vec2f=Vec2f.ZERO, power: Int=1)
            = SpellContext(world, pos, rot, power, null)

        fun at(entity: Entity, power: Int=1)
            = SpellContext(entity.world as ServerWorld, entity.pos, Vec2f(entity.pitch,entity.yaw), power, entity)

        fun atEye(entity: Entity, power: Int=1)
            = SpellContext(entity.world as ServerWorld, entity.eyePos, Vec2f(entity.pitch,entity.yaw), power, entity)
    }



    private fun locateEntity(){
        if(_entity==null){
            _entity=world.getOtherEntities(null, Box.of(pos, 0.1,0.1, 0.1)).firstOrNull()
        }
    }

}