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
    @JvmStatic
    private val providers: MutableMap<Holder<Fluid>, Handler> = hashMapOf()

    /**
     * 指定した[fluid]に[handler]を登録します。
     * @throws IllegalStateException 指定した[fluid]が既に登録されいた場合
     */
    @JvmStatic
    fun register(fluid: Fluid, handler: Handler) {
        check(providers.put(fluid.builtInRegistryHolder(), handler) == null) {
            "Duplicated potion fluid registration: $fluid"
        }
    }

    @JvmStatic
    fun getSupportedFluids(): Set<Holder<Fluid>> = providers.keys

    /**
     * 指定した[fluid]から[Handler]を取得します。
     * @return 対応する[Handler]がない場合は`null`
     */
    @JvmStatic
    fun getHandler(fluid: Fluid): Handler? = providers[fluid.builtInRegistryHolder()]

    //    Handler    //

    /**
     * [ポーション瓶の種類][HTBottleType]を保持するインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    interface Handler {
        operator fun get(holder: DataComponentHolder): HTBottleType

        operator fun set(holder: MutableDataComponentHolder, bottleType: HTBottleType)
    }
}
