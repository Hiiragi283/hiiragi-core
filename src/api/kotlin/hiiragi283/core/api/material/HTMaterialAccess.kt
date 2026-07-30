package hiiragi283.core.api.material

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike

/**
 * 部品と素材に対応するブロックやアイテムを管理するクラスです。
 * @param blocks [HTPart]に対応する素材ブロックの一覧
 * @param items [HTPart]に対応する素材アイテムの一覧
 * @param tools [HTToolType]に対応する素材ツールの一覧
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
@JvmRecord
data class HTMaterialAccess(
    val blocks: HTMaterialContents<HTPart, HTMaterialContents.BlockEntry>,
    val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry>,
    val tools: HTMaterialContents<HTToolType, HTMaterialContents.ItemEntry>,
) {
    /**
     * 指定した部品と素材に対応する素材アイテムを取得します。
     * @return 対応するアイテムがない場合は`null`
     */
    fun getBlockOrItem(part: HTPartLike, key: HTMaterialKey): HTMaterialContents.ItemEntry? = blocks[part, key]?.let { HTMaterialContents.ItemEntry(it.getItemSupplier(), it.isBuiltIn) } ?: items[part, key]
}
