package hiiragi283.core.api.block

import hiiragi283.core.api.text.HTTranslation

/**
 * @see mekanism.common.block.interfaces.IHasDescription
 */
fun interface HTBlockWithDescription {
    fun getDescription(): HTTranslation
}
