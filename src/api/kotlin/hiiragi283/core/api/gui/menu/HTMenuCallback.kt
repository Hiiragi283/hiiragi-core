package hiiragi283.core.api.gui.menu

import net.minecraft.world.entity.player.Player

/**
 * GUIが開かれる/閉じられる時に呼び出されるインターフェース
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTMenuCallback {
    fun openMenu(player: Player) {}

    fun closeMenu(player: Player) {}
}
