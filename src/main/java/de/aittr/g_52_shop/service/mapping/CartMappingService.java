package de.aittr.g_52_shop.service.mapping;

import de.aittr.g_52_shop.domain.dto.CartDto;
import de.aittr.g_52_shop.domain.entity.Cart;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = ProductMappingService.class)
public interface CartMappingService {

    CartDto mapEntityToDto(Cart entity);
    Cart mapDtoToEntity(CartDto dto);

}
