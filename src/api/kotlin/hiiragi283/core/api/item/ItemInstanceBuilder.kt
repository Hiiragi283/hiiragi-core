@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.item

import hiiragi283.core.api.data.HolderAcceptor
import hiiragi283.core.api.data.buildDataPatch
import hiiragi283.core.api.util.HTBuilderMarker
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * [ItemStack]向けのビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@HTBuilderMarker
class ItemInstanceBuilder : HolderAcceptor.ItemAcceptor {
    companion object {
        /**
         * [ItemStack]を作成します。
         */
        @JvmStatic
        inline fun buildStack(builderAction: ItemInstanceBuilder.() -> Unit): ItemStack {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return ItemInstanceBuilder().apply(builderAction).run { ItemStack(item, count, patch) }
        }
    }

    @PublishedApi internal var item: Holder<Item> by HTDelegates.onceInitialize()
    var count: Int = 1

    @PublishedApi internal var patch: DataComponentPatch = DataComponentPatch.EMPTY

    override operator fun Holder<Item>.unaryPlus() {
        item = this
    }

    operator fun DataComponentPatch.unaryPlus() {
        patch = this
    }

    inline fun components(builderAction: DataComponentPatch.Builder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        patch = buildDataPatch(builderAction)
    }
}
