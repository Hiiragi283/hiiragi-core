package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.toId
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

open class HTDeferredRegister<R : Any>(protected val delegate: DeferredRegister<R>) {
    constructor(registryKey: RegistryKey<R>, namespace: String) : this(DeferredRegister.create(registryKey, namespace))

    @JvmField
    val registryKey: RegistryKey<R> = delegate.registryKey

    @JvmField
    val namespace: String = delegate.namespace

    /**
     * [名前空間][namespace]に基づいて，[パス][path]から[ID][ResourceLocation]を作成します。
     * @param path IDの[パス][ResourceLocation.getPath]
     */
    fun createId(path: String): ResourceLocation = namespace.toId(path)

    fun addAlias(from: ResourceLocation, to: ResourceLocation) {
        delegate.addAlias(from, to)
    }

    /**
     * [名前空間][namespace]に基づいて，IDのエイリアスを登録します。
     * @param from 変更前のIDの[パス][ResourceLocation.getPath]
     * @param to 変更後のIDの[パス][ResourceLocation.getPath]
     */
    fun addAlias(from: String, to: String) {
        this.addAlias(createId(from), createId(to))
    }

    fun asSequence(): Sequence<HTHolderLike.HolderDelegate<R, *>> = delegate.entries.asSequence().map { it.toLike() }

    fun register(bus: IEventBus) {
        delegate.register(bus)
    }
}
