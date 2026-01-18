package hiiragi283.core.api.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.ImmutableTable
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HTTagPrefix
import java.util.Comparator

interface HTMaterialContentsAccess {
    companion object {
        /**
         * [HTMaterialContentsAccess]のインスタンス
         */
        val INSTANCE: HTMaterialContentsAccess = HiiragiCoreAPI.getService()

        private val COMPARATOR: Comparator<Triple<HTTagPrefix, HTMaterialKey, *>> =
            compareBy<Triple<HTTagPrefix, HTMaterialKey, *>> { it.first }.thenComparing { it.second }
    }

    //    Block    //

    fun getBlockTable(): ImmutableTable<HTTagPrefix, HTMaterialKey, out HTBlockHolderLike<*, *>>

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

    fun getItemTable(): ImmutableTable<HTTagPrefix, HTMaterialKey, out HTItemHolderLike<*>>

    fun getItem(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*>? = getItemTable()[prefix, material.asMaterialKey()]

    fun getItemOrThrow(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*> =
        getItemTable().getOrThrow(prefix, material.asMaterialKey())

    fun getItemMap(prefix: HTTagPrefix): Map<HTMaterialKey, HTItemHolderLike<*>> = getItemTable().row(prefix)

    fun getItemMap(material: HTMaterialLike): Map<HTTagPrefix, HTItemHolderLike<*>> = getItemTable().column(material.asMaterialKey())

    fun getItemEntries(): Sequence<Triple<HTTagPrefix, HTMaterialKey, HTItemHolderLike<*>>> =
        getItemTable().entries.asSequence().sortedWith(COMPARATOR)

    fun getAllItems(): Sequence<HTItemHolderLike<*>> = getItemEntries().map { it.third }
}
