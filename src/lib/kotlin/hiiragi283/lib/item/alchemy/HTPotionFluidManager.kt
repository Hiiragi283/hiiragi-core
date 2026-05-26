package hiiragi283.lib.item.alchemy

import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.MutableDataComponentHolder

/**
 * 液体ポーションを管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
data object HTPotionFluidManager {
    /**
     * 登録されている[液体][Fluid]の一覧
     * @since 0.13.0
     */
    @JvmStatic
    val handlers: Map<Fluid, Handler> get() = _handlers

    @JvmStatic
    private val _handlers: MutableMap<Fluid, Handler> = hashMapOf()

    /**
     * 指定した[fluid]に[handler]を登録します。
     * @throws IllegalStateException 指定した[fluid]が既に登録されいた場合
     */
    @JvmStatic
    fun register(fluid: Fluid, handler: Handler) {
        check(_handlers.put(fluid, handler) == null) { "Duplicated potion fluid registration: $fluid" }
    }

    /**
     * 指定した[fluid]から[Handler]を取得します。
     * @return 対応する[Handler]がない場合は`null`
     */
    @JvmStatic
    fun getFluidHandler(fluid: Fluid): Handler? = _handlers[fluid]

    //    Handler    //

    /**
     * [ポーション瓶の種類][HTBottleType]を保持するインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    interface Handler {
        operator fun get(getter: DataComponentGetter): HTBottleType?

        operator fun set(holder: MutableDataComponentHolder, bottleType: HTBottleType)

        operator fun set(builder: DataComponentMap.Builder, bottleType: HTBottleType)
    }
}
