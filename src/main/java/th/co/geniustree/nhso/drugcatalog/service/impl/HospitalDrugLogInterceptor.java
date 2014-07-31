/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;

/**
 *
 * @author moth
 */
@Aspect
//@Component
@Deprecated
public class HospitalDrugLogInterceptor{

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(HospitalDrugLogInterceptor.class);


    @Around("execution(* th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo.save(..))")
    public Object anyOldTransfer(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();
        if(proceed instanceof HospitalDrug){
            
        }
        return proceed;
    }// the pointcut signature
}
