package hiiragi283.lib.data

import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

@HTBuilderMarker
interface HolderAccepter<T : Any> {
    operator fun Holder<T>.unaryPlus()

    interface FluidAccepter : HolderAccepter<Fluid> {
        @Suppress("DEPRECATION")
        operator fun Fluid.unaryPlus() {
            +this.builtInRegistryHolder()
        }
    }

    interface ItemAccepter : HolderAccepter<Item> {
        @Suppress("DEPRECATION")
        operator fun Item.unaryPlus() {
            +this.builtInRegistryHolder()
        }
    }

    //    ValueBuilder    //

    open class ValueBuilder<T : Any> : HolderAccepter<T> {
        private var holder: Holder<T> by HTDelegates.onceInitialize()

        override fun Holder<T>.unaryPlus() {
            check(this.delegate is Holder.Reference<T>) { "Cannot serialize given holder $this" }
            holder = this
        }

        fun build(): Holder<T> = holder
    }

    class FluidValueBuilder :
        ValueBuilder<Fluid>(),
        FluidAccepter

    class ItemValueBuilder :
        ValueBuilder<Item>(),
        ItemAccepter

    //    SetBuilder    //

    open class SetBuilder<T : Any> : HolderAccepter<T> {
        private var holders: MutableList<Holder<T>> = mutableListOf()

        override fun Holder<T>.unaryPlus() {
            check(this.delegate is Holder.Reference<T>) { "Cannot serialize given holder $this" }
            holders += this
        }

        fun build(): HolderSet<T> = HolderSet.direct(holders)
    }

    class FluidSetBuilder :
        SetBuilder<Fluid>(),
        FluidAccepter

    class ItemSetBuilder :
        SetBuilder<Item>(),
        ItemAccepter
}
