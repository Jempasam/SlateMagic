package slatemagic.item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.Vec3d
import slatemagic.helper.ColorTools
import slatemagic.registry.SlateMagicRegistry
import slatemagic.spell.build.AssembledSpell

interface SpellItem {
    fun getSpell(stack: ItemStack) = stack.nbt ?.get("spell") ?.let{ AssembledSpell.fromNbt(it) } ?: AssembledSpell.STUB
    fun setSpell(stack: ItemStack, spell: AssembledSpell){
        stack.orCreateNbt.apply {
            put("spell", spell.toNbt())
            putInt("color", ColorTools.int(spell.effect.color))
            putString("name", spell.effect.name.string)
        }
        stack.orCreateNbt
    }


    fun getSpellName(stack: ItemStack): String{
        val name=stack.nbt ?.getString("name") ?: ""
        return name.ifEmpty {
            getSpell(stack).effect.name.string.apply { stack.orCreateNbt.putString("name", this) }
        }
    }

    fun getColor(stack: ItemStack): Int{
        val color=stack.nbt?.getInt("color") ?: 0
        return if(color==0){
            ColorTools.int(getSpell(stack).effect.color).apply { stack.orCreateNbt.putInt("color",this) }
        } else color
    }

    fun getPower(stack: ItemStack) = stack.nbt ?.getInt("power") ?: 1

    fun setPower(stack: ItemStack, power: Int){
        stack.orCreateNbt.putInt("power", power)
    }

    fun getMarkeds(stack: ItemStack) = stack.nbt
        ?.getList("markeds", 6)
        ?.map {m ->
            Vec3d.CODEC.decode(NbtOps.INSTANCE, m).get().orThrow().first
        }
        ?: listOf()

    fun setMarkeds(stack: ItemStack, markeds: List<Vec3d>){
        stack.orCreateNbt.put("markeds", NbtList().apply {
            markeds.forEach {
                val result=Vec3d.CODEC.encodeStart(NbtOps.INSTANCE, it).result()
                if(result.isPresent)add(result.get())
            }
        })
    }

    fun fill(stack: ItemStack, spell: AssembledSpell, power: Int, markeds: List<Vec3d>){
        setSpell(stack, spell)
        setPower(stack, power)
        setMarkeds(stack, markeds)
    }

    fun appendStacks(item: Item, stacks: DefaultedList<ItemStack>) {
        if (item is SpellItem) {
            for(spell in SlateMagicRegistry.EFFECTS){
                val stack = ItemStack(item)
                item.setSpell(stack, spell)
                item.setPower(stack,1)
                stacks.add(stack)
            }
        }
    }
}