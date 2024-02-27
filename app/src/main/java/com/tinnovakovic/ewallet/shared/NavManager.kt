package com.tinnovakovic.ewallet.shared

import com.tinnovakovic.ewallet.shared.NavDirection.Default
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * NavManager must be a singleton instance. That way we can ensure that every class communicating
 * with the NavManager is referencing the same instance.
 */
class NavManager {

    // The commands reference can be used by an outside class to observe the triggered NavCommands
    var commands: MutableStateFlow<NavCommand> = MutableStateFlow(value = Default)

    // The navigate function can be used to trigger the navigation based on the provided NavCommand
    fun navigate(
        direction: NavCommand
    ) {
        commands.value = direction
    }

}