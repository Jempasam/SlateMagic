package slabmagic.entity

import net.minecraft.block.BlockRenderType
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class BlockFollowingRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<BlockFollowingEntity>(ctx) {

    private val blockRenderManager: BlockRenderManager = ctx.blockRenderManager

    init{
        shadowRadius = 0.5f
    }

    override fun render(entity: BlockFollowingEntity, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
        val block = entity.block
        val state = block.defaultState
        val bpos = BlockPos.ofFloored(entity.pos)
        if (state.renderType == BlockRenderType.MODEL) {
            val world: World = entity.world
            if (state !== world.getBlockState(entity.blockPos) && state.renderType != BlockRenderType.INVISIBLE) {
                matrices.push()
                val blockPos = BlockPos.ofFloored(
                    entity.x,
                    entity.boundingBox.maxY,
                    entity.z
                )
                matrices.translate(-0.5, 0.0, -0.5)
                blockRenderManager.modelRenderer.render(
                    world,
                    blockRenderManager.getModel(state),
                    state,
                    blockPos,
                    matrices,
                    vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(state)),
                    false,
                    Random.create(),
                    state.getRenderingSeed(bpos),
                    OverlayTexture.DEFAULT_UV
                )
                matrices.pop()
                super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
            }
        }
    }

    override fun getTexture(entity: BlockFollowingEntity) = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE
}