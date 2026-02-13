package hiiragi283.core.common.item

import hiiragi283.core.api.item.HTThrowableItem
import hiiragi283.core.common.entity.HTThrownCaptureEgg
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class HTCaptureEggItem(properties: Properties) : HTThrowableItem(properties) {
    override fun create(level: Level, player: Player): HTThrownCaptureEgg = HTThrownCaptureEgg(level, player)

    override fun create(
        level: Level,
        x: Double,
        y: Double,
        z: Double,
    ): HTThrownCaptureEgg = HTThrownCaptureEgg(level, x, y, z)
}
