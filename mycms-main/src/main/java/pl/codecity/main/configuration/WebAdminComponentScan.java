package pl.codecity.main.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(WebAdminComponentScanRegistrar.class)
public @interface WebAdminComponentScan {

	String[] value() default {};

	String[] basePackages() default {};

	Class<?>[] basePackageClasses() default {};
}
