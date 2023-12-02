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

class ProjectileCrossedRenderer(ctx: EntityRendererFactory.Context, val factory: Factory) : EntityRenderer<SpellProjectileEntity>(ctx) {

    override fun getTexture(entity: SpellProjectileEntity): Identifier = SlabMagicMod.id("textures/entity/circle.png")

    override fun render(entity: SpellProjectileEntity, yaw: Float, tickDelta: Float, matrix: MatrixStack, vertexConsumers: VertexConsumerProvider, i: Int) {
        val spell=entity.spell

        val pitch= MathHelper.lerp(tickDelta,entity.prevPitch, entity.pitch)
        val yaw= MathHelper.lerp(tickDelta,entity.prevYaw, entity.yaw)

        matrix.push()
        matrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - yaw))
        matrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(pitch))
        matrix.scale(factory.size, factory.size, factory.size)
        val vertexs = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)))
        val panter=CrossedVertexPainter(EntityVPC(vertexs,matrix.peek().positionMatrix), ColorTools.int(spell.color),0.1f)
        spell.shape.draw(panter, (entity.age.toDouble()+tickDelta)/10.0)

        matrix.pop()

        super.render(entity, yaw, tickDelta, matrix, vertexConsumers, i)
    }

    class Factory(val size: Float): EntityRendererFactory<SpellProjectileEntity>{
        override fun create(ctx: EntityRendererFactory.Context): ProjectileCrossedRenderer{
            return ProjectileCrossedRenderer(ctx,this)
        }
    }

}