package hiiragi283.core.api.resource

import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.resources.ResourceLocation

/**
 * [ID][ResourceLocation]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTIdLike {
    /**
     * 保持している[ID][ResourceLocation]を返します。
     */
    fun getId(): ResourceLocation

    /**
     * 保持している[ID][ResourceLocation]の[名前空間][ResourceLocation.getNamespace]を返します。
     */
    val namespace: String get() = getId().namespace

    /**
     * 保持している[ID][ResourceLocation]の[パス][ResourceLocation.getPath]を返します。
     */
    val path: String get() = getId().path

    interface Translatable :
        HTIdLike,
        HTHasTranslationKey,
        HTHasText
}
