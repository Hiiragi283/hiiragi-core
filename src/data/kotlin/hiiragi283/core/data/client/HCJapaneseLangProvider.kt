package hiiragi283.core.data.client

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLangTypes
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.data.PackOutput

class HCJapaneseLangProvider(output: PackOutput) :
    HTLangProvider(output, HiiragiCoreAPI.MOD_ID, HTLangTypes.JA_JP),
    HCLangProvider {
    override fun addTranslations() {
        addPatternTranslations(this)
        // Block
        add(HCBlocks.OIL_SAND, "オイルサンド")
        add(HCBlocks.OIL_SHALE, "オイルシェール")

        add(HCBlocks.WARPED_WART, "歪んだウォート")

        add(HCBlocks.TREE_TAP, "ツリータップ")
        add(HCBlocks.FORGING_ANVIL, "紺鉄の金床")
        // Entity
        add(HCEntityTypes.BOMB, "ボム")
        add(HCEntityTypes.ELDRITCH_EGG, "異質な卵")

        // Fluid
        addFluid(HCFluids.EXPERIENCE, "液体経験値")
        addFluid(HCFluids.HONEY, "ハチミツ")
        addFluid(HCFluids.MUSHROOM_STEW, "キノコシチュー")
        addFluid(HCFluids.DRAGON_BREATH, "ドラゴンブレス")
        add(HCFluids.POTION.getFluidType().descriptionId, "無効なポーション入りバケツ")
        add(HCFluids.POTION.getBucket(), $$"%1$s入りバケツ")
        addFluid(HCFluids.OMINOUS_FLUX, "不吉な流動体")

        addFluid(HCFluids.LATEX, "ラテックス")
        addFluid(HCFluids.MEAT, "肉")
        // Item
        add(HCItems.BAMBOO_CHARCOAL, "竹炭")
        add(HCItems.PARTICLE_BOARD, "パーティクルボード")
        add(HCItems.POLYMER_RESIN, "高分子樹脂")
        add(HCItems.RAW_RUBBER, "生ゴム")
        add(HCItems.STEEL_COMPOUND, "鋼鉄混合物")
        add(HCItems.SYNTHETIC_FEATHER, "合成羽")
        add(HCItems.SYNTHETIC_FIBER, "合成繊維")
        add(HCItems.SYNTHETIC_LEATHER, "合成牛皮")

        add(HCItems.WHEAT_DOUGH, "小麦の生地")
        add(HCItems.WHEAT_FLOUR, "小麦粉")

        add(HCItems.LUMINOUS_PASTE, "蛍光ペースト")
        add(HCItems.MAGMA_SHARD, "マグマシャード")
        add(HCItems.ELDER_HEART, "エルダーの心臓")
        add(HCItems.WITHER_DOLL, "ウィザー人形")
        add(HCItems.WITHER_STAR, "ウィザースター")

        add(HCItems.ANCIENT_UPGRADE, "古代の鍛冶型")
        add(HCItems.PAINT_BRUSH, "ペイントブラシ")

        add(HCItems.BLUEPRINT, "青写真")
        add(HCItems.BOMB, "ボム")
        add(HCItems.ELDRITCH_EGG, "異質な卵")
        add(HCItems.SLOT_COVER, "スロットカバー")
        add(HCItems.TRADER_CATALOG, "行商人のカタログ")
        add(HCItems.EXPERIENCE_TOME, "経験の書")

        add(HCItems.IRIDESCENT_POWDER, "七色の粉")
        add(HCItems.AMBROSIA, "アンブロシア")
        add(HCItems.ETERNAL_UPGRADE, "永遠の鍛冶型")
        add(HCItems.ALMIGHTY_PICKAXE, "全能なるツルハシ")

        // Recipe
        add(HTVanillaRecipeTypes.SMELTING, "かまど")
        add(HTVanillaRecipeTypes.BLASTING, "溶鉱炉")
        add(HTVanillaRecipeTypes.SMOKING, "燻製器")
        add(HTVanillaRecipeTypes.BREWING, "醸造")

        add(HCRecipeLookups.CHARGING, "落雷によるチャージ")
        add(HCRecipeLookups.CRUSHING, "粉砕")
        add(HCRecipeLookups.EXPLODING, "爆破")
        add(HCRecipeLookups.FORGING, "鍛造")
        add(HCRecipeLookups.MELTING, "溶融")
        add(HCRecipeLookups.TANK_INTERACTION, "タンクとの相互作用")

        // Translation
        translation()
    }

    private fun translation() {
        addCommonTranslations(::add)

        // API - Constants
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
        // API - Error
        add(HTCommonTranslation.EMPTY_TAG_KEY, $$"空のタグ: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_S2C, $$"サーバー側からの不正なパケットを受信しました: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_C2S, $$"クライアント側からの不正なパケットを受信しました: %1$s")

        add(HTCommonTranslation.MISSING_SERVER, "サーバーが見つかりません")
        add(HTCommonTranslation.MISSING_REGISTRY, $$"不明なレジストリ: %1$s")
        add(HTCommonTranslation.MISSING_KEY, $$"不明なキー: %1$s")
        // API - GUI
        add(HTCommonTranslation.PROGRESS, $$"進捗率: %1$s %%")
        add(HTCommonTranslation.SECONDS, $$"%1$s 秒 (%2$s ticks)")

        add(HTCommonTranslation.CHANCE_CONSUME, $$"消費確率: %1$s %%")
        add(HTCommonTranslation.CHANCE_PRODUCE, $$"生産確率: %1$s %%")

        add(HTCommonTranslation.RANGE_MIN, $$"最小値: %1$s")
        add(HTCommonTranslation.RANGE_MAX, $$"最大値: %1$s")
        add(HTCommonTranslation.RANGE_MIN_MAX, $$"最小値: %1$s, 最大値: %2$s")
        // API - Item
        add(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"常に少なくとも%1$sがあります")
        add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION, "シフトキーを押して説明を表示")
        add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS, "シフトキーを押して詳細を表示")

        add(HTCommonTranslation.DATAPACK_WIP, "開発中の要素を有効にします")
        // Mod
        add(HCTranslation.HIIRAGI_CORE, "Hiiragi Core")

        add(HCTranslation.WARPED_WART, "食べるとランダムにデバフを一つだけ消します。")

        add(HCTranslation.CREATIVE_TAB_MATERIAL, "Hiiragi Core - 素材")
        add(HCTranslation.CREATIVE_TAB_EQUIPMENT, "Hiiragi Core - 装備品")

        add(HCTranslation.MIN_POWER, "最小の爆発力: %s")

        add(HCTranslation.AMBROSIA, "いつでも食べられる上，いくら食べてもなくなりません！")
        add(HCTranslation.ANCIENT_UPGRADE, "ウォーデンからドロップします。")
        add(HCTranslation.BLUEPRINT, "GUI上で右クリックすると番号を変えられます。")
        add(HCTranslation.BLUEPRINT_NUMBER, $$"番号: %1$s")
        add(HCTranslation.ELDER_HEART, "エルダーガーディアンからドロップします。")
        add(HCTranslation.ELDRITCH_EGG, "右クリックで投げることができ，モブに当たるとスポーンエッグになります。")
        add(HCTranslation.ETERNAL_UPGRADE, "エンダードラゴンからドロップします。")
        add(HCTranslation.EXPERIENCE_TOME, "右クリックで経験値を貯蔵，シフトキーを押しながらで放出することができます。")
        add(HCTranslation.IRIDESCENT_POWDER, "時間経過やダメージで消滅しません。")
        add(HCTranslation.RAW_RUBBER, "設置したラテックスか，ラテックス入り大釜からドロップします。")
        add(HCTranslation.SLOT_COVER, "機械のスロットに入れることでレシピ判定から無視されます。")
        add(HCTranslation.TRADER_CATALOG, "行商人からドロップします。右クリックで行商人との取引を行えます。")

        add(HCTranslation.ETERNAL_PICKAXE, "永遠のツルハシ")

        add(HCTranslation.ANCIENT_UPGRADE_APPLIES_TO, "ダイヤモンドの装備品")
        add(HCTranslation.ANCIENT_UPGRADE_INGREDIENTS, "古代の金属インゴット")
        add(HCTranslation.ANCIENT_UPGRADE_DESC, "古代強化")
        add(HCTranslation.ANCIENT_UPGRADE_BASE_SLOT_DESCRIPTION, "ダイヤモンド製の防具，武器，道具を置いてください")
        add(HCTranslation.ANCIENT_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "古代の金属インゴットを置いてください")

        add(HCTranslation.ETERNAL_UPGRADE_APPLIES_TO, "任意の装備品")
        add(HCTranslation.ETERNAL_UPGRADE_INGREDIENTS, "イリジウムインゴット")
        add(HCTranslation.ETERNAL_UPGRADE_DESC, "不可壊強化")
        add(HCTranslation.ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION, "任意の防具，武器，道具を置いてください")
        add(HCTranslation.ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "イリジウムインゴットを置いてください")
    }
}
