package com.tinnovakovic.ewallet.shared.navigation

import androidx.navigation.NamedNavArgument

/**
 * NavCommand: Defines the requirements for a navigation event
 * Currently only arguments and destination are required but this interface can grow as requirements grow
 */
interface NavCommand {

    val arguments: List<NamedNavArgument>

    val destinationRoute: String

}