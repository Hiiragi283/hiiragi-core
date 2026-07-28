package hiiragi283.core.client.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.api.data.texture.HTTextureUtil
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import java.util.function.Consumer
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks

data object HCClientResourceProvider : HTDynamicResourceProvider.Client(HiiragiCoreAPI.MOD_ID) {
    override fun addDynamicTranslations(afterLanguageLoadEvent: AfterLanguageLoadEvent) {}

    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        // Texture
        HTTextureUtil.clearCache()
        // executor.accept(HCMaterialTextureProvider)

        blockTextures(executor)
        itemTextures(executor)
    }

    @JvmStatic
    private fun vanillaBlockId(vararg path: String): ResourceLocation = vanillaId(HTConst.BLOCK, *path)

    @JvmStatic
    private fun vanillaItemId(vararg path: String): ResourceLocation = vanillaId(HTConst.ITEM, *path)

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
}
