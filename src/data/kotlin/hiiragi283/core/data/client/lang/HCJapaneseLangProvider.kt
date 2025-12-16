package hiiragi283.core.data.client.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.client.emi.category.HCEmiRecipeCategories
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.data.PackOutput

class HCJapaneseLangProvider(output: PackOutput) : HTLangProvider.Japanese(output, HiiragiCoreAPI.MOD_ID) {
    override fun addTranslations() {
        // Material
        HCMaterialTranslations.addTranslations(this)

        // Block
        add(HCBlocks.DRYING_LACK, "乾燥ラック")

        // Fluid
        addFluid(HCFluids.HONEY, "ハチミツ")
        addFluid(HCFluids.MUSHROOM_STEW, "キノコシチュー")

        addFluid(HCFluids.LATEX, "ラテックス")
        addFluid(HCFluids.CRIMSON_SAP, "深紅の樹液")
        addFluid(HCFluids.WARPED_SAP, "歪んだ樹液")

        addFluid(HCFluids.CRIMSON_BLOOD, "深紅の血液")
        addFluid(HCFluids.DEW_OF_THE_WARP, "歪みの雫")
        addFluid(HCFluids.ELDRITCH_FLUX, "エルドリッチフラックス")

        // Item
        add(HCItems.COAL_CHIP, "石炭チップ")
        add(HCItems.COAL_CHUNK, "石炭塊")
        add(HCItems.SYNTHETIC_LEATHER, "合成牛皮")
        add(HCItems.TAR, "タール")

        add(HCItems.CRYOGEN_POWDER, "クライオジェンパウダー")
        add(HCItems.LUMINOUS_PASTE, "蛍光ペースト")
        add(HCItems.MAGMA_SHARD, "マグマシャード")
        add(HCItems.ELDER_HEART, "エルダーの心臓")
        add(HCItems.WITHER_STAR, "ウィザースター")

        add(HCItems.IRIDESCENT_POWDER, "虹色の粉")

        // Translation
        add(HCTranslation.HIIRAGI_CORE, "Hiiragi Core")

        // EMI
        add(HCEmiRecipeCategories.DRYING, "乾燥")
        add(HCEmiRecipeCategories.FROSTING, "凍結")
    }
}
