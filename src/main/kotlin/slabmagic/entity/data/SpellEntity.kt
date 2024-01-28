package slabmagic.entity.data

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.Vec3d
import slabmagic.helper.ColorTools
import slabmagic.shape.SpellShape
import slabmagic.spell.build.AssembledSpell
import slabmagic.spell.effect.action.StubSpellEffect

interface SpellEntity {

    val spellData: Data

    val spell get() = assembled.effect

    val spells get() = spellData.spells

    var assembled: AssembledSpell
        get() = spellData.spell
        set(v) { spellData.spell = v }

    var power: Int
        get() = spellData.power
        set(v) { spellData.power = v }

    var markeds: List<Vec3d>
        get() = spellData.marked
        set(v) { spellData.marked = v }

    class Data(var spells: List<AssembledSpell>, var power: Int, var marked: List<Vec3d>){

        constructor(): this(listOf(AssembledSpell.STUB), 1, listOf())

        var spell
            get() = spells[0]
            set(value){ spells= listOf(value) }

        fun read(buf: PacketByteBuf){
            try{
                power=buf.readInt()
                spells= List(buf.readVarInt()){
                    val shape=SpellShape(buf.readString())
                    AssembledSpell(listOf(), StubSpellEffect(ColorTools.vec(buf.readInt()), shape))
                }
            }catch (e: Exception){
                spell= AssembledSpell.STUB
                power=1
            }
        }

        fun write(buf: PacketByteBuf){
            buf.writeInt(power)
            buf.writeVarInt(spells.size)
            for(s in spells){
                buf.writeString(s.effect.shape.toCode())
                buf.writeInt(ColorTools.int(s.effect.color))
            }
        }

        fun read(nbt: NbtCompound){
            spells= nbt.getList("spells", NbtElement.LIST_TYPE.toInt())
                .map { AssembledSpell.fromNbt(it) ?: AssembledSpell.STUB }
            power= nbt.getInt("power")
            marked= nbt.getList("markeds", NbtElement.LIST_TYPE.toInt()).map {m ->
                Vec3d.CODEC.decode(NbtOps.INSTANCE, m).get().orThrow().first
            }
        }

        fun write(nbt: NbtCompound){
            nbt.put("spells", NbtList().apply{ addAll(spells.map{it.toNbt()}) })
            nbt.putInt("power", power)
            nbt.put("markeds", NbtList().apply {
                for(m in marked){
                    add(Vec3d.CODEC.encode(m, NbtOps.INSTANCE, NbtList()).get().orThrow())
                }
            })
        }
    }
}