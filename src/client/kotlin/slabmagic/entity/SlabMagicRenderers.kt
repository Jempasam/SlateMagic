package slabmagic.entity


import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry

object SlabMagicRenderers {
    init{
        EntityRendererRegistry.register(SlabMagicEntities.SPELL_TURRET, SpellCrossedRenderer.Factory(1.0f))
        EntityRendererRegistry.register(SlabMagicEntities.SPELL_TRAP, ::SpellTrapRenderer)
        EntityRendererRegistry.register(SlabMagicEntities.SPELL_CURSE, ::SpellCurseRenderer)
        EntityRendererRegistry.register(SlabMagicEntities.SPELL_PROJECTILE, ProjectileCrossedRenderer.Factory(0.5f))
        EntityRendererRegistry.register(SlabMagicEntities.SPELL_SHIELD, ::SpellShieldRenderer)
        EntityRendererRegistry.register(SlabMagicEntities.SPELL_ENCHANTING, ::SpellShieldRenderer)
        EntityRendererRegistry.register(SlabMagicEntities.BLOCK_FOLLOWING, ::BlockFollowingRenderer)
    }
}