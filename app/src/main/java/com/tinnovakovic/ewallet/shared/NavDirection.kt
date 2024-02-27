package com.tinnovakovic.ewallet.shared

import androidx.navigation.NamedNavArgument

object NavDirection {

    val Default = object : NavCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destinationRoute = ""

    }

//    val home = object : NavCommand {
//
//        override val arguments = emptyList<NamedNavArgument>()
//
//        override val destinationRoute = Destination.Home.name
//
//    }
//
    fun searchScreen(
    ) = object : NavCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destinationRoute = "${Destination.Search.name}"
    }

}