package org.bk.samples

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.naming.InitialContext
import javax.sql.DataSource

@Controller
class SampleController {

    @Autowired
    private lateinit var dataSource: DataSource
    

    @RequestMapping("/factoryBean")
    @ResponseBody
    fun factoryBean(): String {
        return "DataSource retrieved from JNDI using JndiObjectFactoryBean: " + dataSource
    }

    @RequestMapping("/direct")
    @ResponseBody
    fun direct(): String {
        return "DataSource retrieved directly from JNDI: " + InitialContext().lookup("java:comp/env/jdbc/sampledb")
    }

}