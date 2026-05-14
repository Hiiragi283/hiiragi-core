package hiiragi283.core.common.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockState

class HTWeatheringCopperBasinBlock(private val weatherState: WeatheringCopper.WeatherState, properties: Properties) :
    HTCopperBasinBlock(properties),
    WeatheringCopper {
    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        changeOverTime(state, level, pos, random)
    }

    override fun isRandomlyTicking(state: BlockState): Boolean = super.isRandomlyTicking(state) && WeatheringCopper.getNext(state.block).isPresent

    //    WeatheringCopper    //

    override fun getAge(): WeatheringCopper.WeatherState = weatherState

    /*override fun changeOverTime(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        val chance = 0.05688889f
        if (random.nextFloat() < chance) {
            getNextState(state, level, pos, random).ifPresent { newState: BlockState ->
                val basinBlockEntity: HTCopperBasinBlockEntity =
                    level.getTypedBlockEntity<HTCopperBasinBlockEntity>(pos) ?: return@ifPresent
                val access: RegistryAccess = level.registryAccess()
                val tag: CompoundTag = basinBlockEntity.saveWithoutMetadata(access)

                level.setBlockAndUpdate(pos, newState)
                level.getBlockEntity(pos)?.loadWithComponents(tag, access)
            }
        }
    }*/
}
