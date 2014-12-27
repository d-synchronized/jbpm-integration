package uk.co.techblue.producers;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.techblue.annotation.AppLogger;

public class LoggerProducer {

    @Produces
    @AppLogger
    public Logger getApplicationLogger(final InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
    }

}
