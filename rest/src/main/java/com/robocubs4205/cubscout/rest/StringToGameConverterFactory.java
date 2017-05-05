package com.robocubs4205.cubscout.rest;


import com.robocubs4205.cubscout.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManagerFactory;

@Component
public class StringToGameConverterFactory implements ConverterFactory<String,Game>{
    private final PersistenceManagerFactory pmf;

    @Autowired
    public StringToGameConverterFactory(PersistenceManagerFactory pmf){
        super();
        this.pmf = pmf;
    }
    @Override
    public <T extends Game> Converter<String, T> getConverter(Class<T> targetType) {
        return source -> {
            try{
                return (T)pmf.getPersistenceManager().getObjectById(Game.class, source);
            }
            catch (JDOObjectNotFoundException e){
                throw new ResourceNotFoundException(e);
            }
        };
    }
}
