package hiiragi283.core.api

import net.minecraft.Util
import kotlin.enums.enumEntries

/**
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
inline fun <reified V : Enum<V>> V.nextEntry(): V = Util.findNextInIterable(enumEntries<V>(), this)

/**
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
inline fun <reified V : Enum<V>> V.previousEntry(): V = Util.findPreviousInIterable(enumEntries<V>(), this)
