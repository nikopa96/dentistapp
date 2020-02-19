package com.cgi.dentistapp.utils;

import com.cgi.dentistapp.dto.AbstractDTO;
import com.cgi.dentistapp.entity.AbstractEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DTOConverter {

    private ModelMapper modelMapper = new ModelMapper();

    public AbstractDTO convertToDTO(AbstractEntity abstractEntity, AbstractDTO abstractDTO) {
        return modelMapper.map(abstractEntity, abstractDTO.getClass());
    }

    public AbstractEntity convertToEntity(AbstractDTO abstractDTO, AbstractEntity abstractEntity) {
        return modelMapper.map(abstractDTO, abstractEntity.getClass());
    }
}
