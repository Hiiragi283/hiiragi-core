package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTPartManager
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import kotlin.collections.toSortedSet
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
    protected val partManager: HTPartManager by lazy(HTPartManager::getInstance)

    /**
     * 素材を管理するマネージャを取得します。
     */
    protected val materialManager: HTMaterialManager by lazy(HTMaterialManager::getInstance)

    /**
     * [TagKey]を取得します。
     * @param prefix タグのプレフィックス
     * @param key タグの種類を表す素材
     * @since 21.1.0
     */
    fun tag(prefix: HTTagPrefix, key: HTMaterialKey): TagKey<Item> = prefix.itemTagKey(key)

    /**
     * @since 0.9.0
     */
    fun baseOrPrefix(key: HTMaterialKey, prefix: HTTagPrefix): Set<TagKey<Item>> = setOfNotNull(prefix.itemTagKey(key), materialManager[key]?.getDefaultPart(key)).toSortedSet(HTComparators.TAG_KEY)

    /**
     * @since 0.9.0
     */
    fun baseOrDust(key: HTMaterialKey): Set<TagKey<Item>> = baseOrPrefix(key, CommonTagPrefixes.DUST)

    //    Delegated    //

    abstract class Delegated : HTRecipeProviderContext() {
        protected lateinit var delegate: HTRecipeProviderContext

        final override val exporter: HTRecipeExporter get() = delegate.exporter
    }
}
