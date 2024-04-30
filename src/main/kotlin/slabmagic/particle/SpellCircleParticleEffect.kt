package slabmagic.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.Identifier
import slabmagic.helper.ColorTools
import slabmagic.registry.SlabMagicRegistry
import slabmagic.shape.SpellShape
import slabmagic.spell.effect.SpellEffect

open class SpellCircleParticleEffect(private val type: ParticleType<*>, val shape: SpellShape, val color: Int, val size: Float, val duration: Int=100): ParticleEffect {

    override fun toString() = shape.toCode()

    override fun getType() = type

    companion object{
        fun circle(spell: SpellEffect, size: Float=1.0f, duration: Int=200)
                = SpellCircleParticleEffect(SlabMagicParticles.SPELL_CIRCLE, spell.shape, ColorTools.int(spell.color), size, duration)

        fun crossed(spell: SpellEffect, size: Float=1.0f, duration: Int=200)
                = SpellCircleParticleEffect(SlabMagicParticles.SPELL_CROSSED, spell.shape, ColorTools.int(spell.color), size, duration)

        fun sphere(spell: SpellEffect, size: Float=1.0f, duration: Int=200)
                = SpellCircleParticleEffect(SlabMagicParticles.SPELL_SPHERE, spell.shape, ColorTools.int(spell.color), size, duration)

        fun ofStr(str: String): SpellShape{
            return try{
                val id=Identifier(str)
                val spell=SlabMagicRegistry.EFFECTS.get(id)?.effect ?: throw Exception()
                spell.shape
            }catch (e: Exception){
                SpellShape(str)
            }
        }

        fun codecFor(type: ParticleType<out SpellCircleParticleEffect>)
            = RecordCodecBuilder.mapCodec { it :RecordCodecBuilder.Instance<SpellCircleParticleEffect> ->
                it.group(
                    Codec.STRING.xmap({ofStr(it)},{it.toCode()}) .fieldOf("shape").forGetter { it.shape },
                    Codec.INT .fieldOf("color") .forGetter { it.color },
                    Codec.FLOAT .optionalFieldOf("size",1f) .forGetter { it.size },
                    Codec.INT .optionalFieldOf("duration",100) .forGetter { it.duration },
                ).apply(it){ a,b,c,d -> SpellCircleParticleEffect(type, a,b,c,d)}
            }

        fun packetCodecFor(type: ParticleType<out SpellCircleParticleEffect>) = PacketCodec.tuple(
            PacketCodecs.STRING.xmap({SpellShape(it)},{it.toCode()}), { it.shape },
            PacketCodecs.INTEGER, { it.color },
            PacketCodecs.FLOAT, { it.size },
            PacketCodecs.INTEGER, { it.duration },
            { a,b,c,d -> SpellCircleParticleEffect(type, a,b,c,d)}
        )
    }
}