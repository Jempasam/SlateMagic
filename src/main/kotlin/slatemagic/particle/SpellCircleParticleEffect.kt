package slatemagic.particle

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import slatemagic.helper.ColorTools
import slatemagic.registry.SlateMagicRegistry
import slatemagic.shape.SpellShape

open class SpellCircleParticleEffect(private val type: ParticleType<*>, val shape: SpellShape, val color: Int, val size: Float, val duration: Int=100): ParticleEffect {

    override fun write(buf: PacketByteBuf) {
        buf.writeString(shape.toCode())
        buf.writeInt(color)
        buf.writeFloat(size)
        buf.writeInt(duration)
    }

    override fun asString(): String = shape.toCode()

    override fun getType(): ParticleType<*> = type

    object Factory: ParticleEffect.Factory<SpellCircleParticleEffect> {
        val UNKNOWN_SPELL=DynamicCommandExceptionType({ Text.of("Unknown spell: $it") })

        override fun read(type: ParticleType<SpellCircleParticleEffect>, reader: StringReader ): SpellCircleParticleEffect {
            reader.expect(' ')
            val str=reader.readString()

            val (shape,color)=try{
                SpellShape(str) to reader.readInt()
            }catch (e: Exception){
                val id=Identifier.tryParse(str) ?: throw UNKNOWN_SPELL.create(str)
                val spell=SlateMagicRegistry.SPELLS.get(id) ?: throw UNKNOWN_SPELL.create(str)
                spell.shape to ColorTools.int(spell.color)
            }

            reader.expect(' ')
            val size=reader.readFloat()
            reader.expect(' ')
            val duration=reader.readInt()

            return SpellCircleParticleEffect(type, shape, color, size, duration)
        }

        override fun read(type: ParticleType<SpellCircleParticleEffect>, buf: PacketByteBuf ): SpellCircleParticleEffect {
            return SpellCircleParticleEffect(type, SpellShape(buf.readString()),buf.readInt(), buf.readFloat(), buf.readInt())
        }
    }
}