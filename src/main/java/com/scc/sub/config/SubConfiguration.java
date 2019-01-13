package com.scc.sub.config;

import com.scc.sub.model.Event;
import com.scc.sub.services.SinkService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Created by AnthonyLenovo on 06/01/2019.
 */
@Configuration
public class SubConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SubConfiguration.class);

    @Value("${spring.cloud.stream.bindings.input.destination}")
    private String SUBSCRIPTION_NAME;

    @Autowired
    private SinkService sinkService;

    @Bean
    public DirectChannel pubSubInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(
            @Qualifier("pubSubInputChannel") MessageChannel inputChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, SUBSCRIPTION_NAME);
        adapter.setOutputChannel(inputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(Event.class);
        return adapter;
    }

    @SuppressWarnings("unchecked")
	@ServiceActivator(inputChannel = "pubSubInputChannel")
    public void messageReceiver(Event payload,
                                @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
    	logger.info("Message arrived to Producer! Payload: {}",payload);
        sinkService.handleMessage(payload);
        message.ack();
    }

}
