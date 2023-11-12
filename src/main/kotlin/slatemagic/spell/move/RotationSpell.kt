package slatemagic.spell.move

import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper.lerp
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3f
import slatemagic.helper.ColorTools
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.SlateMagicParticles
import slatemagic.particle.SpellCircleParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.Spell
import slatemagic.spell.SpellContext
import kotlin.random.Random

class RotationSpell(val minimum: Vec2f, val maximum: Vec2f, val decorated: Spell): Spell {

    override fun use(context: SpellContext): SpellContext? {
        val rot=Vec2f(
            lerp(minimum.x, maximum.x, Random.nextFloat())+context.direction.x,
            lerp(minimum.x, maximum.x, Random.nextFloat())+context.direction.y
        )
        context.direction=rot
        sendParticleEffect(
            context.world,
            SpellCircleParticleEffect(SlateMagicParticles.SPELL_CROSSED, shape, ColorTools.int(color), 0.5f),
            context.pos
        )
        return decorated.use(context)
    }

    override val name: Text get(){
        var str=""
        val length=minimum.add(maximum.negate()).length()
        if(length>160)str+="chaotic "
        else if(length>80)str+="random "
        val height=(minimum.x+maximum.x)/2
        if(height>60)str+="sky "
        else if(height<-60)str+="ground "
        return Text.of(str).apply { siblings.add(decorated.name) }
    }

    override val description: Text get(){
        var str=""
        val length=minimum.add(maximum.negate()).length()
        if(length>160)str+="highly scatteredly "
        else if(length>80)str+="scatteredly "
        val height=(minimum.x+maximum.x)/2
        if(height>60)str+="going up "
        else if(height<-60)str+="going down "
        return Text.of(str).apply { siblings.add(decorated.name) }
    }

    override val cost: Int get() = (decorated.cost*(1.0-maximum.add(minimum.negate()).length()/500.0)).toInt()

    override val color: Vec3f get() = decorated.color.apply {
        val length=-maximum.add(minimum.negate()).length().toFloat()/800f
        add(0f,length,length)
        clamp(0f,1f)
    }

    override val shape: SpellShape get() = SpellShape(
        Array(4) {SpellShape.Circle(8, 0, 0, 50, 1, 0, 0)}
        .also {
            it[0].apply { subCircleCount=1 ; spacing=30 }
            it[2]= SpellShape.Circle(3, 0, 0, 0, 1, 0, 0)
            it[3]= SpellShape.Circle(3, 0 , 0, 0, 1, 0, 0)
        }
    )

}