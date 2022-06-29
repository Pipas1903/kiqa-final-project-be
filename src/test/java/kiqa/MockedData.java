package kiqa;

import com.school.kiqa.command.dto.address.AddressDetailsDto;
import com.school.kiqa.command.dto.address.CreateOrUpdateAddressDto;
import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UpdateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.converter.AddressConverter;
import com.school.kiqa.converter.OrderConverter;
import com.school.kiqa.enums.UserType;
import com.school.kiqa.persistence.entity.AddressEntity;
import com.school.kiqa.persistence.entity.BrandEntity;
import com.school.kiqa.persistence.entity.CategoryEntity;
import com.school.kiqa.persistence.entity.ColorEntity;
import com.school.kiqa.persistence.entity.OrderEntity;
import com.school.kiqa.persistence.entity.OrderProductEntity;
import com.school.kiqa.persistence.entity.ProductEntity;
import com.school.kiqa.persistence.entity.ProductTypeEntity;
import com.school.kiqa.persistence.entity.UserEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
public class MockedData {

    private static AddressConverter addressConverter;
    private static OrderConverter orderConverter;

    // USER

    public static List<UserEntity> getUserList() {
        return Collections.singletonList(getMockedUserEntity());
    }

    public static UserEntity getMockedUserEntity() {
        return UserEntity.builder()
                .id(5L)
                .name("Filipo")
                .password("P@ssword123")
                .email("nasco@email.com")
                .dateOfBirth(LocalDate.of(1996, 3, 10))
                .vat(674565)
                .phoneNumber("4567888")
                .userType(UserType.USER)
                .addressEntities(Collections.singletonList())
                .orderEntityList()
                .build();
    }

    public static UserDetailsDto getUserDetailsDto(UserEntity entity) {
        return UserDetailsDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .dateOfBirth(entity.getDateOfBirth())
                .vat(entity.getVat())
                .phoneNumber(entity.getPhoneNumber())
                .addressList(entity.getAddressEntities().stream().map(addressConverter::convertEntityToAddressDetailsDto).collect(Collectors.toList()))
                .orderHistory(entity.getOrderEntityList().stream().map(orderConverter::convertEntityToOrderDetailsDto).collect(Collectors.toList()))
                .email(entity.getEmail())
                .build();
    }

    public static CreateUserDto getCreateUserDto() {
        return CreateUserDto.builder()
                .name("Rafa")
                .password("P@ssword123")
                .email("email@email.com")
                .dateOfBirth(LocalDate.of(2000, 8, 30))
                .vat(2222)
                .phoneNumber("912456789")
                .mainAddress(getCreateOrUpdateAddressDto())
                .build();
    }


    public static UpdateUserDto getUpdateUserDto() {
        return UpdateUserDto.builder()
                .name("ricas")
                .password("jeriewi339")
                .dateOfBirth(LocalDate.of(1990, 12, 3))
                .vat(2222)
                .phoneNumber("12345949")
                .addressList(Collections.singletonList(getCreateOrUpdateAddressDto2()))
                .build();
    }

    // ADDRESS

    public static CreateOrUpdateAddressDto getCreateOrUpdateAddressDto() {
        return CreateOrUpdateAddressDto.builder()
                .cityName("porto")
                .country("portugal")
                .doorNumber("42")
                .floorNumber("2")
                .streetName("rua wawe")
                .zipCode("4440-450")
                .build();
    }

    public static CreateOrUpdateAddressDto getCreateOrUpdateAddressDto2() {
        return CreateOrUpdateAddressDto.builder()
                .cityName("lisboa")
                .country("portugal")
                .doorNumber("22")
                .floorNumber("19")
                .streetName("rua jffj")
                .zipCode("4100-4321")
                .build();
    }

    public static AddressEntity getMockedAddressEntity() {
        return AddressEntity.builder()
                .id(3L)
                .streetName("rua sei la")
                .doorNumber("328")
                .floorNumber("2")
                .zipCode("4939-490")
                .cityName("Minime")
                .country("Portugal")
                .isMain(true)
                .userEntity(getMockedUserEntity())
                .orders()
                .build();
    }

    public static List<AddressEntity> getAddressEntityList() {
        return Collections.singletonList(getMockedAddressEntity());
    }


    public static AddressDetailsDto getAddressDetailsDto(AddressEntity addressEntity) {
        return AddressDetailsDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .dateOfBirth(entity.getDateOfBirth())
                .vat(entity.getVat())
                .phoneNumber(entity.getPhoneNumber())
                .addressList(entity.getAddressEntities().stream().map(addressConverter::convertEntityToAddressDetailsDto).collect(Collectors.toList()))
                .orderHistory(entity.getOrderEntityList().stream().map(orderConverter::convertEntityToOrderDetailsDto).collect(Collectors.toList()))
                .email(entity.getEmail())
                .build();
    }

    // ORDER

    public static OrderEntity getMockedOrderEntity() {
        return OrderEntity.builder()
                .id(1L)
                .totalPrice(50D)
                .status(true)
                .creationDate(LocalDate.of(2000, 5, 20))
                .orderProductEntityList(Collections.singletonList())
                .userEntity()
                .sendingAddress()
                .build();
    }

    public static List<OrderEntity> getAOrderEntityList() {
        return Collections.singletonList(getMockedOrderEntity());
    }


    // ORDER PRODUCT

    public static OrderProductEntity getMockedOrderProductEntity() {
        return OrderProductEntity.builder()
                .id(7L)
                .quantity(3)
                .product()
                .color()
                .isActive()
                .orderEntity()
                .build();
    }

    public static List<OrderProductEntity> getOrderProductEntityList() {
        return Collections.singletonList(getMockedOrderProductEntity());
    }

    // PRODUCT

    public static ProductEntity getMockedProductEntity() {
        return ProductEntity.builder()
                .id(6L)
                .price(30D)
                .description("abcd")
                .name("nunca vi")
                .isActive(true)
                .image("imagemDoJoao")
                .brandEntity(getMockedBrandEntity())
                .productTypeEntity()
                .categoryEntity()
                .colors()
                .build();
    }

    public static List<ProductEntity> getProductEntityList() {
        return Collections.singletonList(getMockedProductEntity());
    }


    // BRAND

    public static BrandEntity getMockedBrandEntity() {
        return BrandEntity.builder()
                .id(8L)
                .name("joaoBrand")
                .imageLink("joaoLink")
                .productEntityList(Collections.singletonList(getMockedProductEntity()))
                .build();
    }

    public static List<BrandEntity> getBrandEntityList() {
        return Collections.singletonList(getMockedBrandEntity());
    }


    // Product Type

    public static ProductTypeEntity getMockedProductTypeEntity() {
        return ProductTypeEntity.builder()
                .id(6L)
                .name("nevermind")
                .productEntityList(Collections.singletonList(getMockedProductEntity()))
                .category()
                .build();
    }

    public static List<ProductTypeEntity> getProductTypeEntityList() {
        return Collections.singletonList(getMockedProductTypeEntity());
    }


    // CATEGORY

    public static CategoryEntity getMockedCategoryEntity() {
        return CategoryEntity.builder()
                .id(2L)
                .name("lips")
                .productEntityList(Collections.singletonList(getMockedProductEntity()))
                .productTypeEntities(Collections.singletonList(getMockedProductTypeEntity()))
                .build();
    }

    public static List<CategoryEntity> getCategoryEntityList() {
        return Collections.singletonList(getMockedCategoryEntity());
    }


    // COLOR

    public static ColorEntity getMockedColorEntity() {
        return ColorEntity.builder()
                .id(5L)
                .hexValue("#3456")
                .colorName("blue")
                .productEntityList(Collections.singletonList(getMockedProductEntity()))
                .orderProductEntityList()
                .build();
    }

    public static List<ColorEntity> getColorEntityList() {
        return Collections.singletonList(getMockedColorEntity());
    }

} */
