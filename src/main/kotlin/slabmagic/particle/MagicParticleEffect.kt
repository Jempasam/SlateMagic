package slabmagic.particle

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.AbstractDustParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.dynamic.Codecs
import org.joml.Vector3f

class MagicParticleEffect(private val type: ParticleType<out MagicParticleEffect>, val color: Vector3f, size: Float): AbstractDustParticleEffect(size) {

    constructor(color: Vector3f, size: Float): this(SlabMagicParticles.MAGIC, color, size)

    override fun getType() = type

    companion object{
        inline fun of(color: Vector3f, size: Float, type: SlabMagicParticles.()->ParticleType<out MagicParticleEffect>)
            = MagicParticleEffect(SlabMagicParticles.type(), color, size)

        fun codecFor(type: ParticleType<out MagicParticleEffect>)
            = RecordCodecBuilder.mapCodec{it :RecordCodecBuilder.Instance<MagicParticleEffect> ->
                it.group(
                    SCALE_CODEC.fieldOf("scale").forGetter { it.scale },
                    Codecs.VECTOR_3F.fieldOf("color").forGetter { it.color }
                ).apply(it){ scale, color -> MagicParticleEffect(type, color, scale)}
            }

        fun packetCodecFor(type: ParticleType<out MagicParticleEffect>) = PacketCodec.tuple(
            PacketCodecs.FLOAT, { it.scale },
            PacketCodecs.VECTOR3F, { it.color },
            { scale, color -> MagicParticleEffect(type, color, scale)}
        )
    }
}