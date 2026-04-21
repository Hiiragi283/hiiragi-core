package hiiragi283.core.api.resource

import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.resources.ResourceLocation
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
