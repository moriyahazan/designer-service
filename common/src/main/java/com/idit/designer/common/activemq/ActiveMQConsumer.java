package com.idit.designer.common.activemq;

//@Component
//@RestController
//@RequestMapping("/consume")
public class ActiveMQConsumer {

//    private static final Logger log = LoggerFactory.getLogger(ActiveMQConsumer.class);
//
//    @Autowired
//    private JmsTemplate jmsTemplate;
//
//    @Autowired
//    private Queue queue;
//
//    // Commented the default JMS Listener to test Rest API- which reads message from queue.
//    @JmsListener(destination = DesignerServiceConstants.IDIT_DESIGN_TIME_QUEUE)
//    public void consumeMessage(String message) {
//        log.info("Message received from active message queue::" + message);
//    }
//
//    // API which can also read messages from queue.
//    @GetMapping("/message")
//    public Product consumeMessage() {
//
//        Product product = null;
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            String jsonMessage = (String) jmsTemplate.receiveAndConvert(queue);
//            product = mapper.readValue(jsonMessage, Product.class);
//
//        } catch (Exception e) {
//            log.error("Exception while reading message from queue:" + e.getStackTrace());
//        }
//        return product;
//    }
}