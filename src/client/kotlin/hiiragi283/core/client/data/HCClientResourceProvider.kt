package hiiragi283.core.client.data

import com.google.gson.JsonObject
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.data.lang.HTLangTypes
import hiiragi283.core.api.data.texture.HTTextureUtil
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.getBucketHolder
import hiiragi283.core.api.registry.getFluidType
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import java.util.function.Consumer

data object HCClientResourceProvider : HTDynamicResourceProvider.Client(HiiragiCoreAPI.MOD_ID) {
    override fun addDynamicTranslations(afterLanguageLoadEvent: AfterLanguageLoadEvent) {}

    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        // Lang
        executor.accept { _, sink: ResourceSink -> addLang(sink, HTLangTypes.EN_US) }
        executor.accept { _, sink: ResourceSink -> addLang(sink, HTLangTypes.JA_JP) }
        // Model
        executor.accept(HCModelProvider)
        // Texture
        HTTextureUtil.clearCache()
        executor.accept(HCMaterialTextureProvider)

        blockTextures(executor)
        itemTextures(executor)
    }

    @JvmStatic
    private fun blockTextures(executor: Consumer<ResourceGenTask>) {
        executor.accept(
            resprite(
                HCBlocks.OIL_SAND.blockId,
                HTConst.MINECRAFT.toId(HTConst.BLOCK, "sand.png"),
                VanillaMaterialKeys.COAL,
            ),
        )
        executor.accept(
            resprite(
                HCBlocks.OIL_SHALE.blockId,
                HTConst.MINECRAFT.toId(HTConst.BLOCK, "stone.png"),
                VanillaMaterialKeys.COAL,
            ),
        )

        for (i in (0..2)) {
            executor.accept(
                resprite(
                    HiiragiCoreAPI.id(HTConst.BLOCK, "warped_wart_stage$i"),
                    HTConst.MINECRAFT.toId(HTConst.BLOCK, "nether_wart_stage$i.png"),
                    Blocks.TWISTING_VINES,
                ),
            )
        }
        executor.accept(
            resprite(
                HCBlocks.WARPED_WART.itemId,
                HTConst.MINECRAFT.toId(HTConst.ITEM, "nether_wart.png"),
                Blocks.TWISTING_VINES,
            ),
        )
        // Fluid
        executor.accept(
            resprite(
                HiiragiCoreAPI.id(HTConst.BLOCK, "dragon_breath"),
                HTConst.MINECRAFT.toId(HTConst.BLOCK, "lava_still.png"),
                Blocks.BRAIN_CORAL_BLOCK,
            ),
        )
    }

    @JvmStatic
    private fun itemTextures(executor: Consumer<ResourceGenTask>) {
        executor.accept(
            resprite(
                HCItems.BAMBOO_CHARCOAL.itemId,
                HTConst.MINECRAFT.toId(HTConst.ITEM, "bamboo.png"),
                Blocks.DEEPSLATE,
            ),
        )

        executor.accept(
            resprite(
                HCItems.RAW_RUBBER.itemId,
                HTConst.MINECRAFT.toId(HTConst.ITEM, "slime_ball.png"),
                Blocks.SANDSTONE,
            ),
        )
        mapOf(
            HCItems.POLYMER_RESIN to "blue_dye.png",
            HCItems.SYNTHETIC_FEATHER to "feather.png",
            HCItems.SYNTHETIC_FIBER to "string.png",
            HCItems.SYNTHETIC_LEATHER to "leather.png",
        ).forEach { (item: HTIdLike, path: String) ->
            executor.accept(
                resprite(
                    item.itemId,
                    HTConst.MINECRAFT.toId(HTConst.ITEM, path),
                    CommonMaterialKeys.PLASTIC,
                ),
            )
        }

        executor.accept(
            resprite(
                HCItems.LUMINOUS_PASTE.itemId,
                HTConst.MINECRAFT.toId(HTConst.ITEM, "black_dye.png"),
                Items.GLOW_INK_SAC,
            ),
        )
        executor.accept(
            resprite(
                HCItems.ELDER_HEART.itemId,
                HTConst.MINECRAFT.toId(HTConst.ITEM, "heart_of_the_sea.png"),
                CommonMaterialKeys.PLASTIC,
            ),
        )
        executor.accept(
            resprite(
                HCItems.WITHER_STAR.itemId,
                HTConst.MINECRAFT.toId(HTConst.ITEM, "nether_star.png"),
                Blocks.DEEPSLATE,
            ),
        )

        executor.accept(
            resprite(
                HCItems.ELDRITCH_EGG.itemId,
                HTConst.MINECRAFT.toId(HTConst.ITEM, "egg.png"),
                HCMaterialKeys.ELDRITCH,
            ),
        )

        executor.accept(
            resprite(
                HCItems.IRIDESCENT_POWDER.itemId,
                HTConst.MINECRAFT.toId(HTConst.ITEM, "blaze_powder.png"),
                CommonMaterialKeys.PLASTIC,
            ),
        )
    }

    //    Texture    //

    @JvmStatic
    private fun resprite(
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

    @JvmStatic
    private fun resprite(id: ResourceLocation, base: ResourceLocation, key: HTMaterialKey): ResourceGenTask =
        resprite(id, base, HTTextureUtil::getOrCreatePalette.partially1(key.getId()))

    @JvmStatic
    private fun resprite(id: ResourceLocation, base: ResourceLocation, palette: Block): ResourceGenTask =
        resprite(id, base) { manager: ResourceManager -> HTTextureUtil.getTexture(manager, palette).map(Palette::fromImage) }

    @JvmStatic
    private fun resprite(id: ResourceLocation, base: ResourceLocation, palette: Item): ResourceGenTask =
        resprite(id, base) { manager: ResourceManager -> HTTextureUtil.getTexture(manager, palette).map(Palette::fromImage) }

    //    Translation    //

    @JvmStatic
    private fun addLang(sink: ResourceSink, langType: HTLangType) {
        val root = JsonObject()
        addTranslations(langType, root::addProperty)
        sink.addLang(HiiragiCoreAPI.MOD_ID.toId(langType.name), root)
    }

    @JvmStatic
    fun addTranslations(langType: HTLangType, consumer: (String, String) -> Unit) {
        val registered: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.registeredContents
        val manager: HTMaterialManager = HiiragiCoreAccess.INSTANCE.materialManager

        for (entry: HTMaterialManager.Entry in manager) {
            // Block
            for ((part: HTPart, block: HTBlockHolderLike<*>) in registered.blocks.column(entry)) {
                val name: String = translate(langType, part, entry) ?: continue
                consumer(block.get().descriptionId, name)
            }
            // Fluid
            val fluids: HTMaterialContents<HTFluidPart, Fluid> = HiiragiCoreAccess.INSTANCE.registeredFluids
            for ((part: HTFluidPart, fluid: HTFluidHolderLike<*>) in fluids.column(entry)) {
                val name: String = translate(langType, part, entry) ?: continue
                consumer(fluid.getFluidType().descriptionId, name)
                consumer(Tags.getTagTranslationKey(part.createTagKey(entry)), name)

                val bucketName: String = HTLangPatternProvider.create("%s Bucket", "%s入りバケツ").translate(langType, name)
                consumer(fluid.getBucketHolder().translationKey, bucketName)
                consumer(Tags.getTagTranslationKey(part.createBucketTag(entry)), bucketName)
            }
            // Item
            for ((part: HTPart, item: HTMaterialContents.Entry<Item>) in registered.items.column(entry)) {
                val name: String = translate(langType, part, entry) ?: continue
                consumer(item.get().descriptionId, name)
            }
            // Tool
            for ((toolType: HTToolType, tool: HTMaterialContents.Entry<Item>) in registered.tools.column(entry)) {
                val materialName: HTLangName = entry[HTMaterialPropertyKeys.LANG_NAME] ?: continue
                consumer(tool.get().descriptionId, toolType.langPattern.translate(langType, materialName))
            }
        }
    }

    @JvmStatic
    private fun translate(type: HTLangType, part: HTPartLike, propertyMap: HTPropertyMap): String? =
        propertyMap.getOrDefault(HTMaterialPropertyKeys.CUSTOM_LANG_NAME)[part]?.getTranslatedName(type) ?: run {
            val materialName: HTLangName = propertyMap[HTMaterialPropertyKeys.LANG_NAME] ?: return@run null
            part.getOrDefault(HTPartPropertyKeys.LANG_PATTERN).translate(type, materialName)
        }
}
