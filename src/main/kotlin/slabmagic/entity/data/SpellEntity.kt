package slabmagic.entity.data

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.Vec3d
import slabmagic.helper.ColorTools
import slabmagic.shape.SpellShape
import slabmagic.spell.build.parts.AssembledSpell
import slabmagic.spell.effect.SpellEffect
import slabmagic.spell.effect.StubSpellEffect

interface SpellEntity {

    val spellData: Data

    val spell: SpellEffect get() = assembled.effect

    var assembled: AssembledSpell
        get() = spellData.spell
        set(v) { spellData.spell = v }

    var power: Int
        get() = spellData.power
        set(v) { spellData.power = v }

    var markeds: List<Vec3d>
        get() = spellData.marked
        set(v) { spellData.marked = v }

    class Data(var spell: AssembledSpell, var power: Int, var marked: List<Vec3d>){

        constructor(): this(AssembledSpell.STUB, 1, listOf())

        fun read(buf: PacketByteBuf){
            try{
                power=buf.readInt()
                val shape=SpellShape(buf.readString())
                spell= AssembledSpell(listOf(),StubSpellEffect(ColorTools.vec(buf.readInt()), shape))
            }catch (e: Exception){
                spell= AssembledSpell.STUB
                power=1
            }
        }

        fun write(buf: PacketByteBuf){
            buf.writeInt(power)
            buf.writeString(spell.effect.shape.toCode())
            buf.writeInt(ColorTools.int(spell.effect.color))
        }

        fun read(nbt: NbtCompound){
            spell=nbt.get("spell") ?.let{ AssembledSpell.fromNbt(it) } ?: AssembledSpell.STUB
            power=nbt.getInt("power")
            marked=nbt.getList("markeds", NbtElement.LIST_TYPE.toInt()).map {m ->
                Vec3d.CODEC.decode(NbtOps.INSTANCE, m).get().orThrow().first
            }
        }

        fun write(nbt: NbtCompound){
            nbt.put("spell", spell.toNbt())
            nbt.putInt("power", power)
            nbt.put("markeds", NbtList().apply {
                for(m in marked){
                    add(Vec3d.CODEC.encode(m, NbtOps.INSTANCE, NbtList()).get().orThrow())
                }
            })
        }
    }
}