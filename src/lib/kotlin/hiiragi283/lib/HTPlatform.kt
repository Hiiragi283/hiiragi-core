package hiiragi283.lib

import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.registry.getResult
import hiiragi283.lib.registry.lookupResult
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.flatMap
import hiiragi283.lib.util.right
import hiiragi283.lib.util.toTextResult
import java.util.ServiceLoader
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

/**
 * Hiiragi LibとHiiragi Coreの橋渡しとなるクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTPlatform {
    companion object {
        /**
         * [HTPlatform]のインスタンス
         */
        @JvmField
        val INSTANCE: HTPlatform = ServiceLoader.load(HTPlatform::class.java).single()
    }

    //    Potion    //

    /**
     * [BottledPotionContents]を取得します。
     * @return 取得できなかった場合は`null`
     * @see HTPotionHelper.getContents
     */
    abstract fun <T> getContentsFromItem(instance: T): BottledPotionContents? where T : TypedInstance<Item>, T : DataComponentGetter

    /**
     * [BottledPotionContents]を取得します。
     * @return 取得できなかった場合は`null`
     * @see HTPotionHelper.getContents
     */
    abstract fun <T> getContentsFromFluid(instance: T): BottledPotionContents? where T : TypedInstance<Fluid>, T : DataComponentGetter

    /**
     * [BottledPotionContents]を設定します。
     * @see HTPotionHelper.fillFluidPatch
     */
    abstract fun fillFluidPatch(fluid: Fluid, contents: BottledPotionContents, builder: DataComponentPatch.Builder)

    /**
     * [BottledPotionContents]を設定します。
     * @see HTPotionHelper.fillItemPatch
     */
    abstract fun fillItemPatch(contents: BottledPotionContents, builder: DataComponentPatch.Builder)

    //    Tag    //

    fun <T : Any> getFirstHolder(provider: HolderLookup.Provider?, tagKey: TagKey<T>): HTTextResult<SupplierWithId<T>> {
        val provider1: HTTextResult<HolderLookup.Provider> = provider?.right() ?: HTPhysicalSideHelper.getRegistryAccess()
        return provider1.flatMap { it.lookupResult(tagKey.registry()) }.flatMap { getFirstHolder(it, tagKey) }
    }

    /**
     * タグの最初の値を取得します。
     * @param T レジストリの種類のクラス
     */
    fun <T : Any> getFirstHolder(provider: HolderLookup<T>, tagKey: TagKey<T>): HTTextResult<SupplierWithId<T>> = provider
        .getResult(tagKey)
        .flatMap { it.takeIf { it.size() > 0 }.toTextResult { "Could not find first value from empty holder set" } }
        .map(::getFirstHolder)

    /**
     * 最初の値を取得します。
     * @param T レジストリの種類のクラス
     */
    protected abstract fun <T : Any> getFirstHolder(holders: Iterable<Holder<T>>): SupplierWithId<T>
}
