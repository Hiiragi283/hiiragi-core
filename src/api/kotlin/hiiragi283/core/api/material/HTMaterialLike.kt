package hiiragi283.core.api.material

import net.minecraft.resources.ResourceLocation

/**
 * [HTMaterialKey]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTMaterialLike {
    /**
     * 保持している[素材キー][HTMaterialKey]を返します。
     */
    fun asMaterialKey(): HTMaterialKey

    /**
     * 保持している[素材キー][HTMaterialKey]の[ID][ResourceLocation]を返します。
     */
    fun asMaterialId(): ResourceLocation = asMaterialKey().getId()
}
