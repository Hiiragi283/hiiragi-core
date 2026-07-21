package hiiragi283.core.support.gui.factory

import hiiragi283.core.api.gui.widget.HTWidgetHolder
import net.minecraft.world.entity.player.Player

interface HTWidgetHolderContext {
    fun setup(player: Player, widgetHolder: HTWidgetHolder)

    fun stillValid(player: Player): Boolean
}
