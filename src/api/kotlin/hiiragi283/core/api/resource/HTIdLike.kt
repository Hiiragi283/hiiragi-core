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
    val namespace: String get() = getId().namespace

    /**
     * 保持している[ID][ResourceLocation]の[パス][ResourceLocation.getPath]を返します。
     */
    val path: String get() = getId().path
}

/**
 * この[HTIdLike]から，`block/`で前置された[ID][HTIdLike.getId]を返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
val HTIdLike.blockId: ResourceLocation get() = when {
    this.path.startsWith("block/") -> getId()
    else -> getId().withPrefix("block/")
}

/**
 * この[HTIdLike]から，`item/`で前置された[ID][HTIdLike.getId]を返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
val HTIdLike.itemId: ResourceLocation get() = when {
    this.path.startsWith("item/") -> getId()
    else -> getId().withPrefix("item/")
}
