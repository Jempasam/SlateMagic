package slabmagic.components

import net.minecraft.component.DataComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import slabmagic.SlabMagicMod
import slabmagic.registry.SlabMagicRegistry
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell

object SlabMagicComponents{

    val SPELL= register("spell"){ codec(AssembledSpell.CODEC) }

    val PART= register("part"){ codec(SlabMagicRegistry.PARTS.entryCodec) }

    val STORED_CONTEXT= register<SpellContext.Stored>("stored_context"){codec(SpellContext.Stored.CODEC)}

    private fun <T> register(id: String, builderOperator: DataComponentType.Builder<T>.()->Unit): DataComponentType<T> {
        val builder= DataComponentType.builder<T>()
        builder.builderOperator()
        val type=builder.build()
        Registry.register(Registries.DATA_COMPONENT_TYPE, SlabMagicMod /id, type)
        return type
    }
}