package slatemagic.particle

import com.mojang.brigadier.StringReader
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.AbstractDustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Vec3f

class MagicParticleEffect(color: Vec3f, size: Float): AbstractDustParticleEffect(color,size) {

    override fun getType(): ParticleType<*> = SlateMagicParticles.MAGIC

    object Factory: ParticleEffect.Factory<MagicParticleEffect> {
        override fun read(type: ParticleType<MagicParticleEffect>, reader: StringReader ): MagicParticleEffect {
            val color = readColor(reader)
            reader.expect(' ')
            val f = reader.readFloat()
            return MagicParticleEffect(color, f)
        }

        override fun read(type: ParticleType<MagicParticleEffect>, buf: PacketByteBuf ): MagicParticleEffect {
            return MagicParticleEffect(readColor(buf), buf.readFloat())
        }
    }
}