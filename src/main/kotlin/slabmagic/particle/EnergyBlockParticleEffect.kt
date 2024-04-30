package slabmagic.particle

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.dynamic.Codecs
import org.joml.Vector3f

class EnergyBlockParticleEffect(private val type: ParticleType<out EnergyBlockParticleEffect>, val colorFrom: Vector3f, val colorTo: Vector3f, val size: Float): ParticleEffect {

    constructor(colorFrom: Vector3f, colorTo: Vector3f, size: Float): this(SlabMagicParticles.CUBE, colorFrom, colorTo, size)

    override fun toString() = "EnergyBlock $colorFrom->$colorTo ($size)"

    override fun getType() = type

    companion object{
        inline fun of(colorFrom: Vector3f, colorTo: Vector3f, size: Float, type: SlabMagicParticles.()->ParticleType<out EnergyBlockParticleEffect>)
                = EnergyBlockParticleEffect(SlabMagicParticles.type(), colorFrom, colorTo, size)

        fun codecFor(type: ParticleType<out EnergyBlockParticleEffect>)
            = RecordCodecBuilder.mapCodec{it :RecordCodecBuilder.Instance<EnergyBlockParticleEffect> ->
                it.group(
                    Codecs.VECTOR_3F.fieldOf("from").forGetter { it.colorFrom },
                    Codecs.VECTOR_3F.fieldOf("to").forGetter { it.colorTo },
                    Codecs.POSITIVE_FLOAT.fieldOf("size").forGetter { it.size },
                    ).apply(it){ a,b,c -> EnergyBlockParticleEffect(type, a, b, c)}
            }

        fun packetCodecFor(type: ParticleType<out EnergyBlockParticleEffect>) = PacketCodec.tuple(
            PacketCodecs.VECTOR3F, { it.colorFrom },
            PacketCodecs.VECTOR3F, { it.colorTo },
            PacketCodecs.FLOAT, { it.size },
            { a,b,c -> EnergyBlockParticleEffect(type, a,b,c)}
        )
    }
}