package slabmagic.entity.data

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import slabmagic.spell.SpellContext
import slabmagic.spell.build.AssembledSpell
import kotlin.jvm.optionals.getOrNull

interface SpellEntity {

    val spellData: Data

    val spell get() = assembled.effect

    val spells get() = spellData.spells

    val assembled get() = spellData.spell

    val stored get() = spellData.stored

    data class Data(val spells: List<AssembledSpell>, val stored: SpellContext.Stored){

        constructor(): this(listOf(AssembledSpell.STUB), SpellContext.Stored.EMPTY)

        val spell get() = spells[0]

        fun read(nbt: NbtCompound): Data?{
            return CODEC.decode(NbtOps.INSTANCE, nbt.get("spell_data")).result().getOrNull()?.first
        }

        fun write(nbt: NbtCompound){
            CODEC.encodeStart(NbtOps.INSTANCE, this).ifSuccess { nbt.put("spell_data", it) }
        }

        companion object{
            val CODEC= RecordCodecBuilder.create {it :RecordCodecBuilder.Instance<Data> ->
                it.group(
                    AssembledSpell.CODEC.listOf() .fieldOf("spells") .forGetter{it.spells},
                    SpellContext.Stored.CODEC .fieldOf("stored") .forGetter{it.stored},
                ).apply(it, ::Data)
            }

            val EMPTY=Data()
        }
    }
}