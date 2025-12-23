package hiiragi283.core.api.item

import hiiragi283.core.api.registry.HTItemHolderLike
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer

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
     * @param parameters 登録時のコンテキスト
     * @param consumer [ItemStack]の登録先
     */
    fun addItems(baseItem: HTItemHolderLike<*>, parameters: CreativeModeTab.ItemDisplayParameters, consumer: Consumer<ItemStack>)

    /**
     * デフォルトの[ItemStack]を追加するか判定します。
     */
    fun shouldAddDefault(): Boolean = true
}
