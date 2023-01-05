package me.nerminsehic.groupevent.config;

import me.nerminsehic.groupevent.dto.EventDto;
import me.nerminsehic.groupevent.entity.Event;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<Event, EventDto> eventMapping = mapper.createTypeMap(Event.class, EventDto.class);
        eventMapping.addMapping(Event::getInvites, EventDto::setAttendees);

        return mapper;
    }
}
