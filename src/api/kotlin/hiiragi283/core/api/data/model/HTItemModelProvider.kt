package hiiragi283.core.api.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[ItemModelBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTItemModelProvider(modId: String, context: HTDataGenContext) :
    ItemModelProvider(context.output, modId, context.fileHelper) {
    protected val contents: HTMaterialContents = HiiragiCoreAccess.INSTANCE.materialContents

    //    Extensions    //

    /**
     * 指定したテクスチャが存在する場合にのみモデルを登録します。
     * @param item モデルを登録させるアイテム
     * @param action モデルを登録するブロック
     */
    protected inline fun existTexture(item: HTIdLike, action: (HTIdLike) -> Unit) {
        existTexture(item, item.itemId) { itemIn: HTIdLike, _: ResourceLocation -> action(itemIn) }
    }

    /**
     * 指定したテクスチャが存在する場合にのみモデルを登録します。
     * @param item モデルを登録させるアイテム
     * @param id テクスチャのID
     * @param action モデルを登録するブロック
     */
    protected inline fun existTexture(item: HTIdLike, id: ResourceLocation, action: (HTIdLike, ResourceLocation) -> Unit) {
        if (existingFileHelper.exists(id, TEXTURE)) {
            action(item, id)
        } else {
            HiiragiCoreAPI.LOGGER.debug("Missing texture {} for {}", id, item.getId())
        }
    }

    /**
     * @since 0.7.0
     */
    protected fun basicItem(id: HTIdLike) {
        basicItem(id.getId())
    }

    /**
     * 複数のレイヤーをもつアイテムモデルを登録します。
     * @param item モデルを登録させるアイテム
     * @param layers 各レイヤーのテクスチャID
     */
    protected fun layeredItem(item: HTIdLike, vararg layers: ResourceLocation): ItemModelBuilder {
        val builder: ItemModelBuilder = withExistingParent(item.path, HTConst.MINECRAFT.toId(HTConst.ITEM, "generated"))
        layers.forEachIndexed { index: Int, layer: ResourceLocation ->
            builder.texture("layer$index", layer)
        }
        return builder
    }

    /**
     * 液体バケツのアイテムモデルを登録します。
     * @since 0.1.0
     */
    protected fun bucketItem(content: HTFluidContent, isDrip: Boolean): DynamicFluidContainerModelBuilder<ItemModelBuilder> {
        val parent: ResourceLocation = when {
            isDrip -> "bucket_drip"
            else -> "bucket"
        }.let { HTConst.NEOFORGE.toId(HTConst.ITEM, it) }

        val builder: DynamicFluidContainerModelBuilder<ItemModelBuilder> = withExistingParent(content.bucketHolder.path, parent)
            .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
            .fluid(content.get())
        if (content.getFluidType().isLighterThanAir) {
            builder.flipGas(true)
        }
        return builder
    }

    /**
     * 素材アイテムのモデルを追加します。
     * @since 0.8.0
     */
    protected fun registerMaterials() {
        contents.getItemTable().forEach { (prefix: HTTagPrefix, _, item: HTIdLike) ->
            if (item.namespace != modid) return@forEach
            existTexture(item) { itemIn: HTIdLike ->
                val textureIcon: String = prefix[HTTagPropertyKeys.TEXTURE_ICON] ?: prefix.name
                val overlay: ResourceLocation = HiiragiCoreAPI.id(HTConst.ITEM, "${textureIcon}_overlay")
                if (existingFileHelper.exists(overlay, TEXTURE)) {
                    layeredItem(itemIn, itemIn.itemId, overlay)
                } else {
                    layeredItem(itemIn, itemIn.itemId)
                }
            }
        }
    }

    /**
     * 素材ツールのモデルを追加します。
     * @since 0.9.0
     */
    protected fun registerTools() {
        for (item: HTIdLike in contents.getAllTools()) {
            if (item.namespace != modid) continue
            existTexture(item, ::basicItem)
        }
    }
}
