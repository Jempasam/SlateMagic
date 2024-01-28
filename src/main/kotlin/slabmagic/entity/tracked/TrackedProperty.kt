package slabmagic.entity.tracked

import net.minecraft.entity.Entity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandler
import kotlin.reflect.KProperty

class TrackedProperty<T>(val trackedData: TrackedData<T>) {
    operator fun getValue(property: Entity, parent: KProperty<*>) = property.dataTracker.get(trackedData)

    operator fun setValue(property: Entity, parent: KProperty<*>, value: T) = property.dataTracker.set(trackedData, value)
}

class TrackedPropertyProvider<T>(val handler: TrackedDataHandler<T>){
    operator fun provideDelegate(thisRef: Entity, property: KProperty<*>) = tracked(DataTracker.registerData(thisRef::class.java, handler))
}

fun <T> tracked(trackedData: TrackedData<T>) = TrackedProperty(trackedData)

fun <T> tracked(handler: TrackedDataHandler<T>, trackedDataList: MutableList<TrackedData<*>>) = TrackedPropertyProvider(handler)