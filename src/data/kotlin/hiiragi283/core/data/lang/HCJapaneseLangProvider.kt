package hiiragi283.core.data.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.HCTranslation
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import net.minecraft.data.PackOutput

class HCJapaneseLangProvider(output: PackOutput) :
    HTLangProvider(output, HiiragiCoreAPI.MOD_ID, HTLangTypes.JA_JP),
    HCLangProvider {
    override fun addTranslations() {
        addCommonTranslations(this::add)
        addPatternTranslations(this)

        // Block
        add(HCBlocks.CHARCOAL_BLOCK, "木炭ブロック")
        add(HCBlocks.ECHO_BLOCK, "残響ブロック")

        add(HCBlocks.WARPED_WART, "歪んだウォート")

        add(HCBlocks.CHOPPING_BOARD, "伐採台")
        // Fluid
        addFluid(HCFluids.EXPERIENCE, "液体経験値")
        addFluid(HCFluids.HONEY, "ハチミツ")
        addFluid(HCFluids.MUSHROOM_STEW, "キノコシチュー")
        addFluid(HCFluids.DRAGON_BREATH, "ドラゴンブレス")
        add(HCFluids.POTION.getFluidType().descriptionId, "無効なポーション入りバケツ")
        add(HCFluids.POTION.bucketHolder, $$"%1$s入りバケツ")
        addFluid(HCFluids.OMINOUS_FLUX, "不吉な流動体")

        addFluid(HCFluids.LATEX, "ラテックス")
        addFluid(HCFluids.MEAT, "肉")
        // Item
        add(HCItems.NETHERITE_NUGGET, "ネザライト塊")

        add(HCItems.ELDER_HEART, "エルダーの心臓")

        add(HCItems.SYNTHETIC_FEATHER, "合成羽")
        add(HCItems.SYNTHETIC_FIBER, "合成繊維")
        add(HCItems.SYNTHETIC_LEATHER, "合成皮革")

        add(HCItems.IRIDESCENT_POWDER, "七色の粉")
        // add(HCItems.ALMIGHTY_PICKAXE, "全能なるツルハシ")
        add(HCItems.AMBROSIA, "アンブロシア")
        add(HCItems.ETERNAL_UPGRADE, "永遠の鍛冶型")
        add(HCItems.POTION_OF_INFINITY, "無限のポーション")
        add(HCItems.RING_OF_HYPERION, "ハイペリオンの指輪")
        // Recipe
        add(HTVanillaRecipeTypes.SMELTING, "かまど")
        add(HTVanillaRecipeTypes.BLASTING, "溶鉱炉")
        add(HTVanillaRecipeTypes.SMOKING, "燻製器")
        add(HTVanillaRecipeTypes.BREWING, "醸造")

        add(HCRecipeTypes.CHARGING, "帯電")
        add(HCRecipeTypes.CHOPPING, "木こり")
        add(HCRecipeTypes.EXPLODING, "爆破")

        add(HCRecipeTypes.EMPTYING, "容器を空にする")
        add(HCRecipeTypes.FILLING, "容器に汲む")

        // Text
        add(HCTranslation.HIIRAGI_CORE, "Hiiragi Core")

        add(HCTranslation.ETERNAL_PICKAXE, "永遠のツルハシ")

        add(HCTranslation.ETERNAL_UPGRADE_APPLIES_TO, "任意の装備品")
        add(HCTranslation.ETERNAL_UPGRADE_INGREDIENTS, "イリジウムインゴット")
        add(HCTranslation.ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION, "任意の防具，武器，道具を置いてください")
        add(HCTranslation.ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "イリジウムインゴットを置いてください")
    }
}
