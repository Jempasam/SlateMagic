package slabmagic.entity

import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f
import slabmagic.SlabMagicMod
import slabmagic.helper.ColorTools
import slabmagic.shape.painter.CircleVertexPainter
import slabmagic.shape.painter.vertex.EntityVPC

class SpellTrapRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SpellTrapEntity>(ctx) {

    override fun getTexture(entity: SpellTrapEntity): Identifier = SlabMagicMod.id("textures/entity/circle.png")

    override fun render(entity: SpellTrapEntity, yaw: Float, tickDelta: Float, matrix: MatrixStack, vertexConsumers: VertexConsumerProvider, i: Int) {
        val spell=entity.spell

        val x = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderX, entity.getX())
        val y = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderY, entity.getY())
        val z = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderZ, entity.getZ())
        val range = entity.range

        matrix.push()
        matrix.scale(range/2, 1.0f, range/2)
        matrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - yaw))

        val vertexes = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)))

        run{
            val painter=CircleVertexPainter(EntityVPC(vertexes,matrix.peek().positionMatrix), ColorTools.int(spell.color),0.1f/range)
            spell.shape.draw(painter, entity.age.toDouble()/10.0/range)
        }

        matrix.translate(0.0, 0.02/range, 0.0)

        run{
            val painter=CircleVertexPainter(EntityVPC(vertexes,matrix.peek().positionMatrix), DyeColor.WHITE.fireworkColor,0.05f/range)
            spell.shape.draw(painter, (entity.age.toDouble()+tickDelta)/10.0/range)
        }

        matrix.pop()

        super.render(entity, yaw, tickDelta, matrix, vertexConsumers, i)
    }

}