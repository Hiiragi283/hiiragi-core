package hiiragi283.core.api.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.texture.HTTextureUtil
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.resource.toId
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicClientResourceProvider
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicServerResourceProvider
import net.mehvahdjukaar.moonlight.api.resources.pack.PackGenerationStrategy
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
object HTDynamicResourceProvider {
    /**
     * クライアント側での動的リソースを提供する抽象クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    abstract class Client(modId: String) :
        DynamicClientResourceProvider(modId.toId("dynamic_resources"), PackGenerationStrategy.REGEN_ON_EVERY_RELOAD) {
        final override fun gatherSupportedNamespaces(): Collection<String> = buildSet {
            this += HTConst.MINECRAFT
            this += HTConst.COMMON
            this += HTConst.NEOFORGE
        }

        //    Extensions    //

        protected fun resprite(
            id: ResourceLocation,
            base: ResourceLocation,
            paletteFactory: (ResourceManager) -> Result<Palette>,
        ): ResourceGenTask = ResourceGenTask { manager: ResourceManager, sink: ResourceSink ->
            val palette: Palette = paletteFactory(manager).getOrNull() ?: return@ResourceGenTask
            runCatching { TextureImage.open(manager, base) }
                .map(Respriter::of)
                .map { it.recolor(palette) }
                .onSuccess { sink.addTexture(id, it) }
        }

        protected fun resprite(id: ResourceLocation, base: ResourceLocation, key: HTMaterialKey): ResourceGenTask =
            resprite(id, base, HTTextureUtil::getOrCreatePalette.partially1(key.getId()))

        protected fun resprite(id: ResourceLocation, base: ResourceLocation, palette: Block): ResourceGenTask =
            resprite(id, base) { manager: ResourceManager -> HTTextureUtil.getTexture(manager, palette).map(Palette::fromImage) }

        protected fun resprite(id: ResourceLocation, base: ResourceLocation, palette: Item): ResourceGenTask =
            resprite(id, base) { manager: ResourceManager -> HTTextureUtil.getTexture(manager, palette).map(Palette::fromImage) }
    }

    /**
     * サーバー側での動的リソースを提供する抽象クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    abstract class Server(modId: String) :
        DynamicServerResourceProvider(modId.toId("dynamic_resources"), PackGenerationStrategy.REGEN_ON_EVERY_RELOAD) {
        final override fun gatherSupportedNamespaces(): Collection<String> = buildSet {
            this += HTConst.MINECRAFT
            this += HTConst.COMMON
            this += HTConst.NEOFORGE
        }
    }
}
