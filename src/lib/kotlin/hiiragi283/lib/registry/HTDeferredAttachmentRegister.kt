package hiiragi283.lib.registry

import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.NeoForgeRegistries

/**
 * [AttachmentType]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredAttachmentRegister(namespace: String) : HTDeferredRegister<AttachmentType<*>>(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, namespace) {
    /**
     * 新しい[AttachmentType]を登録します。
     * @param TYPE 保持する値のクラス
     * @param name 登録する値のIDのパス
     * @param builder [AttachmentType.Builder]を提供するブロック
     * @return 登録された[AttachmentType]のインスタンス
     */
    inline fun <TYPE : Any> registerType(name: String, builder: () -> AttachmentType.Builder<TYPE>): AttachmentType<TYPE> {
        val type: AttachmentType<TYPE> = builder().build()
        this.register(name) { _ -> type }
        return type
    }
}
