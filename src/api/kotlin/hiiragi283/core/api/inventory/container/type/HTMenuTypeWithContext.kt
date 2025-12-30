package hiiragi283.core.api.inventory.container.type

import com.mojang.logging.LogUtils
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuConstructor
import net.neoforged.neoforge.network.IContainerFactory
import org.slf4j.Logger

/**
 * 追加のデータを受け取る[HTMenuType]の拡張クラスです。
 * @param MENU [AbstractContainerMenu]を継承したクラス
 * @param C 追加のデータのクラス
 * @param clazz [C]のクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.inventory.container.type.MekanismContainerType
 */
class HTMenuTypeWithContext<MENU : AbstractContainerMenu, C>(
    private val clazz: Class<C>,
    factory: HTContainerFactory<MENU, C>,
    constructor: IContainerFactory<MENU>,
) : HTMenuType<MENU, HTContainerFactory<MENU, C>>(
        factory,
        constructor,
    ),
    HTContainerFactory<MENU, C> by factory {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    /**
     * 指定された[context]から[MenuConstructor]のインスタンスを作成します。
     */
    fun create(context: C): MenuConstructor = MenuConstructor { containerId: Int, inventory: Inventory, _: Player ->
        factory.create(containerId, inventory, context)
    }

    /**
     * 指定された[obj]から[MenuConstructor]のインスタンスを作成します。
     * @return [obj]が[C]を継承していない場合は`null`
     */
    fun createUnchecked(obj: Any): MenuConstructor? = when {
        clazz.isInstance(obj) -> {
            val context: C = clazz.cast(obj)
            create(context)
        }
        else -> {
            LOGGER.error("Failed to cast ${obj::class.java.simpleName} into ${clazz.simpleName}")
            null
        }
    }
}
