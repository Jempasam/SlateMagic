package slatemagic.entity

import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f
import slatemagic.SlateMagicMod
import slatemagic.helper.ColorTools
import slatemagic.shape.painter.CrossedVertexPainter
import slatemagic.shape.painter.vertex.EntityVPC

class SpellCrossedRenderer(ctx: EntityRendererFactory.Context, val factory: Factory) : EntityRenderer<SimpleSpellEntity>(ctx) {

    override fun getTexture(entity: SimpleSpellEntity): Identifier = SlateMagicMod.id("textures/entity/circle.png")

    override fun render(entity: SimpleSpellEntity, yaw: Float, tickDelta: Float, matrix: MatrixStack, vertexConsumers: VertexConsumerProvider, i: Int) {
        val spell=entity.spell

        val x = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderX, entity.x)
        val y = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderY, entity.y)
        val z = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderZ, entity.z)

        matrix.push()
        matrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - yaw))
        matrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.pitch))
        matrix.scale(factory.size, factory.size, factory.size)
        val vertexs = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)))
        val panter=CrossedVertexPainter(EntityVPC(vertexs,matrix.peek().positionMatrix), ColorTools.int(spell.color),0.1f)
        spell.shape.draw(panter)

        matrix.pop()

        super.render(entity, yaw, tickDelta, matrix, vertexConsumers, i)
    }

    class Factory(val size: Float): EntityRendererFactory<SimpleSpellEntity>{
        override fun create(ctx: EntityRendererFactory.Context): SpellCrossedRenderer{
            return SpellCrossedRenderer(ctx,this)
        }
    }

}