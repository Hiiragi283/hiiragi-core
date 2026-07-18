package hiiragi283.core.api.data.map

import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.resource.SimpleBlockItemSupplierWithKey
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[DataMapProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
abstract class HTDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : DataMapProvider(packOutput, lookupProvider) {
    protected lateinit var provider: HolderLookup.Provider
        private set

    final override fun gather(provider: HolderLookup.Provider) {
        this.provider = provider
        gatherInternal()
    }

    /**
     * データマップの値を登録します。
     */
    protected abstract fun gatherInternal()

    protected fun furnaceFuel(builderAction: Builder<FurnaceFuel, Item>.() -> Unit) {
        builder(NeoForgeDataMaps.FURNACE_FUELS).apply(builderAction)
    }

    //    Extensions    //

    protected fun <T : Any, R : Any> Builder<T, R>.add(holder: HTKeyLike<R>, value: T, vararg conditions: ICondition): Builder<T, R> = add(holder.getKey(), value, false, *conditions)

    protected fun <T : Any> Builder<T, Item>.add(holder: SimpleBlockItemSupplierWithKey, value: T, vararg conditions: ICondition): Builder<T, Item> = add(holder.getItemSupplier().getKey(), value, false, *conditions)
}
