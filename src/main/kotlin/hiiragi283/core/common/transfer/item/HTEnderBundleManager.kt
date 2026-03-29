package hiiragi283.core.common.transfer.item

import hiiragi283.core.api.HTDyeColor
import hiiragi283.core.api.transfer.ItemResourceHandler
import hiiragi283.core.setup.HCAttachmentTypes
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler
import java.util.EnumMap

class HTEnderBundleManager private constructor(private val map: MutableMap<HTDyeColor, ItemStacksResourceHandler>) : ValueIOSerializable {
    companion object {
        @JvmStatic
        fun getManager(server: MinecraftServer): HTEnderBundleManager = server.overworld().getData(HCAttachmentTypes.ENDER_BUNDLE)

        @JvmStatic
        fun getOrCreateHandler(server: MinecraftServer, color: HTDyeColor): ItemResourceHandler =
            getManager(server).getOrCreateHandler(color)

        @JvmStatic
        fun createHandler(): ItemStacksResourceHandler = ItemStacksResourceHandler(27)
    }

    constructor() : this(EnumMap(HTDyeColor::class.java))

    fun getOrCreateHandler(color: HTDyeColor): ItemResourceHandler = map.computeIfAbsent(color) { createHandler() }

    override fun serialize(output: ValueOutput) {
        for (color: HTDyeColor in HTDyeColor.entries) {
            val handler: ItemStacksResourceHandler = map[color] ?: continue
            output.putChild(color.serializedName, handler)
        }
    }

    override fun deserialize(input: ValueInput) {
        for (color: HTDyeColor in HTDyeColor.entries) {
            val key: String = color.serializedName
            if (input.child(key).isPresent) {
                val handler = createHandler()
                input.readChild(key, handler)
                map[color] = handler
            }
        }
    }
}
