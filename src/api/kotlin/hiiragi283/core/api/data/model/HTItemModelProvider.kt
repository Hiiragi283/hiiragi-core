package hiiragi283.core.api.data.model

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.allOf
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.ResourceLocation

/**
 * Hiiragi Seriesで使用される，アイテムモデルを生成する[DataProvider]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
abstract class HTItemModelProvider(output: PackOutput, protected val modId: String) : DataProvider {
    private val modelPathProvider: PackOutput.PathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models")

    override fun run(output: CachedOutput): CompletableFuture<*> {
        val map: MutableMap<ResourceLocation, Supplier<JsonElement>> = hashMapOf()
        registerModels { modelId: ResourceLocation, supplier: Supplier<JsonElement> ->
            check(map.put(modelId, supplier) == null) { "Duplicate model definition for $modelId" }
        }
        return map
            .map { (modelId: ResourceLocation, supplier: Supplier<JsonElement>) -> DataProvider.saveStable(output, supplier.get(), modelPathProvider.json(modelId)) }
            .allOf()
    }

    /**
     * モデルを登録します。
     */
    protected abstract fun registerModels(output: ModelOutput)

    override fun getName(): String = "Item Models - $modId"

    //    Extensions    //

    /**
     * 単純なアイテムモデルを登録します。
     * @param output モデルの出力先
     * @param item モデルを生成するアイテム
     */
    fun basicItem(output: ModelOutput, item: HTIdLike): ResourceLocation = ModelTemplates.FLAT_ITEM.create(item.itemId, TextureMapping.layer0(item.itemId), output)

    /**
     * 複数のレイヤーをもつ単純なアイテムモデルを登録します。
     * @param output モデルの出力先
     * @param item モデルを生成するアイテム
     * @param layers テクスチャのレイヤー
     */
    fun layeredItem(output: ModelOutput, item: HTIdLike, vararg layers: ResourceLocation): ResourceLocation {
        val (mapping: TextureMapping, template: ModelTemplate) = when (layers.size) {
            1 -> TextureMapping.layer0(layers[0]) to ModelTemplates.FLAT_ITEM
            2 -> TextureMapping.layered(layers[0], layers[1]) to ModelTemplates.TWO_LAYERED_ITEM
            3 -> TextureMapping.layered(layers[0], layers[1], layers[2]) to ModelTemplates.THREE_LAYERED_ITEM
            else -> error("Cannot create item model with ${layers.size} layers")
        }
        return template.create(item.itemId, mapping, output)
    }

    /**
     * 液体入りバケツのアイテムモデルを登録します。
     * @param output モデルの出力先
     * @param content 液体バケツを提供するコンテンツ
     * @param isDrip `true`の場合，溶岩バケツのようなテクスチャを使用します
     */
    fun bucketItem(output: ModelOutput, content: HTFluidContent, isDrip: Boolean): ResourceLocation {
        val parent: ResourceLocation = when {
            isDrip -> "bucket_drip"
            else -> "bucket"
        }.let { HTConst.NEOFORGE.toId(HTConst.ITEM, it) }
        val modelId: ResourceLocation = content.bucketHolder.itemId
        output.accept(modelId) {
            JsonObject().apply {
                addProperty("parent", parent.toString())
                addProperty("loader", HTConst.NEOFORGE.toId("fluid_container").toString())
                addProperty("fluid", content.getId().toString())
                if (content.getFluidType().isLighterThanAir) {
                    addProperty("flip_gas", true)
                }
            }
        }
        return modelId
    }
}
