package hiiragi283.core.data.client.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.data.PackOutput

class HCJapaneseLangProvider(output: PackOutput) :
    HTLangProvider.Japanese(output, HiiragiCoreAPI.MOD_ID),
    HCLangProvider {
    override fun addTranslations() {
        // Material
        HCMaterialTranslations.addTranslations(this)

        // Block
        add(HCBlocks.WARPED_WART, "歪んだウォート")

        // Entity
        add(HCEntityTypes.ELDRITCH_EGG, "異質な卵")

        // Fluid
        addFluid(HCFluids.EXPERIENCE, "液体経験値")
        addFluid(HCFluids.HONEY, "ハチミツ")
        addFluid(HCFluids.MUSHROOM_STEW, "キノコシチュー")

        addFluid(HCFluids.LATEX, "ラテックス")
        addFluid(HCFluids.BLOOD, "血液")
        addFluid(HCFluids.MEAT, "肉")

        addFluid(HCFluids.MOLTEN_GLASS, "溶融ガラス")
        addFluid(HCFluids.MOLTEN_CRIMSON_CRYSTAL, "深紅の血液")
        addFluid(HCFluids.MOLTEN_WARPED_CRYSTAL, "歪みの雫")
        addFluid(HCFluids.MOLTEN_ELDRITCH, "異質な流動体")

        // Item
        add(HCItems.BAMBOO_CHARCOAL, "竹炭")
        add(HCItems.COMPRESSED_SAWDUST, "圧縮されたおがくず")
        add(HCItems.SYNTHETIC_LEATHER, "合成牛皮")

        add(HCItems.LUMINOUS_PASTE, "蛍光ペースト")
        add(HCItems.MAGMA_SHARD, "マグマシャード")
        add(HCItems.ELDER_HEART, "エルダーの心臓")
        add(HCItems.WITHER_DOLL, "ウィザー人形")
        add(HCItems.WITHER_STAR, "ウィザースター")

        add(HCItems.WHEAT_FLOUR, "小麦粉")
        add(HCItems.WHEAT_DOUGH, "小麦の生地")
        add(HCItems.ANIMAL_FAT, "獣脂")
        add(HCItems.PULPED_FISH, "魚のパルプ")
        add(HCItems.PULPED_SEED, "種のパルプ")

        add(HCItems.ELDRITCH_EGG, "異質な卵")
        add(HCItems.SLOT_COVER, "スロットカバー")
        add(HCItems.TRADER_CATALOG, "行商人のカタログ")

        add(HCItems.IRIDESCENT_POWDER, "虹色の粉")
        add(HCItems.AMBROSIA, "アンブロシア")
        add(HCItems.ETERNAL_TICKET, "永遠のチケット")

        // Recipe
        add(HCRecipeTypes.CHARGING, "落雷によるチャージ")
        add(HCRecipeTypes.CRUSHING, "金床による粉砕")
        add(HCRecipeTypes.EXPLODING, "爆破")

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
        // API - Error
        add(HTCommonTranslation.EMPTY_TAG_KEY, $$"空のタグ: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_S2C, $$"サーバー側からの不正なパケットを受信しました: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_C2S, $$"クライアント側からの不正なパケットを受信しました: %1$s")

        add(HTCommonTranslation.MISSING_SERVER, "サーバーが見つかりません")
        add(HTCommonTranslation.MISSING_REGISTRY, $$"不明なレジストリ: %1$s")
        add(HTCommonTranslation.MISSING_KEY, $$"不明なキー: %1$s")
        // API - GUI
        add(HTCommonTranslation.SECONDS, $$"%1$s 秒 (%2$s ticks)")
        // API - Item
        add(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"常に少なくとも%1$sがあります")
        add(HTCommonTranslation.TOOLTIP_UPGRADE_TARGET, $$"アップグレードの対象: %1$s")
        add(HTCommonTranslation.TOOLTIP_UPGRADE_EXCLUSIVE, $$"競合するアップグレード: %1$s")

        add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION, "シフトキーを押して説明を表示")
        add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS, "シフトキーを押して詳細を表示")

        add(HTCommonTranslation.DATAPACK_WIP, "開発中の要素を有効にします")
        // Mod
        add(HCTranslation.HIIRAGI_CORE, "Hiiragi Core")

        add(HCTranslation.WARPED_WART, "食べるとランダムにデバフを一つだけ消します。")

        add(HCTranslation.AMBROSIA, "いつでも食べられる上，いくら食べてもなくなりません！")
        add(HCTranslation.ELDER_HEART, "エルダーガーディアンからドロップします。")
        add(HCTranslation.ELDRITCH_EGG, "右クリックで投げることができ，モブに当たるとスポーンエッグになります。")
        add(HCTranslation.ETERNAL_TICKET, "あらゆる道具を不可壊にします！")
        add(HCTranslation.IRIDESCENT_POWDER, "時間経過やダメージで消滅しません。")
        add(HCTranslation.SLOT_COVER, "機械のスロットに入れることでレシピ判定から無視されます。")
        add(HCTranslation.TRADER_CATALOG, "行商人からドロップします。右クリックで行商人との取引を行えます。")
    }
}
