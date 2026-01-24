package hiiragi283.core.common.gui.factory

import hiiragi283.core.api.gui.widget.HTWidgetHolder
import net.minecraft.world.entity.player.Player

/**
 * @see com.lowdragmc.lowdraglib2.gui.factory.IContainerUIHolder
 */
interface HTWidgetHolderContext {
    fun setup(player: Player, widgetHolder: HTWidgetHolder)

    fun stillValid(player: Player): Boolean
}
