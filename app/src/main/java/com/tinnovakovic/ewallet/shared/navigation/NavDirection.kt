package com.tinnovakovic.ewallet.shared.navigation

import androidx.navigation.NamedNavArgument

object NavDirection {

    val Default = object : NavCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destinationRoute = ""

    }

    val homeScreen = object : NavCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destinationRoute = Destination.Home.name

    }

    //TODO:
    //method only required if passing a value to searchScreen
    //refactor to val
    fun searchScreen(
    ) = object : NavCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destinationRoute = "${Destination.Search.name}"
    }

}