package slabmagic.block

import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.IntProperty

val TRIGGERED=BooleanProperty.of("triggered")
val LEVEL=IntProperty.of("level",0,7)