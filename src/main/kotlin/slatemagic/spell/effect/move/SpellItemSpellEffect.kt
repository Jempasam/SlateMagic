package slatemagic.spell.effect.move

import net.minecraft.entity.ItemEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slatemagic.item.SpellItem
import slatemagic.network.messages.AdvancedParticleMessage
import slatemagic.network.messages.sendParticleEffect
import slatemagic.particle.MagicParticleEffect
import slatemagic.shape.SpellShape
import slatemagic.spell.SpellContext
import slatemagic.spell.build.AssembledSpell
import slatemagic.spell.effect.SpellEffect
import kotlin.math.max

class SpellItemSpellEffect(val item: Item, val count: Int, val decorated: AssembledSpell): SpellEffect {

    override fun use(context: SpellContext): SpellContext? {
        val stack=ItemStack(item,count)
        if(item is SpellItem) item.fill(stack, decorated, context.power, context.markeds)
        val dropped=ItemEntity(context.world, context.pos.x, context.pos.y, context.pos.z, stack)
        context.world.spawnEntity(dropped)
        sendParticleEffect(context.world,
            MagicParticleEffect(color, 0.5f),
            context.pos,
            AdvancedParticleMessage.SHOCKWAVE,
            Vec3d(0.3,0.0,0.3),
            10.0
        )
        return context.setEntity(dropped)
    }

    override val name: Text get() = Text.of("Tool of ").also { it.siblings.add(decorated.effect.name) }

    override val description: Text get() = Text.of("give a item that, on use, that ").also { it.siblings.add(decorated.effect.description) }

    override val cost: Int get() = decorated.effect.cost * count * max(1,item.maxDamage)

    override val color: Vec3f get() = decorated.effect.color.apply {
        add(-0.1f,-0.1f,-0.1f)
        clamp(0.0f,1.0f)
    }

    override val shape: SpellShape get() = decorated.effect.shape.also {
        it[2].apply {
            subCircleCount=(subCircleCount+1).toByte()
            if(subCircleRadius<30)subCircleRadius=30
        }
    }

}