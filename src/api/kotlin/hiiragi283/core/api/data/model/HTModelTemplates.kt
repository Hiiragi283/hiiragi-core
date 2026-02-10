package hiiragi283.core.api.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import java.util.Optional

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[ModelTemplate]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 * @see ModelTemplates
 */
object HTModelTemplates {
    /**
     * すべての面が同じテクスチャかつ着色に対応したブロックのモデル
     */
    @JvmField
    val ALL_TINTED: ModelTemplate = block(HiiragiCoreAPI.id(HTConst.BLOCK, "all_tinted"), TextureSlot.ALL)

    /**
     * 二つのレイヤーを持つブロックのモデル
     */
    @JvmField
    val LAYERED: ModelTemplate = block(HiiragiCoreAPI.id(HTConst.BLOCK, "layered"), TextureSlot.LAYER0, TextureSlot.LAYER1)

    @JvmStatic
    private fun block(modelId: ResourceLocation, vararg requiredSlots: TextureSlot): ModelTemplate =
        ModelTemplate(Optional.of(modelId), Optional.empty(), *requiredSlots)

    @JvmStatic
    private fun block(modelId: ResourceLocation, suffix: String, vararg requiredSlots: TextureSlot): ModelTemplate =
        ModelTemplate(Optional.of(modelId), Optional.of(suffix), *requiredSlots)
}
