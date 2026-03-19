package hiiragi283.core.data.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLanguageProvider
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.data.PackOutput

class HCJapaneseLangProvider(output: PackOutput) :
    HTLanguageProvider.Japanese(output, HiiragiCoreAPI.MOD_ID),
    HCLangProvider {
    override fun addTranslations() {
        // Block
        add(HCBlocks.WARPED_WART, "歪んだウォート")
        // Item
        add(HCItems.ALMIGHTY_PICKAXE, "全能なるツルハシ")
        // Recipe
        add(HCRecipeTypes.CHARGING, "Lightning Charging")

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

        add(HCTranslation.MOLTEN_METAL, "溶融%s")
        add(HCTranslation.MOLTEN_METAL_BUCKET, "溶融%s入りバケツ")

        add(HCTranslation.MIN_POWER, "最小の爆発力: %s")

        add(HCTranslation.AMBROSIA, "いつでも食べられる上，いくら食べてもなくなりません！")
        add(HCTranslation.ANCIENT_UPGRADE, "ウォーデンからドロップします。")
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
