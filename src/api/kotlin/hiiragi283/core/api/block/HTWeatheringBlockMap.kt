package hiiragi283.core.api.block

import hiiragi283.core.api.registry.HTBlockHolderLike

/**
 * 銅系ブロックを管理するクラスです。
 * @param base 錆止めされていないブロックの一覧
 * @param waxed 錆止めされたブロックの一覧
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 */
@JvmRecord
data class HTWeatheringBlockMap(
    val base: Map<HTWeatheringLevel, HTBlockHolderLike<*>>,
    val waxed: Map<HTWeatheringLevel, HTBlockHolderLike<*>>,
) {
    val allBlocks: Sequence<HTBlockHolderLike<*>> get() = base.values.plus(waxed.values).asSequence()

    operator fun get(level: HTWeatheringLevel): Pair<HTBlockHolderLike<*>, HTBlockHolderLike<*>>? {
        val baseBlock: HTBlockHolderLike<*> = base[level] ?: return null
        val waxedBlock: HTBlockHolderLike<*> = waxed[level] ?: return null
        return baseBlock to waxedBlock
    }

    inline fun forEach(action: (base: HTBlockHolderLike<*>, waxed: HTBlockHolderLike<*>) -> Unit) {
        for (level: HTWeatheringLevel in HTWeatheringLevel.entries) {
            val baseBlock: HTBlockHolderLike<*> = base[level] ?: continue
            val waxedBlock: HTBlockHolderLike<*> = waxed[level] ?: continue
            action(baseBlock, waxedBlock)
        }
    }
}
