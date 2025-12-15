package hiiragi283.core.api.block

import hiiragi283.core.api.block.type.HTBlockType

/**
 * [HTBlockType]を保持するブロックを表すインターフェース
 * @see mekanism.common.block.interfaces.ITypeBlock
 */
interface HTBlockWithType {
    fun type(): HTBlockType
}
