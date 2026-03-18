package hiiragi283.core.api.item.alchemy

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.MutableDataComponentHolder

/**
 * 液体ポーションを管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
@Suppress("DEPRECATION")
data object HTPotionFluidManager {
    /**
     * 登録されている[液体][Fluid]の一覧
     * @since 0.13.0
     */
    @JvmStatic
    val fluidHandlers: Map<Holder<Fluid>, Handler> get() = _fluidHandlers

    @JvmStatic
    private val _fluidHandlers: MutableMap<Holder<Fluid>, Handler> = hashMapOf()

    /**
     * 指定した[fluid]に[handler]を登録します。
     * @throws IllegalStateException 指定した[fluid]が既に登録されいた場合
     */
    @JvmStatic
    fun register(fluid: Fluid, handler: Handler) {
        check(_fluidHandlers.put(fluid.builtInRegistryHolder(), handler) == null) {
            "Duplicated potion fluid registration: $fluid"
        }
    }

    /**
     * 指定した[holder]から[Handler]を取得します。
     * @return 対応する[Handler]がない場合は`null`
     */
    @JvmStatic
    fun getFluidHandler(holder: Holder<Fluid>): Handler? = _fluidHandlers[holder.delegate]

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
