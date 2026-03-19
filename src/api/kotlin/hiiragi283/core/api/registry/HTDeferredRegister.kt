package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.toId
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[DeferredRegister]のラッパークラスです。
 * @param R レジストリの要素の値
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
open class HTDeferredRegister<R : Any>(protected val delegate: DeferredRegister<R>) {
    constructor(registryKey: RegistryKey<R>, namespace: String) : this(DeferredRegister.create(registryKey, namespace))

    @JvmField
    val registryKey: RegistryKey<R> = delegate.registryKey

    @JvmField
    val namespace: String = delegate.namespace

    /**
     * [名前空間][namespace]に基づいて，[パス][path]から[ID][Identifier]を作成します。
     * @param path IDの[パス][Identifier.getPath]
     */
    fun createId(path: String): Identifier = namespace.toId(path)

    fun createKey(path: String): ResourceKey<R> = createId(path).let(registryKey::createKey)

    fun addAlias(from: Identifier, to: Identifier) {
        delegate.addAlias(from, to)
    }

    /**
     * [名前空間][namespace]に基づいて，IDのエイリアスを登録します。
     * @param from 変更前のIDの[パス][Identifier.getPath]
     * @param to 変更後のIDの[パス][Identifier.getPath]
     */
    fun addAlias(from: String, to: String) {
        this.addAlias(createId(from), createId(to))
    }

    fun asSequence(): Sequence<HTHolderLike<R, *>> = delegate.entries.asSequence().map { it.toLike() }

    fun register(bus: IEventBus) {
        delegate.register(bus)
    }
}
