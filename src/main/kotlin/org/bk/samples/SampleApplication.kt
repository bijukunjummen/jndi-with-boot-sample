package org.bk.samples

import org.apache.catalina.startup.Tomcat
import org.apache.tomcat.util.descriptor.web.ContextResource
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory
import org.springframework.context.annotation.Bean
import org.springframework.jndi.JndiObjectFactoryBean
import javax.sql.DataSource


@SpringBootApplication
class SampleApplication {

    @Bean
    fun jndiDataSource(): DataSource {
        val bean = JndiObjectFactoryBean()
        bean.jndiName = "java:comp/env/jdbc/sampledb"
        bean.setProxyInterface(DataSource::class.java)
        bean.setLookupOnStartup(false)
        bean.afterPropertiesSet()
        return bean.`object` as DataSource
    }

    @Bean
    fun servletContainer(): EmbeddedServletContainerFactory {
        val tomcat = object : TomcatEmbeddedServletContainerFactory() {
            override fun getTomcatEmbeddedServletContainer(
                    tomcat: Tomcat): TomcatEmbeddedServletContainer {
                tomcat.enableNaming()
                return super.getTomcatEmbeddedServletContainer(tomcat)
            }
        }

        tomcat.addContextCustomizers(TomcatContextCustomizer { context ->
            val resource = ContextResource()
            resource.name = "jdbc/sampledb"
            resource.auth = "Container"
            resource.type = "javax.sql.DataSource"
            resource.scope = "Sharable"
            resource.setProperty("driverClassName", "org.h2.Driver")
            resource.setProperty("url", "jdbc:h2:mem:test")
            context.namingResources.addResource(resource)
        })
        return tomcat
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(SampleApplication::class.java, *args)
}

