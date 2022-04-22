/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package kogutex


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.*
import io.ktor.http.*

import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

import java.util.*
import java.io.File;


import kogutex.bot.Bot
import kogutex.bot.BotHttpPollingService

suspend fun createDiscordBot(bot_token:String){
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }

    val bot = Bot(client, bot_token)
    /*val message = bot.getLastMessage("966050401478639687")
    println(message)
    if(message!=null)
    {
        println(message.content);
    }*/

    val bps = BotHttpPollingService(bot);
    bps.runForeverOnChannel("967140711705215047");

    client.close()
}


suspend fun main(){
    val token = File("token.conf").readText(Charsets.UTF_8);
    createDiscordBot(token);
}
