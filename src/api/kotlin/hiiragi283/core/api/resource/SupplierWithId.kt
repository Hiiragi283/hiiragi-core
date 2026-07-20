package hiiragi283.core.api.resource

import java.util.function.Supplier
import net.minecraft.resources.ResourceLocation

/**
 * [ResourceLocation]を提供する[Supplier]の拡張インターフェースです。
 * @param T 保持している値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 */
interface SupplierWithId<out T> :
    Supplier<@UnsafeVariance T>,
    HTIdLike
