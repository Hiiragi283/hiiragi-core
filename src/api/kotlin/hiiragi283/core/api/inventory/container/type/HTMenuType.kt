package hiiragi283.core.api.inventory.container.type

import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.network.IContainerFactory

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[MenuType]の拡張クラスです。
 * @param MENU [AbstractContainerMenu]を継承したクラス
 * @param FACTORY [MENU]を作成するブロックのクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see HTMenuTypeWithContext
 * @see HTItemMenuType
 * @see mekanism.common.inventory.container.type.BaseMekanismContainerType
 */
abstract class HTMenuType<MENU : AbstractContainerMenu, FACTORY>(protected val factory: FACTORY, constructor: IContainerFactory<MENU>) :
    MenuType<MENU>(constructor, FeatureFlags.VANILLA_SET)
