package hiiragi283.core.api.data.model

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.vanillaId
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import org.slf4j.Logger

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[ItemModelBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTItemModelProvider(modId: String, context: HTDataGenContext) :
    ItemModelProvider(context.output, modId, context.fileHelper) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    //    Extensions    //

    /**
     * 指定したテクスチャが存在する場合にのみモデルを登録します。
     * @param item モデルを登録させるアイテム
     * @param action モデルを登録するブロック
     */
    protected inline fun existTexture(item: HTIdLike, action: (ResourceLocation) -> Unit) {
        existTexture(item, item.itemId) { itemIn: HTIdLike, _: ResourceLocation -> action(itemIn.getId()) }
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
            LOGGER.debug("Missing texture {} for {}", id, item.getId())
        }
    }

    /**
     * 複数のレイヤーをもつアイテムモデルを登録します。
     * @param item モデルを登録させるアイテム
     * @param layers 各レイヤーのテクスチャID
     */
    protected fun layeredItem(item: HTIdLike, vararg layers: ResourceLocation): ItemModelBuilder {
        val builder: ItemModelBuilder = withExistingParent(item.getPath(), vanillaId(HTConst.ITEM, "generated"))
        layers.forEachIndexed { index: Int, layer: ResourceLocation ->
            builder.texture("layer$index", layer)
        }
        return builder
    }
}
