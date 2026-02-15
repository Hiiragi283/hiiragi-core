package hiiragi283.core.api.data.model

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TexturedModel
import net.minecraft.resources.ResourceLocation

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[TexturedModel]の代替クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 * @see TexturedModel
 */
class HTTexturedModel(val template: ModelTemplate, val texture: TextureMapping) {
    companion object {
        /**
         * 指定された[template]と[transform]から[Provider]を作成します。
         * @param template 親となるモデル
         * @param transform IDからテクスチャに変換するブロック
         */
        @HTBuilderMarker
        @JvmStatic
        fun create(template: ModelTemplate, transform: (HTIdLike) -> TextureMapping): Provider =
            Provider { holder: HTIdLike -> HTTexturedModel(template, transform(holder)) }

        /**
         * [TexturedModel.Provider]を[Provider]に変換します。
         */
        @JvmStatic
        fun from(provider: TexturedModel.Provider) {
            Provider { holder: HTIdLike ->
                val model: TexturedModel = holder
                    .getId()
                    .let(BuiltInRegistries.BLOCK::get)
                    .let(provider::get)
                HTTexturedModel(model.template, model.mapping)
            }
        }
    }

    /**
     * 指定された[ブロック][block]からモデルを生成し，[output]に保存します。
     * @return 生成されたモデルの[ID][ResourceLocation]
     */
    fun saveBlock(block: HTIdLike, output: HTModelOutput): ResourceLocation = template.create(block.blockId, texture, output)

    fun saveBlock(block: HTIdLike, suffix: String, output: HTModelOutput): ResourceLocation =
        template.create(block.blockId.withSuffix(suffix), texture, output)

    /**
     * 指定された[アイテム][item]からモデルを生成し，[output]に保存します。
     * @return 生成されたモデルの[ID][ResourceLocation]
     */
    fun saveItem(item: HTIdLike, output: HTModelOutput): ResourceLocation = template.create(item.itemId, texture, output)

    /**
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    fun interface Provider {
        /**
         * 指定された[ID][holder]から[HTTexturedModel]を作成します。
         */
        fun getModel(holder: HTIdLike): HTTexturedModel

        /**
         * 指定された[ブロック][block]からモデルを生成し，[output]に保存します。
         * @return 生成されたモデルの[ID][ResourceLocation]
         */
        fun saveBlock(block: HTIdLike, output: HTModelOutput): ResourceLocation = getModel(block).saveBlock(block, output)

        /**
         * 指定された[アイテム][item]からモデルを生成し，[output]に保存します。
         * @return 生成されたモデルの[ID][ResourceLocation]
         */
        fun saveItem(item: HTIdLike, output: HTModelOutput): ResourceLocation = getModel(item).saveItem(item, output)
    }
}
