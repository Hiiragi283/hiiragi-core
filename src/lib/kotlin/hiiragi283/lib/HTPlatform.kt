package hiiragi283.lib

import hiiragi283.lib.item.alchemy.BottledPotionContents
import java.util.ServiceLoader
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.registry.getResult
import hiiragi283.lib.registry.lookupResult
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.flatMap
import hiiragi283.lib.util.right
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.tags.TagKey

abstract class HTPlatform {
    companion object {
        @JvmField
        val INSTANCE: HTPlatform = ServiceLoader.load(HTPlatform::class.java).single()
    }

    //    Potion    //

    /**
     * 指定した[instance]から[BottledPotionContents]を取得します。
     * @return 取得できなかった場合は`null`
     * @since 0.11.0
     * @see HTPotionHelper.getContents
     */
    abstract fun <T> getContentsFromItem(instance: T): BottledPotionContents? where T : TypedInstance<Item>, T : DataComponentGetter

    abstract fun <T> getContentsFromFluid(instance: T): BottledPotionContents? where T : TypedInstance<Fluid>, T : DataComponentGetter

    /**
     * 指定した[stack]に[BottledPotionContents]を設定します。
     * @since 0.11.0
     * @see HTPotionHelper.setContents
     */
    abstract fun setContents(stack: FluidStack, contents: BottledPotionContents)

    abstract fun setContents(stack: ItemStack, contents: BottledPotionContents)

    //    Tag    //

    /**
     * 指定した[provider]から，[tagKey]に紐づいた値を取得します。
     * @param T レジストリの種類のクラス
     * @return [SupplierWithId]の[結果][HTTextResult]
     */
    fun <T : Any> getFirstHolder(provider: HolderLookup.Provider?, tagKey: TagKey<T>): HTTextResult<SupplierWithId<T>> {
        val provider1: HTTextResult<HolderLookup.Provider> = provider?.right() ?: HTPhysicalSideHelper.getRegistryAccess()
        return provider1.flatMap { it.lookupResult(tagKey.registry()) }.flatMap { getFirstHolder(it, tagKey) }
    }

    /**
     * 指定した[provider]から，[tagKey]に紐づいた値を取得します。
     * @param T レジストリの種類のクラス
     * @return [SupplierWithId]の[結果][HTTextResult]
     * @since 0.17.0
     */
    fun <T : Any> getFirstHolder(provider: HolderLookup<T>, tagKey: TagKey<T>): HTTextResult<SupplierWithId<T>> = provider
        .getResult(tagKey)
        .flatMap {
            when (it.size()) {
                0 -> HTTextResult("Could not find first value from empty holder set")
                else -> it.right()
            }
        }
        .map(::getFirstHolder)

    /**
     * 指定した[holders]から，最初の値を取得します。
     * @param T レジストリの種類のクラス
     * @since 0.15.2
     */
    protected abstract fun <T : Any> getFirstHolder(holders: Iterable<Holder<T>>): SupplierWithId<T>
}
