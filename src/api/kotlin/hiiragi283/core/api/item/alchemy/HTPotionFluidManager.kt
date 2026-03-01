package hiiragi283.core.api.item.alchemy

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.MutableDataComponentHolder

/**
 * 液体ポーションを管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
@Suppress("DEPRECATION")
data object HTPotionFluidManager {
    @JvmStatic
    private val fluidHandlers: MutableMap<Holder<Fluid>, Handler> = hashMapOf()

    @JvmStatic
    private val itemHandlers: MutableMap<Holder<Item>, Handler> = hashMapOf()

    /**
     * 指定した[fluid]に[handler]を登録します。
     * @throws IllegalStateException 指定した[fluid]が既に登録されいた場合
     */
    @JvmStatic
    fun register(fluid: Fluid, handler: Handler) {
        check(fluidHandlers.put(fluid.builtInRegistryHolder(), handler) == null) {
            "Duplicated potion fluid registration: $fluid"
        }
    }

    /**
     * 登録されている[液体][Fluid]の一覧を取得します。
     * @return [Holder]の一覧
     */
    @JvmStatic
    fun getSupportedFluids(): Set<Holder<Fluid>> = fluidHandlers.keys

    /**
     * 指定した[holder]から[Handler]を取得します。
     * @return 対応する[Handler]がない場合は`null`
     */
    @JvmStatic
    fun getFluidHandler(holder: Holder<Fluid>): Handler? = fluidHandlers[holder.delegate]

    /**
     * 指定した[item]に[handler]を登録します。
     * @throws IllegalStateException 指定した[item]が既に登録されいた場合
     * @since 0.12.0
     */
    @JvmStatic
    fun register(item: Item, handler: Handler) {
        check(itemHandlers.put(item.builtInRegistryHolder(), handler) == null) {
            "Duplicated potion item registration: $item"
        }
    }

    /**
     * 登録されている[アイテム][Item]の一覧を取得します。
     * @return [Holder]の一覧
     * @since 0.12.0
     */
    @JvmStatic
    fun getSupportedItems(): Set<Holder<Item>> = itemHandlers.keys

    /**
     * 指定した[holder]から[Handler]を取得します。
     * @return 対応する[Handler]がない場合は`null`
     * @since 0.12.0
     */
    @JvmStatic
    fun getItemHandler(holder: Holder<Item>): Handler? = itemHandlers[holder.delegate]

    //    Handler    //

    /**
     * [ポーション瓶の種類][HTBottleType]を保持するインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    interface Handler {
        operator fun get(holder: DataComponentHolder): HTBottleType?

        operator fun set(holder: MutableDataComponentHolder, bottleType: HTBottleType)
    }
}
