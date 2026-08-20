package hiiragi283.core.api.data.pack

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.blaze3d.platform.NativeImage
import com.mojang.logging.LogUtils
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.data.model.HTTexturedModelProvider
import hiiragi283.core.api.data.model.ModelOutput
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.modifyPath
import hiiragi283.core.api.resource.toId
import java.util.function.Supplier
import kotlin.jvm.optionals.getOrDefault
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.function.Consumers
import org.slf4j.Logger

data object HTDynamicResourceRegister {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmStatic
    fun addToData(id: ResourceLocation, json: JsonElement) {
        HTDynamicResourcePack.addToData(id.withSuffix(".json"), json.toString().toByteArray())
    }

    // Language
    @JvmStatic
    inline fun addLang(langType: HTLangType, consumer: (HTLangType, (String, String) -> Unit) -> Unit) {
        val map: MutableMap<String, String> = sortedMapOf()
        consumer(langType, map::put)
        val root = JsonObject()
        map.forEach(root::addProperty)
        addToData(HiiragiCoreAPI.MOD_ID.toId("lang", langType.name), root)
    }

    // Model
    @JvmField
    val MODEL_OUTPUT: ModelOutput = ModelOutput { id: ResourceLocation, supplier: Supplier<JsonElement> -> addToData(id.withPrefix("models/"), supplier.get()) }

    @JvmField
    val BLOCK_MODEL_GENERATOR = BlockModelGenerators(
        { generator: BlockStateGenerator -> addToData(generator.block.toLike().getId().withPrefix("blockstates/"), generator.get()) },
        MODEL_OUTPUT,
        Consumers.nop(),
    )

    @JvmStatic
    fun addModel(provider: HTTexturedModelProvider, value: HTIdLike): ResourceLocation = provider.create(value, MODEL_OUTPUT)

    @JvmStatic
    fun addBlockModel(template: ModelTemplate, block: HTIdLike, texture: TextureMapping): ResourceLocation = template.create(
        block.blockId.withSuffix(template.suffix.getOrDefault("")),
        texture,
        MODEL_OUTPUT,
    )

    @JvmStatic
    fun addItemModel(template: ModelTemplate, item: HTIdLike, texture: TextureMapping): ResourceLocation = template.create(
        item.itemId.withSuffix(template.suffix.getOrDefault("")),
        texture,
        MODEL_OUTPUT,
    )

    // Texture
    @JvmStatic
    fun addTexture(id: ResourceLocation, image: NativeImage) {
        HTDynamicResourcePack.addToData(id.modifyPath { "textures/$it.png" }, image.asByteArray())
    }
}
