package hiiragi283.core.api.registry.register

import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.IdToFunction
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.toId
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[DeferredRegister]の拡張クラスです。
 * @param T レジストリの要素の値
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.registration.MekanismDeferredRegister
 */
open class HTDeferredRegister<T : Any>(registryKey: RegistryKey<T>, namespace: String) : DeferredRegister<T>(registryKey, namespace) {
    /**
     * [名前空間][getNamespace]に基づいて，[パス][path]から[ID][ResourceLocation]を作成します。
     * @param path IDの[パス][ResourceLocation.getPath]
     */
    fun createId(path: String): ResourceLocation = namespace.toId(path)

    /**
     * [名前空間][getNamespace]に基づいて，IDのエイリアスを登録します。
     * @param from 変更前のIDの[パス][ResourceLocation.getPath]
     * @param to 変更後のIDの[パス][ResourceLocation.getPath]
     */
    fun addAlias(from: String, to: String) {
        addAlias(createId(from), createId(to))
    }

    /**
     * 登録された[HTDeferredHolder]の一覧を返します。
     * @return [HTDeferredHolder]の[Sequence]
     */
    open fun asSequence(): Sequence<HTDeferredHolder<T, out T>> = entries.asSequence()

    //    DeferredRegister    //

    override fun getEntries(): Collection<HTDeferredHolder<T, out T>> = super.getEntries().filterIsInstance<HTDeferredHolder<T, out T>>()

    override fun <I : T> register(name: String, func: IdToFunction<out I>): HTDeferredHolder<T, I> =
        super.register(name, func) as HTDeferredHolder<T, I>

    override fun <I : T> register(name: String, sup: Supplier<out I>): HTDeferredHolder<T, I> =
        super.register(name, sup) as HTDeferredHolder<T, I>

    override fun <I : T> createHolder(registryKey: RegistryKey<T>, key: ResourceLocation): HTDeferredHolder<T, I> =
        HTDeferredHolder(registryKey, key)
}
