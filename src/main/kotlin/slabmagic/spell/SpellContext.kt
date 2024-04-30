package slabmagic.spell

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import slabmagic.block.BatteryBlock

data class SpellContext private constructor(
    var world: ServerWorld,
    private var _pos: Vec3d,
    var direction: Vec2f,
    private var _entity: Entity?,
    var stored: Stored,
) {

    data class Stored(val power: Int, val markeds: List<Vec3d> = emptyList()){
        companion object{
            val EMPTY=Stored(1, emptyList())

            val CODEC= RecordCodecBuilder.create {it :RecordCodecBuilder.Instance<Stored> ->
                it.group(
                    Codecs.NONNEGATIVE_INT .fieldOf("power") .forGetter(Stored::power),
                    Vec3d.CODEC.listOf() .fieldOf("markeds") .forGetter(Stored::markeds),
                ).apply(it,::Stored)
            }
        }
    }

    val markeds: MutableList<Vec3d> = mutableListOf()

    val entity: Entity?
        get() = _entity

    var pos: Vec3d
        get() = _pos
        set(value) {
            _pos=value
            _entity=null
        }

    fun copy(world: ServerWorld=this.world, pos: Vec3d=this.pos, direction: Vec2f=this.direction, power: Stored=this.stored): SpellContext{
        val ret=SpellContext(world, pos, direction, if(pos===_pos)_entity else null, stored)
        ret.markeds.addAll(markeds)
        return ret
    }

    fun setEntity(entity: Entity): SpellContext{
        _entity=entity
        _pos=entity.pos
        return this
    }

    companion object{
        fun at(world: ServerWorld, pos: Vec3d, rot : Vec2f=Vec2f.ZERO, stored: Stored=Stored.EMPTY)
            = SpellContext(world, pos, rot, null, stored)

        fun at(entity: Entity, stored: Stored=Stored.EMPTY)
            = SpellContext(entity.world as ServerWorld, entity.pos, Vec2f(entity.pitch,entity.yaw), entity, stored)

        fun atEye(entity: Entity, stored: Stored=Stored.EMPTY)
            = SpellContext(entity.world as ServerWorld, entity.eyePos, Vec2f(entity.pitch,entity.yaw), entity, stored)
    }

    private fun locateEntity(){
        if(_entity==null){
            _entity=world.getOtherEntities(null, Box.of(pos, 0.1,0.1, 0.1)).firstOrNull()
        }
    }

}