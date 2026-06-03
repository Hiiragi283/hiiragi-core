package hiiragi283.lib.gui.menu

import net.minecraft.world.entity.player.Player

interface HTMenuCallback {
    fun openMenu(player: Player) {}

    fun closeMenu(player: Player) {}
}
