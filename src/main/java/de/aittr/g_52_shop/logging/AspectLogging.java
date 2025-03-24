package de.aittr.g_52_shop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLogging {

    private Logger logger = LoggerFactory.getLogger(AspectLogging.class);

    @Pointcut("execution(* de.aittr.g_52_shop.service.ProductServiceImpl.*(..))")
    public void allProductServiceMethods() {
    }

    @Before("allProductServiceMethods()")
    public void beforeAnyProductMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Call method {} with args: {}", methodName, args);
    }

    @After("allProductServiceMethods()")
    public void afterAnyProductMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Method {} has finished", methodName);
    }

//    @Pointcut("execution(* de.aittr.g_52_shop.service.ProductServiceImpl.save(de.aittr.g_52_shop.domain.dto.ProductDto))")
//    public void saveProduct() {}
//
//    @Before("saveProduct()")
//    public void beforeSavingProduct(JoinPoint joinPoint) {
//        Object[] args = joinPoint.getArgs();
//        logger.info("Method save of the class ProductServiceImpl called with args {}", args[0]);
//    }
//
//    @After("saveProduct()")
//    public void afterSavingProducts() {
//        logger.info("Method save of the class ProductServiceImpl finished");
//    }
//
//    @Pointcut("execution(* de.aittr.g_52_shop.service.ProductServiceImpl.getById(Long))")
//    public void getProductById() {}
//
//    @AfterReturning(
//            pointcut = "getProductById()",
//            returning = "result"
//    )
//    public void afterReturningProductById(Object result) {
//        logger.info("Method getById of ProductServiceImpl successfully returned product: {}", result);
//    }
//
    @AfterThrowing(
            pointcut = "getProductById()",
            throwing = "e"
    )
    public void afterThrowingAnyProductMethod(JoinPoint joinPoint, Exception e) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.error("Method {} with args {} has an exception: {}", methodName, args, e.getMessage());
    }
//    public void afterThrowingExceptionWhileGettingProduct(Exception e) {
//        logger.warn("Method getById of ProductServiceImpl threw an exception: {}", e.getMessage());
//    }

}
