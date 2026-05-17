package hiiragi283.lib.resource

import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.HTHasTranslationKey
import net.minecraft.resources.Identifier

/**
 * [ID][Identifier]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see SupplierWithId
 */
fun interface HTIdLike {
    /**
     * 保持している[ID][Identifier]を返します。
     */
    fun getId(): Identifier

    /**
     * 保持している[ID][Identifier]の[名前空間][Identifier.getNamespace]を返します。
     */
    val namespace: String get() = getId().namespace

    /**
     * 保持している[ID][Identifier]の[パス][Identifier.getPath]を返します。
     */
    val path: String get() = getId().path

    interface Translatable :
        HTIdLike,
        HTHasTranslationKey,
        HTHasText
}

/**
 * この[HTIdLike]から，`block/`で前置された[ID][HTIdLike.getId]を返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
val HTIdLike.blockId: Identifier get() = when {
    this.path.startsWith("block/") -> getId()
    else -> getId().withPrefix("block/")
}

/**
 * この[HTIdLike]から，`item/`で前置された[ID][HTIdLike.getId]を返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
val HTIdLike.itemId: Identifier get() = when {
    this.path.startsWith("item/") -> getId()
    else -> getId().withPrefix("item/")
}
