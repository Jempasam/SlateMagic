package slabmagic.item

import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraft.util.Formatting
import slabmagic.block.properties.VisitableBlock
import slabmagic.block.properties.visitAt
import slabmagic.helper.ColorTools
import slabmagic.item.WandItem.Action
import slabmagic.spell.build.AssembledSpell
import slabmagic.spell.build.parts.SPELL
import slabmagic.spell.build.visited.SlabPartVisited
import slabmagic.spell.build.visitor.AssemblingPartVisitor
import slabmagic.spell.build.visitor.VisitorException

class WandItem(val action: Action, settings: Settings) : Item(settings) {

    fun interface Action{
        fun apply(visited: SlabPartVisited, user: ServerPlayerEntity?, spell: AssembledSpell, power: Int): ActionResult
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val block=context.world.getBlockState(context.blockPos).block
        if(block is VisitableBlock){
            if(!context.world.isClient){
                val visitor= AssemblingPartVisitor()
                val visited=try{
                    visitor.visitAt(context.world,context.blockPos,context.side.opposite)
                }catch (e: VisitorException){
                    context.player?.sendMessage(Text.of(e.message),true)
                    return ActionResult.FAIL
                }
                val result=visitor.result
                if(visited!=null && result!=null && result.first.type==SPELL){
                    val spell=SPELL.get(result.first)
                    action.apply(visited,context.player as? ServerPlayerEntity, spell, 1)
                }
            }
            return ActionResult.SUCCESS
        }
        else return ActionResult.FAIL
    }

    companion object{

        fun withLevel(action: Action, level: Int)
            = Action{v, u, s, p -> action.apply(v, u, s, level) }

        fun cast(visited: SlabPartVisited, player: ServerPlayerEntity?, spell: AssembledSpell, power: Int): ActionResult {
            return if(visited.cast(spell.effect, power)!=null) ActionResult.SUCCESS else ActionResult.FAIL
        }

        fun show(visited: SlabPartVisited, player: ServerPlayerEntity?, spell: AssembledSpell, power: Int){
            visited.show(spell.effect.shape,ColorTools.int(spell.effect.color))
        }

        fun castAndConsume(visited: SlabPartVisited, player: ServerPlayerEntity?, spell: AssembledSpell, power: Int): ActionResult {
            return if(spell.effect.cost*power<visited.energy){
                cast(visited,player,spell,power)
                visited.consumer()
                ActionResult.CONSUME
            }
            else{
                player?.sendMessage(Text.literal("Not enough energy! ${visited.energy}/${spell.effect.cost*power}").setStyle(Style.EMPTY.withColor(Formatting.RED)),false)
                visited.show(ColorTools.RED)
                ActionResult.FAIL
            }
        }

        fun cost(visited: SlabPartVisited, player: ServerPlayerEntity?, spell: AssembledSpell, power: Int): ActionResult {
            val cost=Text.literal("Cost: ${spell.effect.cost*power}(${spell.effect.cost}) | Energy: ${visited.energy}").setStyle(Style.EMPTY.withColor(DyeColor.BLUE.fireworkColor))
            visited.say(cost)
            player?.sendMessage(cost,false)
            return ActionResult.SUCCESS
        }

        fun desc(visited: SlabPartVisited, player: ServerPlayerEntity?, spell: AssembledSpell, power: Int): ActionResult {
            val desc=spell.effect.description.copy().setStyle(Style.EMPTY.withColor(ColorTools.int(spell.effect.color)))
            visited.say(desc)
            player?.sendMessage(desc,false)
            return ActionResult.SUCCESS
        }

        fun name(visited: SlabPartVisited, player: ServerPlayerEntity?, spell: AssembledSpell, power: Int): ActionResult {
            val name=spell.effect.name.copy().setStyle(Style.EMPTY.withColor(ColorTools.int(spell.effect.color)))
            visited.say(name)
            player?.sendMessage(name,false)
            return ActionResult.SUCCESS
        }

        fun composition(visited: SlabPartVisited, player: ServerPlayerEntity?, spell: AssembledSpell, power: Int): ActionResult {
            val composition=Text.empty()
            for(part in spell.parts){
                composition.append(part.name.copy().setStyle(Style.EMPTY.withColor(ColorTools.int(part.color))))
                composition.append(Text.of(" > "))
            }
            if(spell.parts.isNotEmpty())composition.siblings.removeLast()
            visited.say(composition)
            player?.sendMessage(composition,false)
            return ActionResult.SUCCESS
        }

        fun resume(visited: SlabPartVisited, player: ServerPlayerEntity?, spell: AssembledSpell, power: Int): ActionResult {
            return if(
                name(visited, player, spell, power).isAccepted
                && desc(visited, player, spell, power).isAccepted
                && composition(visited, player, spell, power).isAccepted
                && cost(visited, player, spell, power).isAccepted
                ) ActionResult.SUCCESS else ActionResult.FAIL
        }
    }
}