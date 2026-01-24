package hiiragi283.core.api.data.tag

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block

/**
 * [ブロック][Block]向けの[HTTagsProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTBlockTagsProvider(modId: String, context: HTDataGenContext) : HTTagsProvider<Block>(modId, Registries.BLOCK, context) {
    //    Extensions    //

    fun HTTagBuilder<Block>.addBlock(block: Block, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<Block> =
        this.add(HTBlockHolderLike.of(block), type)

    @HTBuilderMarker
    protected fun addMaterials(factory: BuilderFactory<Block>, builderAction: (Triple<HTTagPrefix, HTMaterialKey, HTIdLike>) -> Unit) {
        contents.getBlockTable().forEach { triple ->
            val (prefix: HTTagPrefix, key: HTMaterialKey, block: HTIdLike) = triple
            if (key.namespace != modId) return@forEach
            addMaterial(factory, prefix, key).add(block)
            builderAction(triple)
        }
    }
}
