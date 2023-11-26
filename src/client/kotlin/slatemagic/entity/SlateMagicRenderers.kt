package slatemagic.entity


import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry

object SlateMagicRenderers {
    init{
        EntityRendererRegistry.register(SlateMagicEntities.SPELL_TURRET, SpellCrossedRenderer.Factory(1.0f))
        EntityRendererRegistry.register(SlateMagicEntities.SPELL_TRAP, ::SpellTrapRenderer)
        EntityRendererRegistry.register(SlateMagicEntities.SPELL_CURSE, ::SpellCurseRenderer)
        EntityRendererRegistry.register(SlateMagicEntities.SPELL_PROJECTILE, ProjectileCrossedRenderer.Factory(0.5f))
    }
}