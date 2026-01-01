package hiiragi283.core.api

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.IMekanismAccess
 */
interface HiiragiCoreAccess {
    companion object {
        @JvmField
        val INSTANCE: HiiragiCoreAccess = HiiragiCoreAPI.getService()
    }

    fun getModIdPriorityList(): List<String>
}
