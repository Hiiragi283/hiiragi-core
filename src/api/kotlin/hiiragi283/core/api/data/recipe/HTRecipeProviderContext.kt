package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.getResult
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import kotlin.collections.toSortedSet
import net.minecraft.core.HolderSet
import net.minecraft.data.DataProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

abstract class HTRecipeProviderContext {
    /**
     * レシピの出力先
     */
    abstract val exporter: HTRecipeExporter

    //    Extensions    //

    fun getHasName(id: HTIdLike): String = "has_${id.path}"

    fun getHasName(tagKey: TagKey<*>): String = "has_${tagKey.location().path.replace("/", "_")}"

    // Material
    /**
     * 部品を管理するマネージャを取得します。
     * @since 0.12.0
     */
    protected val partManager by lazy(HiiragiCoreAccess.INSTANCE::partManager)

    /**
     * 素材を管理するマネージャを取得します。
     */
    protected val materialManager: HTMaterialManager by lazy(HTMaterialManager::getInstance)

    protected inline fun useItem(part: HTPartLike, material: HTMaterialLike, action: (HTMaterialContents.ItemEntry) -> Unit) {
        HiiragiCoreAccess.INSTANCE
            .registeredContents
            .items
            .getResult(part, material)
            .onLeft { DataProvider.LOGGER.error(it.value) }
            .onRight(action)
    }

    /**
     * [HolderSet]を取得します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     */
    fun tag(prefix: HTTagPrefix, material: HTMaterialLike): TagKey<Item> = prefix.itemTagKey(material)

    /**
     * @since 0.9.0
     */
    fun baseOrPrefix(material: HTMaterialLike, prefix: HTTagPrefix): Set<TagKey<Item>> = setOfNotNull(prefix.itemTagKey(material), materialManager.getOrEmpty(material).getDefaultPart(material)).toSortedSet(HTComparators.TAG_KEY)

    /**
     * @since 0.9.0
     */
    fun baseOrDust(material: HTMaterialLike): Set<TagKey<Item>> = baseOrPrefix(material, CommonTagPrefixes.DUST)

    //    Delegated    //

    abstract class Delegated : HTRecipeProviderContext() {
        protected lateinit var delegate: HTRecipeProviderContext

        final override val exporter: HTRecipeExporter get() = delegate.exporter
    }
}
