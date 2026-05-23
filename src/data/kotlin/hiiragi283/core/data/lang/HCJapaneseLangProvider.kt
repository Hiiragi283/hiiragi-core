package hiiragi283.core.data.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
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
    }
}
