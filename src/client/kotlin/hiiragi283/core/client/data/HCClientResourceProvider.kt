package hiiragi283.core.client.data

import com.google.gson.JsonObject
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.data.lang.HTLangTypes
import hiiragi283.core.api.data.texture.HTTextureUtil
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.api.text.HTHasTranslationKey
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.level.block.Blocks
import java.util.function.Consumer

data object HCClientResourceProvider : HTDynamicResourceProvider.Client(HiiragiCoreAPI.MOD_ID) {
    private val contents: HTMaterialContents by lazy { HiiragiCoreAccess.INSTANCE.materialContents }
    private val materialManager: HTMaterialManager by lazy { HiiragiCoreAccess.INSTANCE.materialManager }

    override fun addDynamicTranslations(afterLanguageLoadEvent: AfterLanguageLoadEvent) {}

    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        // Lang
        executor.accept { _, sink: ResourceSink -> addLang(sink, HTLangTypes.EN_US) }
        executor.accept { _, sink: ResourceSink -> addLang(sink, HTLangTypes.JA_JP) }
        // Model
        executor.accept(HCModelProvider)
        // Texture
        executor.accept(HCMaterialTextureProvider)

        executor.accept { manager: ResourceManager, sink: ResourceSink ->
            runCatching {
                val base: TextureImage = TextureImage.open(manager, HTConst.MINECRAFT.toId(HTConst.BLOCK, "lava_still.png"))
                val color: TextureImage = HTTextureUtil
                    .getTexture(manager, Blocks.MAGENTA_CONCRETE_POWDER)
                    .getOrNull()
                    ?: return@runCatching
                val respriter: Respriter = Respriter.of(base)
                val palette: Palette = Palette.fromImage(color)
                val newImage: TextureImage = respriter.recolor(palette)
                sink.addTexture(
                    HiiragiCoreAPI.id(HTConst.BLOCK, "dragon_breath"),
                    newImage,
                )
            }
        }
    }

    //    Translation    //

    @JvmStatic
    private fun addLang(sink: ResourceSink, langType: HTLangType) {
        val root = JsonObject()
        addTranslations(langType) { key: HTHasTranslationKey, value: String ->
            root.addProperty(key.translationKey, value)
        }
        sink.addLang(HiiragiCoreAPI.MOD_ID.toId(langType.name), root)
    }

    @JvmStatic
    fun addTranslations(langType: HTLangType, consumer: (HTHasTranslationKey, String) -> Unit) {
        for (entry: HTMaterialManager.Entry in materialManager) {
            // Block
            for ((prefix: HTTagPrefix, block: HTHasTranslationKey) in contents.getBlockMap(entry)) {
                val name: String = translate(langType, prefix, entry) ?: continue
                consumer(block, name)
            }
            // Item
            for ((prefix: HTTagPrefix, item: HTHasTranslationKey) in contents.getItemMap(entry)) {
                val name: String = translate(langType, prefix, entry) ?: continue
                consumer(item, name)
            }
            // Tool
            for ((toolType: HTToolType, tool: HTHasTranslationKey) in contents.getToolMap(entry)) {
                val materialName: HTLangName = entry[HTMaterialPropertyKeys.LANG_NAME] ?: continue
                consumer(tool, toolType.langPattern.translate(langType, materialName))
            }
        }
    }

    @JvmStatic
    private fun translate(type: HTLangType, prefix: HTTagPrefix, propertyMap: HTPropertyMap): String? =
        propertyMap.getOrDefault(HTMaterialPropertyKeys.CUSTOM_LANG_NAME)[prefix]?.getTranslatedName(type) ?: run {
            val materialName: HTLangName = propertyMap[HTMaterialPropertyKeys.LANG_NAME] ?: return@run null
            prefix.getOrDefault(HTTagPropertyKeys.LANG_PATTERN).translate(type, materialName)
        }
}
