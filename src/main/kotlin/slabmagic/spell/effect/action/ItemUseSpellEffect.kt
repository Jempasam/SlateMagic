package slabmagic.spell.effect.action

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import slabmagic.helper.ColorTools
import slabmagic.network.messages.AdvancedParticleMessage
import slabmagic.network.messages.sendParticleEffect
import slabmagic.particle.EnergyBlockParticleEffect
import slabmagic.particle.MagicParticleEffect
import slabmagic.shape.SpellShape
import slabmagic.simulator.PlayerSimulator
import slabmagic.spell.SpellContext
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.pierce

abstract class ItemUseSpellEffect(val stack: ItemStack): SpellEffect {

    override val name: Text get() = stack.name

    override val description: Text get() = Text.literal("simulate using a ").append(stack.name).append(action)

    override val cost: Int get() = 5

    override val color: Vec3f get() = ColorTools.vec(ColorTools.of(stack.item))

    override val shape: SpellShape get() = SpellShape("17100045013412040d000f013500031a0060011d001118025b012c0e")

    protected abstract val action: Text

    class Block(stack: ItemStack): ItemUseSpellEffect(stack){
        override val action: Text get() = Text.literal(" on block")

        override fun use(context: SpellContext): SpellContext?{
            pierce(context)
            val bpos=BlockPos(context.pos)
            val player= PlayerSimulator(context.world, bpos)
            return if(player.useOnBlock(stack.copy(), bpos, context.direction).isAccepted){
                context.world.spawnParticles(
                    EnergyBlockParticleEffect(color,color,0.5f),
                    bpos.x+0.5, bpos.y+0.5, bpos.z+0.5,
                    1,
                    0.0,0.0,0.0,
                    0.0
                )
                context
            } else null
        }
    }

    class Entity(stack: ItemStack): ItemUseSpellEffect(stack){
        override val action: Text get() = Text.literal(" on entity")

        override fun use(context: SpellContext): SpellContext?{
            val bpos= BlockPos(context.pos)
            val player= PlayerSimulator(context.world, bpos)
            val entity= context.entity
            return if(entity is LivingEntity && player.useOnEntity(stack.copy(), entity, context.direction).isAccepted){
                sendParticleEffect(
                    context.world,
                    MagicParticleEffect(color, 0.5f),
                    entity.pos.add(0.0,0.5,0.0),
                    AdvancedParticleMessage.SPIRAL,
                    Vec3d(1.5,1.5,1.5),
                    40.0+10.0*context.power
                )
                context
            } else null
        }
    }

    class Direction(stack: ItemStack): ItemUseSpellEffect(stack){
        override val action: Text get() = Text.empty()

        override fun use(context: SpellContext): SpellContext?{
            val bpos= BlockPos(context.pos)
            val player= PlayerSimulator(context.world, bpos)
            val directionVector=Vec3d.fromPolar(context.direction.x,context.direction.y).multiply(0.2)
            return if(player.use(stack.copy(), context.pos, context.direction).isAccepted){
                sendParticleEffect(
                    context.world,
                    MagicParticleEffect(color, 0.3f),
                    context.pos,
                    AdvancedParticleMessage.BOOM,
                    Vec3d(0.1,0.1,0.1),
                    6.0,
                    speed=directionVector
                )
                context
            } else null
        }
    }
}