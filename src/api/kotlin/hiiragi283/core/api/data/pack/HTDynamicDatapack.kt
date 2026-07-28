package hiiragi283.core.api.data.pack

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.toText
import hiiragi283.core.internal.data.pack.HTPackContents
import java.io.InputStream
import java.nio.file.Path
import net.minecraft.SharedConstants
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackLocationInfo
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.metadata.MetadataSectionSerializer
import net.minecraft.server.packs.metadata.pack.PackMetadataSection
import net.minecraft.server.packs.resources.IoSupplier

/**
 * Hiiragi Seriesで使用される動的データパックを管理するクラスです。
 *
 * 参照 : [GregTech Modern - GTDynamicDataPack](https://github.com/GregTechCEu/GregTech-Modern/blob/1.21/src/main/java/com/gregtechceu/gtceu/data/pack/GTDynamicDataPack.java)
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
class HTDynamicDatapack(private val locationInfo: PackLocationInfo) : PackResources {
    companion object {
        @JvmStatic
        private val DOMAINS: MutableSet<String> = HTConst.getBuiltInIdSet(HiiragiCoreAPI.MOD_ID).toMutableSet()

        @JvmStatic
        private val CONTENTS = HTPackContents()

        @JvmStatic
        fun addDomain(domain: String) {
            DOMAINS += domain
        }

        @JvmStatic
        fun clear() {
            CONTENTS.clearData()
        }

        @JvmStatic
        fun addToData(id: ResourceLocation, bytes: ByteArray) {
            if (HiiragiCoreAPI.isDevelopment()) {
                val parent: Path = HiiragiCoreAPI.GAME_DIR.resolve("debug/dumped/data")
                HTPackContents.dumpJson(id, parent, bytes)
            }
            CONTENTS.addToData(id, bytes)
        }
    }

    //    PackResources    //

    override fun getRootResource(vararg elements: String): IoSupplier<InputStream>? = when {
        elements.firstOrNull() == "pack.png" -> IoSupplier { HiiragiCoreAPI::class.java.getResourceAsStream("/icon.png")!! }
        else -> null
    }

    override fun getResource(packType: PackType, location: ResourceLocation): IoSupplier<InputStream>? = when (packType) {
        PackType.CLIENT_RESOURCES -> null
        PackType.SERVER_DATA -> CONTENTS.getResource(location)
    }

    override fun listResources(packType: PackType, namespace: String, path: String, resourceOutput: PackResources.ResourceOutput) {
        if (packType == PackType.SERVER_DATA) {
            CONTENTS.listResources(namespace, path, resourceOutput)
        }
    }

    override fun getNamespaces(type: PackType): Set<String> = when (type) {
        PackType.CLIENT_RESOURCES -> setOf()
        PackType.SERVER_DATA -> DOMAINS
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getMetadataSection(deserializer: MetadataSectionSerializer<T>): T? = when (deserializer) {
        PackMetadataSection.TYPE -> PackMetadataSection("Hiiragi Core dynamic data".toText(), SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA)) as T
        else -> null
    }

    override fun location(): PackLocationInfo = locationInfo

    override fun close(): Unit = Unit
}
