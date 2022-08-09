package pl.ms.fire.emblem.app.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.thymeleaf.ITemplateEngine
import org.thymeleaf.TemplateEngine
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver

@Configuration
class ThymeleafConfiguration {

    @Bean
    @Scope("singleton")
    fun getTemplateResolver(): ITemplateResolver {
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "templates/"
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.characterEncoding = "UTF-8"
        return templateResolver
    }

    @Bean
    @Scope("singleton")
    fun getTemplateEngine(): ITemplateEngine {
        val engine = TemplateEngine()
        engine.setTemplateResolver(getTemplateResolver())
        engine.addDialect(Java8TimeDialect())

        return engine
    }

}