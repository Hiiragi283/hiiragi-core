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
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTSimpleFluidHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
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
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
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
    private fun vanillaBlockId(vararg path: String): ResourceLocation = HTConst.MINECRAFT.toId(HTConst.BLOCK, *path)

    @JvmStatic
    private fun vanillaItemId(vararg path: String): ResourceLocation = HTConst.MINECRAFT.toId(HTConst.ITEM, *path)

    @JvmStatic
    private fun blockTextures(executor: Consumer<ResourceGenTask>) {
        buildSet {
            this += resprite(
                HCBlocks.OIL_SAND.blockId,
                vanillaBlockId("sand"),
                VanillaMaterialKeys.COAL,
            )
            this += resprite(
                HCBlocks.OIL_SHALE.blockId,
                vanillaBlockId("stone"),
                VanillaMaterialKeys.COAL,
            )
            this += resprite(
                HCBlocks.WARPED_WART.itemId,
                vanillaItemId("nether_wart"),
                Blocks.TWISTING_VINES,
            )
        }.forEach(executor)

        (0..2)
            .map { i: Int ->
                resprite(
                    HiiragiCoreAPI.id(HTConst.BLOCK, "warped_wart_stage$i"),
                    vanillaBlockId("nether_wart_stage$i"),
                    Blocks.TWISTING_VINES,
                )
            }.forEach(executor)
        // Fluid
        executor.accept(
            resprite(
                HiiragiCoreAPI.id(HTConst.BLOCK, "dragon_breath"),
                vanillaBlockId("lava_still"),
                Blocks.BRAIN_CORAL_BLOCK,
            ),
        )
    }

    @JvmStatic
    private fun itemTextures(executor: Consumer<ResourceGenTask>) {
        buildSet {
            this += resprite(
                HCItems.BAMBOO_CHARCOAL.itemId,
                vanillaItemId("bamboo"),
                Blocks.DEEPSLATE,
            )
            this += resprite(
                HCItems.RAW_RUBBER.itemId,
                vanillaItemId("raw_gold"),
                Blocks.SANDSTONE,
            )
            this += resprite(
                HCItems.CURED_RUBBER.itemId,
                vanillaItemId("nether_brick"),
                CommonMaterialKeys.RUBBER,
            )
        }.forEach(executor)

        mapOf(
            HCItems.POLYMER_RESIN to "blue_dye",
            HCItems.SYNTHETIC_FEATHER to "feather",
            HCItems.SYNTHETIC_FIBER to "string",
            HCItems.SYNTHETIC_LEATHER to "leather",
        ).map { (item: HTIdLike, path: String) ->
            resprite(
                item.itemId,
                vanillaItemId(path),
                CommonMaterialKeys.PLASTIC,
            )
        }.forEach(executor)

        mapOf(
            HCItems.WHEAT_FLOUR to "brown_dye",
            HCItems.WHEAT_DOUGH to "clay_ball",
        ).map { (item: HTIdLike, base: String) ->
            resprite(item.itemId, vanillaItemId(base), Items.WHEAT)
        }.forEach(executor)

        buildSet {
            this += resprite(
                HCItems.LUMINOUS_PASTE.itemId,
                vanillaItemId("black_dye"),
                Items.GLOW_INK_SAC,
            )
            this += resprite(
                HCItems.ELDER_HEART.itemId,
                vanillaItemId("heart_of_the_sea"),
                CommonMaterialKeys.PLASTIC,
            )
            this += resprite(
                HCItems.WITHER_STAR.itemId,
                vanillaItemId("nether_star"),
                Blocks.DEEPSLATE,
            )
            this += resprite(
                HCItems.ELDRITCH_EGG.itemId,
                vanillaItemId("egg"),
                HCMaterialKeys.ELDRITCH,
            )
            this += resprite(
                HCItems.IRIDESCENT_POWDER.itemId,
                vanillaItemId("blaze_powder"),
                CommonMaterialKeys.PLASTIC,
            )
        }.forEach(executor)
    }

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

        for (entry: HTMaterialManager.Entry in HTMaterialManager.getInstance()) {
            // Material Name
            val key: HTMaterialKey = entry.asMaterialKey()
            val materialName: HTLangName = entry[HTMaterialPropertyKeys.LANG_NAME] ?: continue
            consumer(key.translationKey, materialName.getTranslatedName(langType))
            // Block
            for ((part: HTPart, block: HTBlockHolderLike<*>) in registered.blocks.column(entry)) {
                val name: String = translate(langType, part, entry) ?: continue
                consumer(block.get().descriptionId, name)
            }
            // Fluid
            val fluids: HTMaterialContents<HTFluidPart, HTMaterialContents.FluidEntry> = HiiragiCoreAccess.INSTANCE.registeredFluids
            for ((part: HTFluidPart, fluid: HTSimpleFluidHolderLike) in fluids.column(entry)) {
                val name: String = translate(langType, part, entry) ?: continue
                consumer(fluid.getFluidType().descriptionId, name)
                consumer(Tags.getTagTranslationKey(part.createTagKey(entry)), name)

                val bucketName: String = HTLangPatternProvider.create("%s Bucket", "%s入りバケツ").translate(langType, name)
                consumer(fluid.getBucket().translationKey, bucketName)
                consumer(Tags.getTagTranslationKey(part.createBucketTag(entry)), bucketName)
            }
            // Item
            for ((part: HTPart, item: HTSimpleItemHolderLike) in registered.items.column(entry)) {
                val name: String = translate(langType, part, entry) ?: continue
                consumer(item.translationKey, name)
            }
            // Tool
            for ((toolType: HTToolType, tool: HTSimpleItemHolderLike) in registered.tools.column(entry)) {
                consumer(tool.translationKey, toolType.langPattern.translate(langType, materialName))
            }
        }
    }

    @JvmStatic
    private fun translate(type: HTLangType, part: HTPart, getter: HTPropertyGetter): String? = translate(type, part, getter, HTMaterialPropertyKeys.CUSTOM_LANG_NAME)

    @JvmStatic
    private fun translate(type: HTLangType, part: HTFluidPart, getter: HTPropertyGetter): String? = translate(type, part, getter, HTMaterialPropertyKeys.CUSTOM_FLUID_NAME)

    @JvmStatic
    private fun <T : HTPartLike> translate(
        type: HTLangType,
        part: T,
        getter: HTPropertyGetter,
        key: HTPropertyKey<Map<T, HTLangName>>,
    ): String? = getter.getOrDefault(key)[part]?.getTranslatedName(type) ?: run {
        val materialName: HTLangName = getter[HTMaterialPropertyKeys.LANG_NAME] ?: return@run null
        part.getOrDefault(HTPartPropertyKeys.LANG_PATTERN).translate(type, materialName)
    }
}
