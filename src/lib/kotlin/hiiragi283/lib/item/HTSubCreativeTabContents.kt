package hiiragi283.lib.item

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * クリエイティブタブに複数の[ItemStack]を追加するためのインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.registration.impl.CreativeTabDeferredRegister.ICustomCreativeTabContents
 */
fun interface HTSubCreativeTabContents {
    /**
     * 複数の[ItemStack]を追加します。
     * @param baseItem 対象のアイテム
     * @param context 登録時のコンテキスト
     */
    fun addItems(baseItem: Holder<Item>, context: Context)

    /**
     * デフォルトの[ItemStack]を追加するか判定します。
     */
    fun shouldAddDefault(): Boolean = true

    //    Context    //

    /**
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    class Context(
        val enabledFeatures: FeatureFlagSet,
        val hasPermissions: Boolean,
        val provider: HolderLookup.Provider,
        output: CreativeModeTab.Output,
    ) : (ItemStack) -> Unit by output::accept {
        constructor(
            parameters: CreativeModeTab.ItemDisplayParameters,
            output: CreativeModeTab.Output,
        ) : this(parameters.enabledFeatures(), parameters.hasPermissions(), parameters.holders(), output)
    }
}
