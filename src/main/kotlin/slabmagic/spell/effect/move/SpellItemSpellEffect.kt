package slabmagic.spell.effect.move

import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.ItemEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import slabmagic.components.SlabMagicComponents
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell
import slabmagic.spell.effect.SpellEffect
import slabmagic.utils.coerceIn

class SpellItemSpellEffect(val item: Item, val count: Int, val decorated: AssembledSpell): SpellEffect {

    override fun use(context: SpellContext): SpellContext {
        val stack=ItemStack(item,count)
        stack.set(SlabMagicComponents.STORED_CONTEXT,context.stored)
        val dropped=ItemEntity(context.world, context.pos.x, context.pos.y, context.pos.z, stack)
        context.world.spawnEntity(dropped)
        sendParticleEffect(context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.Shape.SHOCKWAVE,
            Vec3d(0.3,0.0,0.3),
            10.0
        )
        return context.setEntity(dropped)
    }

    override val name: Text get() = item.name.copy().append(Text.of(" of ")).append(decorated.effect.name)

    override val description: Text get() = Text.literal("give $count ").append(item.name).append(" item that, on use, ").append(decorated.effect.description)

    override val cost: Int get() = decorated.effect.cost * count * (item.components.get(DataComponentTypes.MAX_DAMAGE)?:1)

    override val color: Vector3f get() = decorated.effect.color.apply {
        add(-0.1f,-0.1f,-0.1f)
        coerceIn(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.effect.shape.also {
        it[2].apply {
            subCircleCount=(subCircleCount+1).toByte()
            if(subCircleRadius<30)subCircleRadius=30
        }
    }

}