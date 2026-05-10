package hiiragi283.lib.resource

import java.util.function.Supplier

/**
 * [ResourceLocation]を提供する[Supplier]の拡張インターフェースです。
 * @param T 保持している値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 * @see HTHolderLike
 */
interface SupplierWithId<T : Any> :
    Supplier<T>,
    HTIdLike
