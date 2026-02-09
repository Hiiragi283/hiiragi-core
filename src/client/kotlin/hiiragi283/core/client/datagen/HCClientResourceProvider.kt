package hiiragi283.core.client.datagen

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.api.data.texture.HTTextureUtil
import hiiragi283.core.api.resource.toId
import hiiragi283.core.client.datagen.lang.HCEnglishLangProvider
import hiiragi283.core.client.datagen.lang.HCJapaneseLangProvider
import hiiragi283.core.client.datagen.model.HCModelProvider
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
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
        executor.accept(HCEnglishLangProvider)
        executor.accept(HCJapaneseLangProvider)
        // Model
        executor.accept(HCModelProvider)
        // Texture
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
}
