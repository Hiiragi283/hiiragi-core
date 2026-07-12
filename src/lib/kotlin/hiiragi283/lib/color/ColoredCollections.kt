package hiiragi283.lib.color

import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.ColorCollection

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
fun <T : Any> ColorCollection(init: (color: HTDefaultColor) -> T): ColorCollection<T> = HTDefaultColor.COLLECTION.map(init)

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
operator fun <T : Any> ColorCollection<T>.get(color: DyeColor): T = this.pick(color)

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
operator fun <T : Any> ColorCollection<T>.get(color: HTDefaultColor): T = this[color.dyeColor]
