package hiiragi283.core.common.item

import hiiragi283.core.common.entity.HTThrownBomb
import hiiragi283.core.support.item.HTThrowableItem
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class HTBombItem(properties: Properties) : HTThrowableItem(properties) {
    override fun create(level: Level, player: Player): HTThrownBomb = HTThrownBomb(level, player)

    override fun create(
        level: Level,
        x: Double,
        y: Double,
        z: Double,
    ): HTThrownBomb = HTThrownBomb(level, x, y, z)
}
