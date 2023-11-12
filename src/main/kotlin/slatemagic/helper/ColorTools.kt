package slatemagic.helper

import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.DyeItem
import net.minecraft.item.Item
import net.minecraft.item.SpawnEggItem
import net.minecraft.util.DyeColor
import net.minecraft.util.math.ColorHelper.Argb.*
import net.minecraft.util.math.Vec3f
import java.awt.Color

object ColorTools {

    fun vec(color: Int): Vec3f{
        return Vec3f(
            getRed(color)/255f,
            getGreen(color)/255f,
            getBlue(color)/255f
        )
    }

    fun awt(vec: Vec3f): Color{
        return Color(
            vec.x,
            vec.y,
            vec.z
        )
    }

    fun int(vec: Vec3f): Int{
        return getArgb(
            255,
            (vec.x*255).toInt(),
            (vec.y*255).toInt(),
            (vec.z*255).toInt()
        )
    }

    fun of(block: Block): Int {
        return block.defaultState.material.color.color
    }



    private val entity_colors= mutableMapOf<EntityType<*>,Int>()
    fun of(entity: EntityType<*>): Int {
        return entity_colors.computeIfAbsent(entity){
            SpawnEggItem.forEntity(entity)?.run {
                val c0=getColor(0)
                val c1=getColor(1)
                getArgb(
                    0,
                    (getRed(c0)+getRed(c1))/2,
                    (getGreen(c0)+getGreen(c1))/2,
                    (getBlue(c0)+getBlue(c1))/2
                )
            }
                ?: DyeColor.GRAY.fireworkColor.takeIf { entity.translationKey.contains("iron") }
                ?: DyeColor.ORANGE.fireworkColor.takeIf { entity.translationKey.contains("fire") }
                ?: DyeColor.YELLOW.fireworkColor.takeIf { entity.translationKey.contains("honey") }
                ?: DyeColor.PURPLE.fireworkColor.takeIf { entity.translationKey.contains("ender") }
                ?: DyeColor.PINK.fireworkColor.takeIf { entity.spawnGroup==SpawnGroup.AXOLOTLS }
                ?: DyeColor.BLUE.fireworkColor.takeIf { entity.spawnGroup==SpawnGroup.WATER_CREATURE }
                ?: DyeColor.LIGHT_BLUE.fireworkColor.takeIf { entity.spawnGroup==SpawnGroup.WATER_AMBIENT }
                ?: DyeColor.ORANGE.fireworkColor.takeIf { entity.isFireImmune }
                ?: DyeColor.GREEN.fireworkColor.takeIf { entity.spawnGroup==SpawnGroup.CREATURE }
                ?: DyeColor.BROWN.fireworkColor.takeIf { entity.spawnGroup==SpawnGroup.MISC }
                ?: DyeColor.PURPLE.fireworkColor
        }
    }



    private val item_colors= mutableMapOf<Item,Int>()
    fun of(item: Item): Int {
        return item_colors.computeIfAbsent(item) {
            DyeColor.GRAY.fireworkColor.takeIf { item.translationKey.contains("iron") }
                ?: DyeColor.LIGHT_BLUE.fireworkColor.takeIf { item.translationKey.contains("diamond") }
                ?: DyeColor.YELLOW.fireworkColor.takeIf { item.translationKey.contains("gold") }
                ?: DyeColor.RED.fireworkColor.takeIf { item.translationKey.contains("red") }
                ?: DyeColor.BROWN.fireworkColor.takeIf { item.translationKey.contains("wood") }
                ?: DyeColor.GRAY.fireworkColor.takeIf { item.translationKey.contains("stone") }
                ?: DyeColor.GREEN.signColor.takeIf { item.translationKey.contains("leave") }
                ?: DyeColor.YELLOW.fireworkColor.takeIf { item.translationKey.contains("honey") }
                ?: DyeColor.LIGHT_BLUE.fireworkColor.takeIf { item.translationKey.contains("ice") }
                ?: DyeColor.PURPLE.fireworkColor.takeIf { item.translationKey.contains("ender") }
                ?: DyeColor.BROWN.signColor.takeIf { item.isFood }
                ?: (item as? DyeItem)?.color?.signColor
                ?: DyeColor.ORANGE.fireworkColor.takeIf { item.isFireproof }
                ?: DyeColor.LIGHT_GRAY.fireworkColor
        }
    }



    fun of(effect: StatusEffect): Int {
        return effect.color
    }


}