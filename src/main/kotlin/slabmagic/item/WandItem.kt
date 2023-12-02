package slabmagic.item

import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import slabmagic.block.entity.NodeBlockEntity
import slabmagic.block.entity.visitAt
import slabmagic.helper.ColorTools
import slabmagic.spell.build.parts.AssembledSpell
import slabmagic.spell.build.parts.SPELL
import slabmagic.spell.build.visitor.AssemblingNodeVisitor
import slabmagic.spell.build.visitor.Visited
import slabmagic.spell.build.visitor.VisitorException

class WandItem(val action: (Visited, ServerPlayerEntity?, AssembledSpell) -> ActionResult, settings: Settings) : Item(settings) {

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if(context.world.getBlockEntity(context.blockPos) is NodeBlockEntity){
            if(!context.world.isClient){
                val visitor= AssemblingNodeVisitor()
                try{
                    visitor.visitAt(context.world,context.blockPos,context.side.opposite)
                }catch (e: VisitorException){
                    context.player?.sendMessage(Text.of(e.message),true)
                    return ActionResult.FAIL
                }
                val result=visitor.result
                val visited=visitor.lastVisited
                if(result!=null && visited!=null && result.first.type==SPELL){
                    val spell=SPELL.get(result.first)
                    action(visited,context.player as? ServerPlayerEntity,spell)
                }
            }
            return ActionResult.SUCCESS
        }
        else return ActionResult.FAIL
    }

    companion object{
        fun cast(visited: Visited, player: ServerPlayerEntity?, spell: AssembledSpell): ActionResult {
            return if(visited.cast(spell.effect, 1)!=null) ActionResult.SUCCESS else ActionResult.FAIL
        }

        fun cost(visited: Visited, player: ServerPlayerEntity?, spell: AssembledSpell): ActionResult {
            val cost=Text.literal("Cost: ${spell.effect.cost}").setStyle(Style.EMPTY.withColor(DyeColor.BLUE.fireworkColor))
            visited.say(cost)
            player?.sendMessage(cost,false)
            return ActionResult.SUCCESS
        }

        fun desc(visited: Visited, player: ServerPlayerEntity?, spell: AssembledSpell): ActionResult {
            val desc=spell.effect.description.copy().setStyle(Style.EMPTY.withColor(ColorTools.int(spell.effect.color)))
            visited.say(desc)
            player?.sendMessage(desc,false)
            return ActionResult.SUCCESS
        }

        fun name(visited: Visited, player: ServerPlayerEntity?, spell: AssembledSpell): ActionResult {
            val name=spell.effect.name.copy().setStyle(Style.EMPTY.withColor(ColorTools.int(spell.effect.color)))
            visited.say(name)
            player?.sendMessage(name,false)
            return ActionResult.SUCCESS
        }

        fun composition(visited: Visited, player: ServerPlayerEntity?, spell: AssembledSpell): ActionResult {
            val composition=Text.empty()
            for(part in spell.parts){
                composition.append(part.name.copy().setStyle(Style.EMPTY.withColor(ColorTools.int(part.color))))
                composition.append(Text.of(" > "))
            }
            if(!spell.parts.isEmpty())composition.siblings.removeLast()
            visited.say(composition)
            player?.sendMessage(composition,false)
            return ActionResult.SUCCESS
        }

        fun resume(visited: Visited, player: ServerPlayerEntity?, spell: AssembledSpell): ActionResult {
            return if(
                name(visited,player,spell).isAccepted
                && desc(visited,player,spell).isAccepted
                && composition(visited,player,spell).isAccepted
                && cost(visited,player,spell).isAccepted
                ) ActionResult.SUCCESS else ActionResult.FAIL
        }
    }
}