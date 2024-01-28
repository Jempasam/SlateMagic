package slabmagic.particle

import com.mojang.brigadier.StringReader
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.AbstractDustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Vec3f

class MagicParticleEffect(private val type: ParticleType<out MagicParticleEffect>, color: Vec3f, size: Float): AbstractDustParticleEffect(color,size) {

    constructor(color: Vec3f, size: Float): this(SlabMagicParticles.MAGIC, color, size)

    override fun getType() = type

    companion object{
        inline fun of(color: Vec3f, size: Float, type: SlabMagicParticles.()->ParticleType<out MagicParticleEffect>)
            = MagicParticleEffect(SlabMagicParticles.type(), color, size)
    }

    object Factory: ParticleEffect.Factory<MagicParticleEffect> {
        override fun read(type: ParticleType<MagicParticleEffect>, reader: StringReader ): MagicParticleEffect {
            val color = readColor(reader)
            reader.expect(' ')
            val f = reader.readFloat()
            return MagicParticleEffect(type, color, f)
        }

        override fun read(type: ParticleType<MagicParticleEffect>, buf: PacketByteBuf ): MagicParticleEffect {
            return MagicParticleEffect(type, readColor(buf), buf.readFloat())
        }
    }
}