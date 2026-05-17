package hiiragi283.lib.registry

import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Function
import java.util.function.Supplier
import net.neoforged.neoforge.common.util.ValueIOSerializable

class HTDeferredAttachmentRegister(namespace: String) : HTDeferredRegister<AttachmentType<*>>(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, namespace) {
    fun <TYPE : Any> registerType(name: String, builder: AttachmentType.Builder<TYPE>): HTDeferredAttachmentType<TYPE> {
        this.register(name) { _ -> builder.build() }
        return HTDeferredAttachmentType(createId(name))
    }

    inline fun <TYPE : ValueIOSerializable> registerSerializable(
        name: String,
        factory: Function<IAttachmentHolder, TYPE>,
        operator: Identity<AttachmentType.Builder<TYPE>> = identity(),
    ): HTDeferredAttachmentType<TYPE> = registerType(name, AttachmentType.serializable(factory).let(operator))

    inline fun <TYPE : ValueIOSerializable> registerSerializable(
        name: String,
        supplier: Supplier<TYPE>,
        operator: Identity<AttachmentType.Builder<TYPE>> = identity(),
    ): HTDeferredAttachmentType<TYPE> = registerType(name, AttachmentType.serializable(supplier).let(operator))
}
