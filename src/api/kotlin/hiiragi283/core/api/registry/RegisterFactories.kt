package hiiragi283.core.api.registry

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.Function

typealias IdToFunction<R> = Function<ResourceLocation, R>

typealias BlockFactory<BLOCK> = (BlockBehaviour.Properties) -> BLOCK

typealias ItemFactory<ITEM> = (Item.Properties) -> ITEM

typealias BlockWithContextFactory<C, BLOCK> = (C, BlockBehaviour.Properties) -> BLOCK

typealias ItemWithContextFactory<C, ITEM> = (C, Item.Properties) -> ITEM
