package hiiragi283.core.api.block

import hiiragi283.core.api.text.HTTranslation
import net.minecraft.world.level.block.Block

/**
 * 説明文付きの[ブロック][Block]を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.block.interfaces.IHasDescription
 */
fun interface HTBlockWithDescription {
    /**
     * この[ブロック][Block]の説明文の[翻訳][HTTranslation]を返します。
     */
    fun getDescription(): HTTranslation
}
