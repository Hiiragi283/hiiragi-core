package hiiragi283.lib.data

import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

/**
 * [Holder]を受け取る処理を表すインターフェースです。
 * @param T レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.1
 */
@HTBuilderMarker
interface HolderAccepter<T : Any> {
    /**
     * [Holder]を追加します。
     */
    operator fun Holder<T>.unaryPlus()

    /**
     * [Fluid]向けの[HolderAccepter]の拡張インターフェースです。
     */
    interface FluidAccepter : HolderAccepter<Fluid> {
        @Suppress("DEPRECATION")
        operator fun Fluid.unaryPlus() {
            +this.builtInRegistryHolder()
        }
    }

    /**
     * [Item]向けの[HolderAccepter]の拡張インターフェースです。
     */
    interface ItemAccepter : HolderAccepter<Item> {
        @Suppress("DEPRECATION")
        operator fun Item.unaryPlus() {
            +this.builtInRegistryHolder()
        }
    }

    //    ValueBuilder    //

    /**
     * 単一の[Holder]のみを保持する[HolderAccepter]の実装クラスです。
     */
    open class ValueBuilder<T : Any> : HolderAccepter<T> {
        private var holder: Holder<T> by HTDelegates.onceInitialize()

        override fun Holder<T>.unaryPlus() {
            check(this.delegate is Holder.Reference<T>) { "Cannot serialize given holder $this" }
            holder = this
        }

        fun build(): Holder<T> = holder
    }

    /**
     * [Fluid]向けの[ValueBuilder]の拡張クラスです。
     */
    class FluidValueBuilder :
        ValueBuilder<Fluid>(),
        FluidAccepter

    /**
     * [Item]向けの[ValueBuilder]の拡張クラスです。
     */
    class ItemValueBuilder :
        ValueBuilder<Item>(),
        ItemAccepter

    //    SetBuilder    //

    /**
     * [HolderSet]を作成する[HolderAccepter]の実装クラスです。
     */
    open class SetBuilder<T : Any> : HolderAccepter<T> {
        private var holders: MutableList<Holder<T>> = mutableListOf()

        override fun Holder<T>.unaryPlus() {
            check(this.delegate is Holder.Reference<T>) { "Cannot serialize given holder $this" }
            holders += this
        }

        fun build(): HolderSet<T> = HolderSet.direct(holders)
    }

    /**
     * [Fluid]向けの[SetBuilder]の拡張クラスです。
     */
    class FluidSetBuilder :
        SetBuilder<Fluid>(),
        FluidAccepter

    /**
     * [Item]向けの[SetBuilder]の拡張クラスです。
     */
    class ItemSetBuilder :
        SetBuilder<Item>(),
        ItemAccepter
}
