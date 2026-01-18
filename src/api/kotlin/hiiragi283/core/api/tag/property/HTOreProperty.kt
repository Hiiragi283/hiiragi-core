package hiiragi283.core.api.tag.property

import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.blockId
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

@JvmRecord
data class HTOreProperty(
    val baseStone: HTHolderLike<Block, *>,
    val property: BlockBehaviour.Properties,
    val stoneTex: ResourceLocation = baseStone.blockId,
)
