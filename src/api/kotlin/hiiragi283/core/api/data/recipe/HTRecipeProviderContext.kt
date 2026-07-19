package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.util.Identity
import hiiragi283.core.api.util.getOrThrow
import hiiragi283.core.api.util.identity
import kotlin.collections.toSortedSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

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

    /**
     * [TagKey]を取得します。
     * @param prefix タグのプレフィックス
     * @param material タグの種類を表す素材
     * @since 21.1.0
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

    /**
     *@since 21.1.0
     */
    fun tag(part: HTFluidPart, material: HTMaterialLike): TagKey<Fluid> = part.createTagKey(material)

    /**
     *@since 21.1.0
     */
    fun fluid(part: HTFluidPart, material: HTMaterialLike, operator: Identity<Int> = identity()): HTFluidResult = HTFluidResultBuilder().apply {
        +HiiragiCoreAccess.INSTANCE.registeredFluids.getResult(part, material).getOrThrow()
        amount = materialManager.getOrEmpty(material).getDefaultFluidAmount().let(operator)
    }.build()

    /**
     *@since 21.1.0
     */
    fun molten(material: HTMaterialLike, operator: Identity<Int> = identity()): HTFluidResult = fluid(HTFluidPart.MOLTEN, material, operator)

    //    Delegated    //

    abstract class Delegated : HTRecipeProviderContext() {
        protected lateinit var delegate: HTRecipeProviderContext

        final override val exporter: HTRecipeExporter get() = delegate.exporter
    }
}
