package slabmagic.particle

import com.mojang.brigadier.StringReader
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.AbstractDustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Vec3f

class EnergyBlockParticleEffect(private val type: ParticleType<out EnergyBlockParticleEffect>, val colorFrom: Vec3f, val colorTo: Vec3f, val size: Float): ParticleEffect {

    constructor(colorFrom: Vec3f, colorTo: Vec3f, size: Float): this(SlabMagicParticles.CUBE, colorFrom, colorTo, size)

    override fun write(buf: PacketByteBuf) {
        buf.writeFloat(this.colorFrom.x)
        buf.writeFloat(this.colorFrom.y)
        buf.writeFloat(this.colorFrom.z)

        buf.writeFloat(this.colorTo.x)
        buf.writeFloat(this.colorTo.y)
        buf.writeFloat(this.colorTo.z)

        buf.writeFloat(this.size)
    }

    override fun asString(): String = "EnergyBlock $colorFrom->$colorTo ($size)"

    override fun getType() = type

    companion object{
        inline fun of(colorFrom: Vec3f, colorTo: Vec3f, size: Float, type: SlabMagicParticles.()->ParticleType<out EnergyBlockParticleEffect>)
                = EnergyBlockParticleEffect(SlabMagicParticles.type(), colorFrom, colorTo, size)
    }

    object Factory: ParticleEffect.Factory<EnergyBlockParticleEffect> {
        override fun read(type: ParticleType<EnergyBlockParticleEffect>, reader: StringReader): EnergyBlockParticleEffect {
            val fromColor = AbstractDustParticleEffect.readColor(reader)
            val toColor = AbstractDustParticleEffect.readColor(reader)
            reader.expect(' ')
            val f = reader.readFloat()
            return EnergyBlockParticleEffect(type,fromColor, toColor, f)
        }

        override fun read(type: ParticleType<EnergyBlockParticleEffect>, buf: PacketByteBuf): EnergyBlockParticleEffect {
            return EnergyBlockParticleEffect(type, AbstractDustParticleEffect.readColor(buf), AbstractDustParticleEffect.readColor(buf), buf.readFloat())
        }

    }
}