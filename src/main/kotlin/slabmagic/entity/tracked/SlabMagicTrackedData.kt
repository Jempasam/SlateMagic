package slabmagic.entity.tracked

import io.netty.buffer.ByteBuf
import net.minecraft.entity.Entity
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import slabmagic.entity.data.SpellEntity
import slabmagic.shape.SpellShape
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object SlabMagicTrackedData {


    val SPELL_SHAPE= register(PacketCodecs.STRING.xmap({SpellShape(it)},{it.toCode()}))

    val SPELL_DATA= register(PacketCodecs.codec(SpellEntity.Data.CODEC))

    val BLOCK= register(PacketCodecs.STRING.xmap( {Registries.BLOCK.get(Identifier(it))}, {Registries.BLOCK.getId(it).toString()} ))

    fun <T> register(codec: PacketCodec<ByteBuf,T>): TrackedDataHandler<T> {
        val handler=TrackedDataHandler.create(codec)
        TrackedDataHandlerRegistry.register(handler)
        return handler
    }
}

operator fun <T> TrackedData<T>.provideDelegate(thisRef: Entity, prop: KProperty<*>): ReadWriteProperty<Entity,T> {
    val handler=this
    return object: ReadWriteProperty<Entity,T>{
        override fun getValue(thisRef: Entity, property: KProperty<*>) = thisRef.dataTracker.get(handler)
        override fun setValue(thisRef: Entity, property: KProperty<*>, value: T) = thisRef.dataTracker.set(handler,value)
    }
}