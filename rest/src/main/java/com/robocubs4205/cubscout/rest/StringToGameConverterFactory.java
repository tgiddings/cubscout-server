package com.robocubs4205.cubscout.rest;


import com.robocubs4205.cubscout.model.Game;
import com.robocubs4205.cubscout.model.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import javax.jdo.JDOObjectNotFoundException;

@Component
public class StringToGameConverterFactory implements ConverterFactory<String,Game>, ConditionalConverter{
    private final GameRepository gameRepository;

    @Autowired
    public StringToGameConverterFactory(GameRepository gameRepository){
        super();
        this.gameRepository = gameRepository;
    }
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Game> Converter<String, T> getConverter(Class<T> targetType) {
        return source -> {
            try{
                return (T) gameRepository.find(Integer.parseInt(source));
            }
            catch (JDOObjectNotFoundException e){
                throw new ResourceNotFoundException(e);
            }
        };
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getType().equals(Game.class);
    }
}
