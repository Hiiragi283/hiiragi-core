package hiiragi283.lib.data

import hiiragi283.lib.util.HTBuilderMarker
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet

@HTBuilderMarker
class HolderSetBuilder<T : Any> {
    private var contents: MutableList<Holder<T>> = mutableListOf()

    operator fun Holder<T>.unaryPlus() {
        check(this.delegate is Holder.Reference<T>) { "Cannot serialize give holder $this" }
        contents += this
    }

    fun build(): HolderSet<T> = HolderSet.direct(contents)
}
