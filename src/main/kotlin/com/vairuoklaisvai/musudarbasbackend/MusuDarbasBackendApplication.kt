package com.vairuoklaisvai.musudarbasbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MusuDarbasBackendApplication

fun main(args: Array<String>) {
    println("test")
    runApplication<MusuDarbasBackendApplication>(*args)
}
