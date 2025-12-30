package hiiragi283.core.api.inventory.container.type

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.neoforge.network.IContainerFactory

/**
 * [アイテム][ItemStack]を受け取る[HTMenuType]の拡張クラスです。
 * @param MENU [AbstractContainerMenu]を継承したクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.inventory.container.type.MekanismItemContainerType
 */
class HTItemMenuType<MENU : AbstractContainerMenu>(factory: HTItemContainerFactory<MENU>, constructor: IContainerFactory<MENU>) :
    HTMenuType<MENU, HTItemContainerFactory<MENU>>(factory, constructor) {
    /**
     * 指定された[hand]と[stack]から[MenuConstructor]のインスタンスを作成します。
     * @return [ItemStack.isEmpty]の場合は`null`
     */
    fun create(hand: InteractionHand?, stack: ItemStack): MenuConstructor? {
        if (stack.isEmpty) return null
        return MenuConstructor { containerId: Int, inventory: Inventory, _: Player ->
            factory.create(containerId, inventory, hand, stack, Dist.DEDICATED_SERVER)
        }
    }
}
