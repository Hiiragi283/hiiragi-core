package hiiragi283.core.api

import net.minecraft.Util
import kotlin.enums.enumEntries

inline fun <reified V : Enum<V>> V.nextEntry(): V = Util.findNextInIterable(enumEntries<V>(), this)

inline fun <reified V : Enum<V>> V.previousEntry(): V = Util.findPreviousInIterable(enumEntries<V>(), this)
