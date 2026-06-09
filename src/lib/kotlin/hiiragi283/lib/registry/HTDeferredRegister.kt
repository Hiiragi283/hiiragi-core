package hiiragi283.lib.registry

import hiiragi283.lib.resource.toId
import java.util.function.Function
import java.util.function.Supplier
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredRegister

open class HTDeferredRegister<T : Any>(registryKey: RegistryKey<T>, namespace: String) : DeferredRegister<T>(registryKey, namespace) {
    /**
     * [名前空間][namespace]に基づいて，[パス][path]から[ID][Identifier]を作成します。
     * @param path IDの[パス][Identifier.getPath]
     */
    fun createId(path: String): Identifier = namespace.toId(path)

    fun createKey(path: String): ResourceKey<T> = this.registryKey.createKey(namespace, path)

    fun createKey(id: Identifier): ResourceKey<T> = this.registryKey.createKey(id)

    /**
     * [名前空間][namespace]に基づいて，IDのエイリアスを登録します。
     * @param from 変更前のIDの[パス][Identifier.getPath]
     * @param to 変更後のIDの[パス][Identifier.getPath]
     */
    fun addAlias(from: String, to: String) {
        this.addAlias(createId(from), createId(to))
    }

    open fun asSequence(): Sequence<HTDeferredHolder<T, *>> = this.entries.asSequence().filterIsInstance<HTDeferredHolder<T, *>>()

    override fun <I : T> createHolder(registryKey: RegistryKey<T>, key: Identifier): HTDeferredHolder<T, I> = HTDeferredHolder(registryKey, key)

    override fun <I : T> register(name: String, sup: Supplier<out I>): HTDeferredHolder<T, I> = super.register(name, sup) as HTDeferredHolder<T, I>

    override fun <I : T> register(name: String, func: Function<Identifier, out I>): HTDeferredHolder<T, I> = super.register(name, func) as HTDeferredHolder<T, I>
}
