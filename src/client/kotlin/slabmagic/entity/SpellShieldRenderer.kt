package slabmagic.entity

import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f
import slabmagic.SlabMagicMod
import slabmagic.helper.ColorTools
import slabmagic.shape.painter.CrossedVertexPainter
import slabmagic.shape.painter.vertex.EntityVPC

class SpellShieldRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SpellFollowingEntity>(ctx) {

    override fun getTexture(entity: SpellFollowingEntity): Identifier = SlabMagicMod.id("textures/entity/circle.png")

    override fun render(entity: SpellFollowingEntity, yaw: Float, tickDelta: Float, matrix: MatrixStack, vertexConsumers: VertexConsumerProvider, i: Int) {
        val spell=entity.spell

        val x = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderX, entity.x)
        val y = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderY, entity.y)
        val z = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderZ, entity.z)

        matrix.push()
        matrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - yaw))
        matrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.pitch))
        matrix.scale(entity.range, entity.range, entity.range)
        val vertexs = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)))
        val painter= CrossedVertexPainter(EntityVPC(vertexs,matrix.peek().positionMatrix), ColorTools.int(spell.color),0.1f)
        spell.shape.draw(painter, (entity.age.toDouble()+tickDelta)/10.0)

        matrix.pop()

        super.render(entity, yaw, tickDelta, matrix, vertexConsumers, i)
    }

}