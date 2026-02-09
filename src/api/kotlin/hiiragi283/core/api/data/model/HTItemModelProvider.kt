package hiiragi283.core.api.data.model

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import java.util.function.Supplier

abstract class HTItemModelProvider : ResourceGenTask {
    private lateinit var sink: ResourceSink

    final override fun accept(manager: ResourceManager, sink: ResourceSink) {
        this.sink = sink
        registerModels(manager)
    }

    protected abstract fun registerModels(manager: ResourceManager)

    //    Extensions    //

    // Block
    protected fun addBlockModel(block: HTIdLike, model: ModelTemplate, textureMap: TextureMapping) {
        model.create(block.getId(), textureMap) { id: ResourceLocation, json: Supplier<JsonElement> ->
            sink.addBlockModel(id, json.get())
        }
    }

    // Item
    protected fun addItemModel(item: HTIdLike, model: ModelTemplate, textureMap: TextureMapping) {
        model.create(item.getId(), textureMap) { id: ResourceLocation, json: Supplier<JsonElement> ->
            sink.addItemModel(id, json.get())
        }
    }

    protected fun addSimpleItemModel(item: HTIdLike) {
        addItemModel(item, ModelTemplates.FLAT_ITEM, TextureMapping.layer0(item.itemId))
    }

    protected fun addLayeredItemModel(item: HTIdLike, vararg layers: ResourceLocation) {
        addLayeredItemModel(item, listOf(*layers))
    }

    protected fun addLayeredItemModel(item: HTIdLike, layers: List<ResourceLocation>) {
        when (layers.size) {
            1 -> addItemModel(item, ModelTemplates.FLAT_ITEM, TextureMapping.layer0(layers[0]))
            2 -> addItemModel(item, ModelTemplates.TWO_LAYERED_ITEM, TextureMapping.layered(layers[0], layers[1]))
            3 -> addItemModel(item, ModelTemplates.THREE_LAYERED_ITEM, TextureMapping.layered(layers[0], layers[1], layers[2]))
            else -> error("Unsupported layer count: ${layers.size}")
        }
    }

    /**
     * @see net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder
     */
    protected fun addBucketModel(content: HTFluidContent, isDrip: Boolean) {
        val parent: ResourceLocation = when {
            isDrip -> "bucket_drip"
            else -> "bucket"
        }.let { HTConst.NEOFORGE.toId(HTConst.ITEM, it) }
        val root = JsonObject()
        root.addProperty("parent", parent.toString())
        root.addProperty("fluid", content.getId().toString())
        root.addProperty("loader", "neoforge:fluid_container")
        if (content.getFluidType().isLighterThanAir) {
            root.addProperty("flip_gas", "true")
        }
        sink.addItemModel(content.bucketHolder.getId(), root)
    }
}
