package hiiragi283.core.api.resource

import net.minecraft.resources.ResourceLocation

/**
 * [ID][ResourceLocation]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTKeyLike
 */
fun interface HTIdLike {
    /**
     * 保持している[ID][ResourceLocation]を返します。
     */
    fun getId(): ResourceLocation

    /**
     * 保持している[ID][ResourceLocation]の[名前空間][ResourceLocation.getNamespace]を返します。
     */
    fun getNamespace(): String = getId().namespace

    /**
     * 保持している[ID][ResourceLocation]の[パス][ResourceLocation.getPath]を返します。
     */
    fun getPath(): String = getId().path
}
