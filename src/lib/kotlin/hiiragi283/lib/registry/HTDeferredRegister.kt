package hiiragi283.lib.registry

import hiiragi283.lib.resource.toId
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

open class HTDeferredRegister<T : Any>(registryKey: RegistryKey<T>, namespace: String) : DeferredRegister<T>(registryKey, namespace) {
    /**
     * [名前空間][namespace]に基づいて，[パス][path]から[ID][Identifier]を作成します。
     * @param path IDの[パス][Identifier.getPath]
     */
    fun createId(path: String): Identifier = namespace.toId(path)

    /**
     * [名前空間][namespace]に基づいて，IDのエイリアスを登録します。
     * @param from 変更前のIDの[パス][Identifier.getPath]
     * @param to 変更後のIDの[パス][Identifier.getPath]
     */
    fun addAlias(from: String, to: String) {
        this.addAlias(createId(from), createId(to))
    }

    open fun asSequence(): Sequence<DeferredHolder<T, *>> = this.entries.asSequence()
}
