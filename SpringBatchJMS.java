Application-context.xml
----------------------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:beans="http://www.springframework.org/schema/beans"
  xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="
    http://www.springframework.org/schema/beans 
    http://www.springframework.org/schema/beans/spring-beans.xsd 
    http://www.springframework.org/schema/context 
    http://www.springframework.org/schema/context/spring-context.xsd">
  <context:component-scan base-package="com.nm.illus" />
  <!-- =============================================== -->
  <!-- JMS Common, Define Jms connection factory       -->
  <!-- =============================================== -->
  <!-- Activemq connection factory -->
  <bean id="amqConnectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
    <!-- brokerURL -->
    <constructor-arg index="0" value="tcp://<put the mq address name here:Port" />
  </bean>
  <!-- Pooled Spring connection factory -->
  <bean id="connectionFactory"
    class="org.springframework.jms.connection.CachingConnectionFactory">
    <constructor-arg ref="amqConnectionFactory" />
  </bean>
  <!-- ============================================================= -->
  <!-- JMS receive.                                                  -->
  <!-- Define MessageListenerAdapter and MessageListenerContainer    -->
  <!-- ============================================================= -->
  <bean id="messageListenerAdapter"
    class="org.springframework.jms.listener.adapter.MessageListenerAdapter">
    <constructor-arg ref="jmsMessageListener" />
  </bean>   
  <bean id="messageListenerContainer"
    class="org.springframework.jms.listener.DefaultMessageListenerContainer">
    <property name="connectionFactory" ref="connectionFactory" />
    <property name="destinationName" value="Send2Recv" />
    <property name="messageListener" ref="messageListenerAdapter" />
  </bean> 
  <!-- ============================================================= -->
  <!-- Define your Dal/sync call here                                -->
  <!-- ============================================================= -->  
</beans>
---------------------------------------------------------------------------

* java class *

package com.nm.illus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class JMSSync {
  public static void main(String[] args) {
    // create Spring context
    ApplicationContext ctx = new ClassPathXmlApplicationContext("app-context.xml");         
    // sleep for 1 second
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }   
    // close application context
    ((ClassPathXmlApplicationContext)ctx).close();
  }
}

