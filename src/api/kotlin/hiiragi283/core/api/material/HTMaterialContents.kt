package hiiragi283.core.api.material

import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HTTagPrefix
import java.util.Comparator

/**
 * 素材に紐づいたコンテンツを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTMaterialContents {
    companion object {
        private val COMPARATOR: Comparator<Triple<Comparable<*>, HTMaterialKey, *>> =
            compareBy<Triple<Comparable<*>, HTMaterialKey, *>> { it.first }.thenComparing { it.second }
    }

    //    Block    //

    /**
     * 素材ブロックの[HTTable]を取得します。
     */
    fun getBlockTable(): HTTable<HTTagPrefix, HTMaterialKey, out HTBlockHolderLike<*, *>>

    fun getBlock(prefix: HTTagPrefix, material: HTMaterialLike): HTBlockHolderLike<*, *>? =
        getBlockTable()[prefix, material.asMaterialKey()]

    fun getBlockOrThrow(prefix: HTTagPrefix, material: HTMaterialLike): HTBlockHolderLike<*, *> =
        getBlockTable().getOrThrow(prefix, material.asMaterialKey())

    fun getBlockMap(prefix: HTTagPrefix): Map<HTMaterialKey, HTBlockHolderLike<*, *>> = getBlockTable().row(prefix)

    fun getBlockMap(material: HTMaterialLike): Map<HTTagPrefix, HTBlockHolderLike<*, *>> = getBlockTable().column(material.asMaterialKey())

    fun getBlockEntries(): Sequence<Triple<HTTagPrefix, HTMaterialKey, HTBlockHolderLike<*, *>>> =
        getBlockTable().entries.asSequence().sortedWith(COMPARATOR)

    fun getAllBlocks(): Sequence<HTBlockHolderLike<*, *>> = getBlockEntries().map { it.third }

    //    Item    //

    /**
     * 素材アイテムの[HTTable]を取得します。
     */
    fun getItemTable(): HTTable<HTTagPrefix, HTMaterialKey, out HTItemHolderLike<*>>

    fun getItem(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? = getItemTable()[prefix, material.asMaterialKey()]

    fun getItemOrThrow(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*> =
        getItemTable().getOrThrow(prefix, material.asMaterialKey())

    fun getItemMap(prefix: HTTagPrefix): Map<HTMaterialKey, HTItemHolderLike<*>> = getItemTable().row(prefix)

    fun getItemMap(material: HTMaterialLike): Map<HTTagPrefix, HTItemHolderLike<*>> = getItemTable().column(material.asMaterialKey())

    fun getItemEntries(): Sequence<Triple<HTTagPrefix, HTMaterialKey, HTItemHolderLike<*>>> =
        getItemTable().entries.asSequence().sortedWith(COMPARATOR)

    fun getAllItems(): Sequence<HTItemHolderLike<*>> = getItemEntries().map { it.third }

    //    Tool    //

    /**
     * 素材ツールの[HTTable]を取得します。
     */
    fun getToolTable(): HTTable<HTToolType, HTMaterialKey, out HTItemHolderLike<*>>

    fun getTool(toolType: HTToolType, material: HTMaterialLike): HTItemHolderLike<*>? = getToolTable()[toolType, material.asMaterialKey()]

    fun getToolOrThrow(toolType: HTToolType, material: HTMaterialLike): HTItemHolderLike<*> =
        getTool(toolType, material) ?: error("Unknown ${toolType.name} for ${material.asMaterialId()}")

    fun getToolMap(toolType: HTToolType): Map<HTMaterialKey, HTItemHolderLike<*>> = getToolTable().row(toolType)

    fun getToolMap(material: HTMaterialLike): Map<HTToolType, HTItemHolderLike<*>> = getToolTable().column(material.asMaterialKey())

    fun getToolEntries(): Sequence<Triple<HTToolType, HTMaterialKey, HTItemHolderLike<*>>> =
        getToolTable().entries.asSequence().sortedWith(COMPARATOR)

    fun getAllTools(): Sequence<HTItemHolderLike<*>> = getToolEntries().map { it.third }
}
