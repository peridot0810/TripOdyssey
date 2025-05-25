package com.ssafy.pjt.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore.MetadataField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import redis.clients.jedis.JedisPooled;

@Configuration
public class AiConfig {

    @Value("${ssafy.ai.system-prompt}")
    String systemPrompt;

    @Bean
    ChatClient simpleChatClient(ChatClient.Builder builder, ChatMemory memory) {
         return builder.defaultSystem(systemPrompt)
        		 .defaultAdvisors(
        				 new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE-1),
        				 new MessageChatMemoryAdvisor(memory)
        				 )
        		 .build();
    }

 
    @Bean
    ChatMemory chatMemory() {
    	return new InMemoryChatMemory();
    }


     // @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel,
            @Value("${spring.ai.vectorstore.redis.index}") String index,
            @Value("${spring.ai.vectorstore.redis.prefix}") String prefix) {
        RedisVectorStore vs = RedisVectorStore.builder(new JedisPooled("localhost", 6379), embeddingModel)
                .indexName(index)
                .prefix(prefix)
                .metadataFields(                          // Optional: filtering을 위한 metadata field 설정
                        MetadataField.tag("category"), MetadataField.numeric("meta_num"), MetadataField.text("meta_txt"))
                .initializeSchema(true)                   // Optional: schema 초기화 여부(default: false)
                .build();
        return vs;
    }

     // @Bean
    ChatClient ragChatDefaultClient(ChatClient.Builder builder, ChatMemory chatMemory, VectorStore store) {
        SearchRequest request = SearchRequest.builder().topK(3).similarityThreshold(0.75).build();
        return builder.defaultSystem(systemPrompt)
                .defaultAdvisors(new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1))
                //.defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .defaultAdvisors(new QuestionAnswerAdvisor(store, request))
                .build();
    }

     // @Bean
    ChatClient ragChatCustomClient(ChatClient.Builder builder, ChatMemory chatMemory, VectorStore store, @Value("${ssafy.ai.rag-prompt}") String ragPrompt) {
        SearchRequest request = SearchRequest.builder().topK(3).similarityThreshold(0.75).build();
        return builder.defaultSystem(systemPrompt)
                .defaultAdvisors(new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1))
                //.defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .defaultAdvisors(new QuestionAnswerAdvisor(store, request, ragPrompt))
                .build();
    }

}
