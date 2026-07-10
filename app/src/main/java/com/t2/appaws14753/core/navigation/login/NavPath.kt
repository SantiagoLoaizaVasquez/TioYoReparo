package com.t2.appaws14753.core.navigation.login

object NavPath {
    const val LOGIN = "Login"

    fun getTittle(path: String?): String{
        return when{
            path == LOGIN -> "TioYoReparo"
            else -> "- TioYoReparo -"
        }
    }
}