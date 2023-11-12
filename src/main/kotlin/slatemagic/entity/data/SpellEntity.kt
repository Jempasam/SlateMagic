package slatemagic.entity.data

import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import slatemagic.helper.ColorTools
import slatemagic.registry.SlateMagicRegistry
import slatemagic.shape.SpellShape
import slatemagic.spell.Spell
import slatemagic.spell.StubSpell

interface SpellEntity {

    val spellData: Data

    var spell: Spell
        get() = spellData.spell
        set(v) { spellData.spell = v }

    var power: Int
        get() = spellData.power
        set(v) { spellData.power = v }

    class Data(var spell: Spell, var power: Int){

        constructor(): this(StubSpell.INSTANCE, 1)

        fun read(buf: PacketByteBuf){
            try{
                power=buf.readInt()
                val shape=SpellShape(buf.readString())
                spell=StubSpell(ColorTools.vec(buf.readInt()), shape)
            }catch (e: Exception){
                spell=StubSpell.INSTANCE
                power=1
            }
        }

        fun write(buf: PacketByteBuf){
            buf.writeInt(power)
            buf.writeString(spell.shape.toCode())
            buf.writeInt(ColorTools.int(spell.color))
        }

        fun read(nbt: NbtCompound){
            spell=nbt.getString("spell")
                ?.let { Identifier.tryParse(it) }
                ?.let { SlateMagicRegistry.SPELLS.get(it) }
                ?: StubSpell.INSTANCE
            power=nbt.getInt("power")
        }

        fun write(nbt: NbtCompound){
            spell.let{SlateMagicRegistry.SPELLS.getId(it)}
                ?.let { nbt.putString("spell", it.toString()) }
            nbt.putInt("power", power)
        }
    }
}