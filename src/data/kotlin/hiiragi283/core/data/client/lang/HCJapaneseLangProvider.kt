package hiiragi283.core.data.client.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.text.HTCommonTranslation
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

        // Fluid
        addFluid(HCFluids.HONEY, "ハチミツ")
        addFluid(HCFluids.MUSHROOM_STEW, "キノコシチュー")

        addFluid(HCFluids.LATEX, "ラテックス")
        addFluid(HCFluids.CRIMSON_SAP, "深紅の樹液")
        addFluid(HCFluids.WARPED_SAP, "歪んだ樹液")

        addFluid(HCFluids.CRIMSON_BLOOD, "深紅の血液")
        addFluid(HCFluids.DEW_OF_THE_WARP, "歪みの雫")
        addFluid(HCFluids.ELDRITCH_FLUX, "異質な流動体")

        // Item
        add(HCItems.COMPRESSED_SAWDUST, "圧縮されたおがくず")
        add(HCItems.SYNTHETIC_LEATHER, "合成牛皮")
        add(HCItems.TAR, "タール")

        add(HCItems.LUMINOUS_PASTE, "蛍光ペースト")
        add(HCItems.MAGMA_SHARD, "マグマシャード")
        add(HCItems.ELDER_HEART, "エルダーの心臓")
        add(HCItems.WITHER_STAR, "ウィザースター")

        add(HCItems.IRIDESCENT_POWDER, "虹色の粉")

        // Recipe
        add(HCRecipeTypes.CHARGING, "充電")
        add(HCRecipeTypes.CRUSHING, "粉砕")
        add(HCRecipeTypes.DRYING, "乾燥")
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
        add(HTCommonTranslation.TOOLTIP_BLOCK_POS, $$"座標: [%1$s, %2$s, %3$s]")
        add(HTCommonTranslation.TOOLTIP_CHARGE_POWER, $$"威力: %1$s")
        add(HTCommonTranslation.TOOLTIP_DIMENSION, $$"次元: %1$s")
        add(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"常に少なくとも%1$sがあります")
        add(HTCommonTranslation.TOOLTIP_LOOT_TABLE_ID, $$"ルートテーブル: %1$s")
        add(HTCommonTranslation.TOOLTIP_UPGRADE_TARGET, $$"アップグレードの対象: %1$s")
        add(HTCommonTranslation.TOOLTIP_UPGRADE_EXCLUSIVE, $$"競合するアップグレード: %1$s")

        add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION, "シフトキーを押して説明を表示")
        add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS, "シフトキーを押して詳細を表示")

        add(HTCommonTranslation.DATAPACK_WIP, "開発中の要素を有効にします")
    }
}
