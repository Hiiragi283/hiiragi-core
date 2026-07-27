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

    protected abstract fun registerModels(output: ModelOutput)

    override fun getName(): String = "Item Models - $modId"

    //    Extensions    //

    fun basicItem(output: ModelOutput, item: HTIdLike): ResourceLocation = ModelTemplates.FLAT_ITEM.create(item.itemId, TextureMapping.layer0(item.itemId), output)

    fun layeredItem(output: ModelOutput, item: HTIdLike, vararg layers: ResourceLocation): ResourceLocation {
        val (mapping: TextureMapping, template: ModelTemplate) = when (layers.size) {
            1 -> TextureMapping.layer0(layers[0]) to ModelTemplates.FLAT_ITEM
            2 -> TextureMapping.layered(layers[0], layers[1]) to ModelTemplates.TWO_LAYERED_ITEM
            3 -> TextureMapping.layered(layers[0], layers[1], layers[2]) to ModelTemplates.THREE_LAYERED_ITEM
            else -> error("Cannot create item model with ${layers.size} layers")
        }
        return template.create(item.itemId, mapping, output)
    }

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
