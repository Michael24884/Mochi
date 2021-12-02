package com.izanaminightz.mochi

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}