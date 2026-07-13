package hiiragi283.core.api.resource

import net.minecraft.resources.ResourceKey

typealias SimpleSupplierWithKey<R> = SupplierWithKey<R, R>

/**
 * [キー][ResourceKey]を提供する[SupplierWithId]の拡張インターフェースです。
 * @param T 保持している値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
interface SupplierWithKey<R : Any, out T : R> :
    SupplierWithId<T>,
    HTKeyLike<R>
