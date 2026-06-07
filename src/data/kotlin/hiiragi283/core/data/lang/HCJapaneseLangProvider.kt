package hiiragi283.core.data.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.HCTranslation
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCEnchantments
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import hiiragi283.lib.text.HTCommonTranslation
import net.minecraft.data.PackOutput

class HCJapaneseLangProvider(output: PackOutput) :
    HTLangProvider(output, HiiragiCoreAPI.MOD_ID, HTLangTypes.JA_JP),
    HCLangProvider {
    override fun addTranslations() {
        addCommonTranslations(this::add)
        addPatternTranslations(this)

        // Block
        addMaterials(HCBlocks.RESOURCES)

        add(HCBlocks.WARPED_WART, "歪んだウォート")

        add(HCBlocks.CHOPPING_BOARD, "伐採台")
        add(HCBlocks.FORGING_ANVIL, "鍛造台")
        // Enchantment
        add(HCEnchantments.HAMMER_OF_JUSTICE, "正義の鉄槌", "襲撃者に対するダメージを増加させます。")
        add(HCEnchantments.NOISE_CANCELING, "ノイズキャンセリング", "ウォーデンなどのスカルク系モンスターに対するダメージを増加させます。")
        add(HCEnchantments.PURIFICATION, "浄化", "ウィザー系モンスターに対するダメージを増加させます。")

        add(HCEnchantments.SONIC_PROTECTION, "音響耐性", "ソニックブームなどの音響攻撃を無効にします。")
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
        addMaterials(HCItems.RESOURCES)

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
        add(HCRecipeTypes.CRUSHING, "粉砕")
        add(HCRecipeTypes.EXPLODING, "爆破")

        add(HCRecipeTypes.EMPTYING, "容器を空にする")
        add(HCRecipeTypes.FILLING, "容器に汲む")

        // Text - Hiiragi Series
        add(HTCommonTranslation.ERROR, "エラー")
        add(HTCommonTranslation.INFINITE, "無限")
        add(HTCommonTranslation.NONE, "なし")
        add(HTCommonTranslation.EMPTY, "空")

        add(HTCommonTranslation.DOWN, "下")
        add(HTCommonTranslation.UP, "上")
        add(HTCommonTranslation.NORTH, "北")
        add(HTCommonTranslation.SOUTH, "南")
        add(HTCommonTranslation.WEST, "西")
        add(HTCommonTranslation.EAST, "東")

        add(HTCommonTranslation.INVALID_PACKET_S2C, $$"サーバー側からの不正なパケットを受信しました: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_C2S, $$"クライアント側からの不正なパケットを受信しました: %1$s")

        add(HTCommonTranslation.PROGRESS, $$"進捗率: %1$s %%")
        add(HTCommonTranslation.SECONDS, $$"%1$s 秒 (%2$s ticks)")

        add(HTCommonTranslation.CHANCE_PRODUCE, $$"生産確率: %1$s %%")

        add(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"常に少なくとも%1$sがあります")
        add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION, "シフトキーを押して説明を表示")
        add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS, "シフトキーを押して詳細を表示")

        add(HTCommonTranslation.DATAPACK_WIP, "開発中の要素を有効にします")
        // Text - Hiiragi Core
        add(HCTranslation.HIIRAGI_CORE, "Hiiragi Core")

        add(HCTranslation.ETERNAL_PICKAXE, "永遠のツルハシ")

        add(HCTranslation.ETERNAL_UPGRADE_APPLIES_TO, "任意の装備品")
        add(HCTranslation.ETERNAL_UPGRADE_INGREDIENTS, "イリジウムインゴット")
        add(HCTranslation.ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION, "任意の防具，武器，道具を置いてください")
        add(HCTranslation.ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "イリジウムインゴットを置いてください")
    }
}
