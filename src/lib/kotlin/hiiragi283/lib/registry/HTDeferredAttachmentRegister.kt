package hiiragi283.lib.registry

import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTDeferredAttachmentRegister(namespace: String) : HTDeferredRegister<AttachmentType<*>>(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, namespace) {
    fun <TYPE : Any> registerType(name: String, builder: AttachmentType.Builder<TYPE>): AttachmentType<TYPE> {
        val type: AttachmentType<TYPE> = builder.build()
        this.register(name) { _ -> type }
        return type
    }
}
