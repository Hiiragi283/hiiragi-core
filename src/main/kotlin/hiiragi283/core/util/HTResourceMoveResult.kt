package hiiragi283.core.util

import hiiragi283.core.api.storage.resource.HTResourceType

/**
 * @see net.neoforged.neoforge.fluids.FluidActionResult
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTResourceMoveResult<RESOURCE : HTResourceType<*>> private constructor(
    val succeeded: Boolean,
    val remainder: RESOURCE?,
    val amount: Int,
) {
    companion object {
        @JvmStatic
        fun <RESOURCE : HTResourceType<*>> failed(): HTResourceMoveResult<RESOURCE> = HTResourceMoveResult(false, null, 0)

        @JvmStatic
        fun <RESOURCE : HTResourceType<*>> succeeded(remainder: RESOURCE?, amount: Int): HTResourceMoveResult<RESOURCE> =
            HTResourceMoveResult(true, remainder, amount)
    }
}
