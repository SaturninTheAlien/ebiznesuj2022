package kogutex.bot;

import kotlinx.coroutines.delay

/*
Tymczasowo http polling.
Kiedyś postaram się zrobić wersję w oparciu o WebScoket
*/

class BotHttpPollingService(val bot:Bot, val period:Long=10000){

    var lastMessage: Message? = null;

    suspend fun runForeverOnChannel(channel_id:String){
        while(true){
            if(lastMessage==null){
                lastMessage = bot.getLastMessage(channel_id);
                if(lastMessage!=null){
                    bot.replyToCommand(lastMessage!!);
                }
            }
            else{
                val messagesAfter = bot.getMessagesAfter(channel_id, lastMessage!!);
                if(messagesAfter.size>0){
                    messagesAfter.forEach { message -> bot.replyToCommand(message)}
                    lastMessage = messagesAfter.last();
                }
            }
            delay(period);
        }
    }
}