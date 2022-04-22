package kogutex.bot;

import io.ktor.client.*;
import io.ktor.client.request.*;
import io.ktor.client.call.*;
import io.ktor.client.plugins.*;
import io.ktor.client.statement.*
import io.ktor.http.*;

import kogutex.model.CategoryRepository;
import kogutex.model.ProductRepository;

data class DiscordUser(
    val id:String,
    val username:String,
    val bot:Boolean
);


data class Message(
    val content:String,

    val tts:Boolean=false,
    val author: DiscordUser?=null,
    val channel_id:String?=null,
    val id:String?=null,
    val type:Int?=null);

class Bot {

    private val API_URL = "https://discord.com/api/v9";

    val token: String;
    val client: HttpClient;
    val command_sign: Char = '!';

    constructor(client:HttpClient, token: String){
        this.token = token;
        this.client = client;
    }

    private fun getURL(internal_url:String):String{
        return this.API_URL + internal_url;
    }

    private suspend fun get(internal_url:String): HttpResponse{
        return this.client.get(this.getURL(internal_url)){
            headers {
                append(HttpHeaders.Authorization, "Bot ${token}")
            }
            contentType(ContentType.Application.Json);
        }
    }

    suspend fun getLastMessage(channel_id:String): Message? {

        val result = this.get("/channels/${channel_id}/messages?limit=1");
        //println(result.body<String>());
        val tmp: Array<Message> = result.body();
        return if(tmp.size==1) tmp[0] else null;
    }

    suspend fun getMessagesAfter(channel_id:String, message:Message): Array<Message> {
        if(message.id==null)return emptyArray()
        
        val result = this.get("/channels/${channel_id}/messages?after=${message.id}");
        return result.body();

    }

    suspend fun createMessage(channel_id:String, content:String): Message{
        val message = Message(content);
        val response = client.post(this.getURL("/channels/${channel_id}/messages")){
            headers {
                append(HttpHeaders.Authorization, "Bot ${token}")
            }
            contentType(ContentType.Application.Json);
            setBody(message);
        }
        return response.body<Message>();
    }

    suspend fun replyToMessage(message: Message, content:String): Message?{
        if(message.channel_id!=null){
            return createMessage(message.channel_id, content);
        }
        return null;
    }

    private fun mRespondToCommand(command:String): String?{
        if(command=="help" || command=="pomoc"){
            
return """Witaj w kogutex! 🐔
Wszystko dla hodowców kurczaków i innego drobiu.
${this.command_sign}pomoc -> pomoc
${this.command_sign}kategorie -> kategorie produktów
${this.command_sign}produkty <category_id> -> produkty z danej kategorii
"""
        }
        else if(command=="kategorie"){
            var res = "Kategorie produktów:\n"
            for(category in CategoryRepository.allCategories()){
                if(category.parent_id!=null) res += " -> "
                res += "[${category.id}] ${category.name}\n"
            }
            return res;
        }
        else if(command.length > 8 && command.substring(0,8)=="produkty"){
            try{
                val tmp = command.split("\\s".toRegex()).toTypedArray()
                val c_id=tmp[1].toInt()
                val category = CategoryRepository.getCategotyById(c_id);
                if(category==null){
                    return "Kategoria o id: ${c_id} nie znaleziona"
                }

                var res = "Produkty z kategorii: [${c_id}] ${category.name}\n"

                val products = ProductRepository.getProductsByCategory(c_id)
                if(products.size==0){
                    res += "Chwilowo brak produktów z danej kategorii";
                }
                else{
                    for(product in products){
                        res+="<${product.id}> ${product.name} - cena ${product.price} zł\n"
                    }
                }
                return res
            }
            catch(e:Exception){
                return "Niepoprawne użycie komendy."
            }
        }
        
        return null;
    }

    suspend fun replyToCommand(message: Message){

        if(message.author!=null && message.author.bot)return;

        var command = message.content.trim()
        println(command)
        
        if(command.length > 0 && command[0] == this.command_sign){
            val responseContent = this.mRespondToCommand(command.substring(1))
            if(responseContent!=null){
                replyToMessage(message, responseContent)
            }
        }
    }

    /*suspend fun getGateway(){
        val response = this.get("/gateway/bot");
        println(response.body<String>());
    }*/
}