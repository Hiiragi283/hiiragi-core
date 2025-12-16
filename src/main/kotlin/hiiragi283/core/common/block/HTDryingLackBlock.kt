package hiiragi283.core.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.setup.HCBlockEntityTypes
import net.minecraft.world.level.block.HorizontalDirectionalBlock

class HTDryingLackBlock(properties: Properties) :
    HorizontalDirectionalBlock(properties),
    HTBlockWithEntity {
    override fun codec(): MapCodec<out HorizontalDirectionalBlock> = throw UnsupportedOperationException()

    override fun getBlockEntityType(): HTDeferredBlockEntityType<*> = HCBlockEntityTypes.DRYING_LACK
}
