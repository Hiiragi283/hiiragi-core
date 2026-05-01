package hiiragi283.core.api.registry

import net.minecraft.core.component.DataComponentHolder

interface StackTemplate<T : Any> :
    TypedInstance<T>,
    DataComponentHolder
