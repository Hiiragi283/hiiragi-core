package hiiragi283.core.api.material

import hiiragi283.core.api.collection.HTMapLike
import hiiragi283.core.api.collection.HTTableLike
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.fluid.HTFluidTagPrefix
import java.util.Comparator

/**
 * 素材に紐づいたコンテンツを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTMaterialContents {
    companion object {
        private val ENTRY_COMPARATOR: Comparator<Map.Entry<HTMaterialKey, HTIdLike>> = compareBy { it.key }

        private val TRIPLE_COMPARATOR: Comparator<Triple<Comparable<*>, HTMaterialKey, *>> =
            compareBy<Triple<Comparable<*>, HTMaterialKey, *>> { it.first }.thenComparing { it.second }
    }

    //    Block    //

    /**
     * 素材ブロックの[HTTableLike]を取得します。
     */
    fun getBlockTable(): HTTableLike<HTTagPrefix, HTMaterialKey, HTBlockHolderLike<*, *>>

    fun getBlock(prefix: HTTagPrefix, material: HTMaterialLike): HTBlockHolderLike<*, *>? =
        getBlockTable()[prefix, material.asMaterialKey()]

    fun getBlockMap(prefix: HTTagPrefix): HTMapLike<HTMaterialKey, HTBlockHolderLike<*, *>> = getBlockTable().row(prefix)

    fun getBlockMap(material: HTMaterialLike): HTMapLike<HTTagPrefix, HTBlockHolderLike<*, *>> =
        getBlockTable().column(material.asMaterialKey())

    fun getBlockEntries(): Sequence<Triple<HTTagPrefix, HTMaterialKey, HTBlockHolderLike<*, *>>> =
        getBlockTable().iterator().asSequence().sortedWith(TRIPLE_COMPARATOR)

    fun getAllBlocks(): Sequence<HTBlockHolderLike<*, *>> = getBlockEntries().map { it.third }

    //    Fluid    //

    /**
     * 素材液体の[HTTableLike]を取得します。
     */
    fun getFluidTable(): HTTableLike<HTFluidTagPrefix, HTMaterialKey, HTFluidHolderLike<*>>

    fun getFluid(prefix: HTFluidTagPrefix, material: HTMaterialLike): HTFluidHolderLike<*>? =
        getFluidTable()[prefix, material.asMaterialKey()]

    fun getFluidMap(prefix: HTFluidTagPrefix): HTMapLike<HTMaterialKey, HTFluidHolderLike<*>> = getFluidTable().row(prefix)

    fun getFluidMap(material: HTMaterialLike): HTMapLike<HTFluidTagPrefix, HTFluidHolderLike<*>> =
        getFluidTable().column(material.asMaterialKey())

    fun getFluidEntries(): Sequence<Triple<HTFluidTagPrefix, HTMaterialKey, HTFluidHolderLike<*>>> =
        getFluidTable().iterator().asSequence().sortedWith(TRIPLE_COMPARATOR)

    fun getAllFluids(): Sequence<HTFluidHolderLike<*>> = getFluidEntries().map { it.third }

    //    Item    //

    /**
     * 素材アイテムの[HTTableLike]を取得します。
     */
    fun getItemTable(): HTTableLike<HTTagPrefix, HTMaterialKey, HTItemHolderLike<*>>

    fun getItem(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? = getItemTable()[prefix, material.asMaterialKey()]

    fun getItemMap(prefix: HTTagPrefix): HTMapLike<HTMaterialKey, HTItemHolderLike<*>> = getItemTable().row(prefix)

    fun getItemMap(material: HTMaterialLike): HTMapLike<HTTagPrefix, HTItemHolderLike<*>> = getItemTable().column(material.asMaterialKey())

    fun getItemEntries(): Sequence<Triple<HTTagPrefix, HTMaterialKey, HTItemHolderLike<*>>> =
        getItemTable().iterator().asSequence().sortedWith(TRIPLE_COMPARATOR)

    fun getAllItems(): Sequence<HTItemHolderLike<*>> = getItemEntries().map { it.third }

    //    Tool    //

    /**
     * 素材ツールの[HTTableLike]を取得します。
     */
    fun getToolTable(): HTTableLike<HTToolType, HTMaterialKey, HTItemHolderLike<*>>

    fun getTool(toolType: HTToolType, material: HTMaterialLike): HTItemHolderLike<*>? = getToolTable()[toolType, material.asMaterialKey()]

    fun getToolMap(toolType: HTToolType): HTMapLike<HTMaterialKey, HTItemHolderLike<*>> = getToolTable().row(toolType)

    fun getToolMap(material: HTMaterialLike): HTMapLike<HTToolType, HTItemHolderLike<*>> = getToolTable().column(material.asMaterialKey())

    fun getToolEntries(): Sequence<Triple<HTToolType, HTMaterialKey, HTItemHolderLike<*>>> =
        getToolTable().iterator().asSequence().sortedWith(TRIPLE_COMPARATOR)

    fun getAllTools(): Sequence<HTItemHolderLike<*>> = getToolEntries().map { it.third }
}
