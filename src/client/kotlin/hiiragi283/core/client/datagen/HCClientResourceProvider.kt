package hiiragi283.core.client.datagen

import com.google.gson.JsonObject
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.data.lang.HTLangTypes
import hiiragi283.core.api.data.texture.HTTextureUtil
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.client.datagen.HCMaterialTranslationHelper
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage
import net.minecraft.world.level.block.Blocks
import java.util.function.Consumer

data object HCClientResourceProvider : HTDynamicResourceProvider.Client(HiiragiCoreAPI.MOD_ID) {
    override fun addDynamicTranslations(afterLanguageLoadEvent: AfterLanguageLoadEvent) {}

    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        HTDynamicResourceProvider.addMaterialIds(this::addSupportedNamespaces)

        // Lang
        executor.accept { _, sink: ResourceSink -> addLang(sink, HTLangTypes.EN_US) }
        executor.accept { _, sink: ResourceSink -> addLang(sink, HTLangTypes.JA_JP) }
        // Model
        executor.accept(HCModelProvider)
        // Texture
        executor.accept(HCMaterialTextureProvider)

        executor.accept { manager, sink ->
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

    @JvmStatic
    private fun addLang(sink: ResourceSink, langType: HTLangType) {
        val root = JsonObject()
        HCMaterialTranslationHelper.addTranslations(langType) { key: HTHasTranslationKey, value: String ->
            root.addProperty(key.translationKey, value)
        }
        sink.addLang(HiiragiCoreAPI.MOD_ID.toId(langType.name), root)
    }
}
