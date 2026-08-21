package hiiragi283.core.api.item.alchemy

import hiiragi283.core.api.item.alchemy.HTPotionFluidManager.Handler
import hiiragi283.core.api.serialization.component.DataComponentGetter
import hiiragi283.core.api.serialization.component.DataComponentSetter
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
    val fluidHandlers: Map<Fluid, Handler>field: MutableMap<Fluid, Handler> = hashMapOf()

    /**
     * 指定した[fluid]に[handler]を登録します。
     * @throws IllegalStateException 指定した[fluid]が既に登録されいた場合
     */
    @JvmStatic
    fun register(fluid: Fluid, handler: Handler) {
        check(fluidHandlers.put(fluid, handler) == null) {
            "Duplicated potion fluid registration: $fluid"
        }
    }

    /**
     * 指定した[holder]から[Handler]を取得します。
     * @return 対応する[Handler]がない場合は`null`
     */
    @JvmStatic
    fun getHandler(holder: Fluid): Handler? = fluidHandlers[holder]

    /**
     * 指定した[holder]から[Handler]を取得します。
     * @return 対応する[Handler]がない場合は[Handler.DEFAULT]
     * @since 21.1.1.0
     */
    @JvmStatic
    fun getHandlerOrDefault(holder: Fluid): Handler = getHandler(holder) ?: Handler.DEFAULT

    //    Handler    //

    /**
     * [ポーション瓶の種類][HTBottleType]を保持するインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    interface Handler {
        companion object {
            @JvmField
            val DEFAULT: Handler = object : Handler {
                override fun get(getter: DataComponentGetter): HTBottleType? = getter[HTPotionAccess.INSTANCE.bottleTypeComponent]

                override fun set(setter: DataComponentSetter, bottleType: HTBottleType) {
                    setter[HTPotionAccess.INSTANCE.bottleTypeComponent] = bottleType
                }
            }
        }

        operator fun get(getter: DataComponentGetter): HTBottleType?

        operator fun set(setter: DataComponentSetter, bottleType: HTBottleType)

        operator fun get(holder: DataComponentHolder): HTBottleType? = get(DataComponentGetter(holder))

        operator fun set(holder: MutableDataComponentHolder, bottleType: HTBottleType) {
            set(DataComponentSetter(holder), bottleType)
        }
    }
}
