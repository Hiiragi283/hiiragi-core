package hiiragi283.core.api.storage.stack

import hiiragi283.core.api.stack.ImmutableStack

fun interface HTStackSetter<STACK : ImmutableStack<*, STACK>> {
    fun setStack(stack: STACK?)
}
