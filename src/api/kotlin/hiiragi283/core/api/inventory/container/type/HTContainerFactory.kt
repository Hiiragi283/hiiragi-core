package hiiragi283.core.api.inventory.container.type

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuConstructor

/**
 * 追加のデータを引数にとる，[MenuConstructor]の代替となるインターフェースです。
 * @param MENU [AbstractContainerMenu]を継承したクラス
 * @param C 追加のデータのクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.inventory.container.type.MekanismContainerType.IMekanismContainerFactory
 */
fun interface HTContainerFactory<MENU : AbstractContainerMenu, C> {
    fun create(containerId: Int, inventory: Inventory, context: C): MENU
}
