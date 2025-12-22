package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * [HTHolderLike]を実装した[DeferredHolder]の拡張クラスです。
 * @param R レジストリの要素のクラス
 * @param T [R]を継承した値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.registration.MekanismDeferredHolder
 */
open class HTDeferredHolder<R : Any, T : R>(key: ResourceKey<R>) :
    DeferredHolder<R, T>(key),
    HTHolderLike<R, T>,
    HTKeyLike.HolderDelegate<R> {
    constructor(key: RegistryKey<R>, id: ResourceLocation) : this(key.createKey(id))

    override fun getId(): ResourceLocation = super<DeferredHolder>.getId()

    override fun getKey(): ResourceKey<R> = super.key

    override fun getHolder(): Holder<R> = this.delegate
}
