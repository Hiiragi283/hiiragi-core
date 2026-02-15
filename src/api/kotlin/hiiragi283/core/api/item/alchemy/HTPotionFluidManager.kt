package hiiragi283.core.api.item.alchemy

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.fluids.FluidStack

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
     * 指定した[液体][stack]から[PotionContents]を取得します。
     */
    @JvmStatic
    fun getContents(stack: FluidStack): PotionContents = HTPotionHelper.getPotion(stack)

    /**
     * 指定した[液体][fluid]から[Handler]を取得し，[holder]から[HTBottleType]を取得します。
     */
    @JvmStatic
    fun getBottleType(fluid: Fluid, holder: DataComponentHolder): HTBottleType =
        providers[fluid.builtInRegistryHolder()]?.get(holder) ?: HTBottleType.DEFAULT

    /**
     * 指定した[液体][stack]から[HTBottleType]を取得します。
     */
    @JvmStatic
    fun getBottleType(stack: FluidStack): HTBottleType = providers[stack.fluidHolder]?.get(stack) ?: HTBottleType.DEFAULT

    /**
     * 指定した[液体][fluid]から[Handler]を取得し，[holder]に[ポーション瓶の種類][bottleType]を設定します。
     */
    @JvmStatic
    fun setBottleType(fluid: Fluid, holder: MutableDataComponentHolder, bottleType: HTBottleType) {
        providers[fluid.builtInRegistryHolder()]?.set(holder, bottleType)
    }

    /**
     * 指定した[液体][stack]に[ポーション瓶の種類][bottleType]を設定します。
     */
    @JvmStatic
    fun setBottleType(stack: FluidStack, bottleType: HTBottleType) {
        providers[stack.fluidHolder]?.set(stack, bottleType)
    }

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
