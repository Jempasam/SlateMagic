package slabmagic.particle

import com.mojang.brigadier.StringReader
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.AbstractDustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Vec3f

class SlabMagicParticleEffect(private val type: ParticleType<out SlabMagicParticleEffect>, color: Vec3f, size: Float): AbstractDustParticleEffect(color,size) {

    override fun getType(): ParticleType<*> = type

    object Factory: ParticleEffect.Factory<SlabMagicParticleEffect> {
        override fun read(type: ParticleType<SlabMagicParticleEffect>, reader: StringReader ): SlabMagicParticleEffect {
            val color = readColor(reader)
            reader.expect(' ')
            val f = reader.readFloat()
            return SlabMagicParticleEffect(type, color, f)
        }

        override fun read(type: ParticleType<SlabMagicParticleEffect>, buf: PacketByteBuf ): SlabMagicParticleEffect {
            return SlabMagicParticleEffect(type, readColor(buf), buf.readFloat())
        }
    }
}