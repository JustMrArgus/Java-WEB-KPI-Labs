package com.cosmocats.cosmo_cats_api.aspect;

import com.cosmocats.cosmo_cats_api.exception.FeatureNotAvailableException;
import com.cosmocats.cosmo_cats_api.service.FeatureToggleService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    @Around("@annotation(com.cosmocats.cosmo_cats_api.aspect.FeatureToggle)")
    public Object checkFeatureToggle(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        FeatureToggle featureToggle = method.getAnnotation(FeatureToggle.class);

        String featureName = featureToggle.featureName();

        if (featureToggleService.isFeatureEnabled(featureName)) {
            return joinPoint.proceed();
        } else {
            throw new FeatureNotAvailableException(featureName);
        }
    }
}