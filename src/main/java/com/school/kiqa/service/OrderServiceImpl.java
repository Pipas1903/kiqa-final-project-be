package com.school.kiqa.service;

import com.school.kiqa.command.dto.order.CreateOrUpdateOrderDto;
import com.school.kiqa.command.dto.order.OrderDetailsDto;
import com.school.kiqa.command.dto.orderProduct.CreateOrUpdateOrderProductDto;
import com.school.kiqa.converter.AddressConverter;
import com.school.kiqa.converter.ColorConverter;
import com.school.kiqa.converter.OrderConverter;
import com.school.kiqa.converter.OrderProductConverter;
import com.school.kiqa.converter.ProductConverter;
import com.school.kiqa.exception.alreadyExists.AlreadyExistsException;
import com.school.kiqa.exception.notFound.ColorNotFoundException;
import com.school.kiqa.exception.notFound.OrderNotFoundException;
import com.school.kiqa.exception.notFound.OrderProductNotFoundException;
import com.school.kiqa.exception.notFound.ProductNotFoundException;
import com.school.kiqa.exception.notFound.UserNotFoundException;
import com.school.kiqa.persistence.entity.AddressEntity;
import com.school.kiqa.persistence.entity.ColorEntity;
import com.school.kiqa.persistence.entity.OrderEntity;
import com.school.kiqa.persistence.entity.OrderProductEntity;
import com.school.kiqa.persistence.entity.ProductEntity;
import com.school.kiqa.persistence.entity.UserEntity;
import com.school.kiqa.persistence.repository.ColorRepository;
import com.school.kiqa.persistence.repository.OrderProductRepository;
import com.school.kiqa.persistence.repository.OrderRepository;
import com.school.kiqa.persistence.repository.ProductRepository;
import com.school.kiqa.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.school.kiqa.exception.ErrorMessageConstants.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;
    private final ColorRepository colorRepository;
    private final OrderProductConverter orderProductConverter;
    private final OrderConverter orderConverter;
    private final AddressConverter addressConverter;
    private final ProductConverter productConverter;
    private final ColorConverter colorConverter;


    @Override
    public OrderDetailsDto addProductToOrder(CreateOrUpdateOrderProductDto orderProductDto, Long orderId, Long userId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error(String.format(ORDER_NOT_FOUND, orderId));
                    return new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
                });

        order.getOrderProductEntityList().forEach(orderProduct -> {
            if (Objects.equals(orderProduct.getColor().getId(), orderProductDto.getColorId()) &&
                    Objects.equals(orderProduct.getProduct().getId(), orderProductDto.getProductId())
            )
                throw new AlreadyExistsException("Product with same color is already in cart");
        });

        final OrderProductEntity savedOrderProduct = createOrderProduct(orderProductDto);

        List<OrderProductEntity> list = order.getOrderProductEntityList();
        list.add(savedOrderProduct);
        log.info("Added orderProduct to order list");

        order.setOrderProductEntityList(list);
        log.info("Set orderProductList");

        final var savedOrder = orderRepository.save(order);
        log.info("Saved order changes to database");
        return orderConverter.convertEntityToOrderDetailsDto(savedOrder);
    }

    @Override
    public OrderDetailsDto createOrder(CreateOrUpdateOrderDto orderDto, Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);

        OrderEntity orderEntity = orderConverter.convertDtoToOrderEntity(orderDto);

        final AddressEntity[] sendingAddress = new AddressEntity[1];

        sendingAddress[0] = addressConverter.convertCreateDtoToAddressEntity(orderDto.getAddressDto());
        sendingAddress[0].setIsMain(true);

        user.ifPresent(userEntity -> {
            orderEntity.setUserEntity(userEntity);
            userEntity.getAddressEntities().stream()
                    .filter(address ->
                            address.getCityName().equals(sendingAddress[0].getCityName()) &&
                                    address.getStreetName().equals(sendingAddress[0].getStreetName()) &&
                                    address.getDoorNumber().equals(sendingAddress[0].getDoorNumber()) &&
                                    address.getFloorNumber().equals(sendingAddress[0].getFloorNumber()))
                    .findFirst()
                    .ifPresent(address -> sendingAddress[0] = address);
            orderEntity.setUserEntity(userEntity);
        });

        orderEntity.setSendingAddress(sendingAddress[0]);
        log.info("set sending address");

        orderEntity.setStatus(true);
        log.info("open status set to 'true'");

        List<OrderProductEntity> orderProductEntityList = orderDto.getProductDtos().stream()
                .map(this::createOrderProduct)
                .collect(Collectors.toList());
        log.info("Created orderProductEntity list");

        orderEntity.setOrderProductEntityList(orderProductEntityList);
        log.info("set orderProductEntity list");

        orderEntity.setTotalPrice(orderProductEntityList.stream()
                .map(orderProductEntity -> orderProductEntity.getProduct().getPrice() * orderProductEntity.getQuantity())
                .mapToDouble(Double::doubleValue)
                .sum()
        );
        log.info("set total price to {} €", orderEntity.getTotalPrice());

        final OrderEntity savedOrder = orderRepository.save(orderEntity);
        log.info("Saved order to database with id {}", savedOrder.getId());

        orderProductEntityList.forEach(orderProduct -> orderProduct.setOrderEntity(savedOrder));

        OrderDetailsDto orderDetailsDto = orderConverter.convertEntityToOrderDetailsDto(savedOrder);
        orderDetailsDto.setAddressDetailsDto(addressConverter.convertEntityToAddressDetailsDto(savedOrder.getSendingAddress()));

        final var orderProductDetails = savedOrder.getOrderProductEntityList().stream()
                .map(orderProductEntity -> {
                    final var converted = orderProductConverter.convertEntityToOrderProductDetailsDto(orderProductEntity);
                    if (orderProductEntity.getColor() != null)
                        converted.setColorId(orderProductEntity.getColor().getId());
                    converted.setOrderId(savedOrder.getId());
                    return converted;
                })
                .collect(Collectors.toList());
        orderDetailsDto.setOrderProductDetailsDtoList(orderProductDetails);
        log.info("retrieving details of the order");
        return orderDetailsDto;
    }

    @Override
    public OrderDetailsDto updateProductQuantity(Long orderProductId, Long orderId, int quantity, Long userId) {
        OrderProductEntity orderProductEntity = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> {
                    log.warn("order product with id {} does not exist", orderProductId);
                    throw new OrderProductNotFoundException(String.format(ORDER_PRODUCT_NOT_FOUND, orderProductId));
                });

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("order with id {} does not exist", orderId);
                    throw new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
                });

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", userId);
                    throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
                });

        orderProductEntity.setQuantity(quantity);
        final var savedOrder = orderRepository.save(orderEntity);
        savedOrder.setUserEntity(userEntity);
        return orderConverter.convertEntityToOrderDetailsDto(savedOrder);

        //orderEntity.setTotalPrice();

    }

    @Override
    public OrderDetailsDto removeProductFromOrder(Long orderProductId, Long orderId, Long userId) {
        OrderProductEntity orderProductEntity = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> {
                    log.warn("order product with id {} does not exist", orderProductId);
                    throw new OrderProductNotFoundException(String.format(ORDER_PRODUCT_NOT_FOUND, orderProductId));
                });

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("order with id {} does not exist", orderId);
                    throw new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
                });

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", userId);
                    throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
                });

        orderEntity.getOrderProductEntityList().remove(orderProductId);
        final var savedOrder = orderRepository.save(orderEntity);
        savedOrder.setUserEntity(userEntity);
        return orderConverter.convertEntityToOrderDetailsDto(savedOrder);
    }

    @Override
    public OrderDetailsDto incrementProductQuantity(Long orderProductId, Long orderId, Long userId) {
        OrderProductEntity orderProductEntity = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> {
                    log.warn("order product with id {} does not exist", orderProductId);
                    throw new OrderProductNotFoundException(String.format(ORDER_PRODUCT_NOT_FOUND, orderProductId));
                });

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("order with id {} does not exist", orderId);
                    throw new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
                });

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", userId);
                    throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
                });

        orderProductEntity.setQuantity(orderProductEntity.getQuantity() + 1);

        final var savedOrder = orderRepository.save(orderEntity);
        savedOrder.setUserEntity(userEntity);
        return orderConverter.convertEntityToOrderDetailsDto(savedOrder);
    }

    @Override
    public OrderDetailsDto decrementProductQuantity(Long orderProductId, Long orderId, Long userId) {

        OrderProductEntity orderProductEntity = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> {
                    log.warn("order product with id {} does not exist", orderProductId);
                    throw new OrderProductNotFoundException(String.format(ORDER_PRODUCT_NOT_FOUND, orderProductId));
                });

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("order with id {} does not exist", orderId);
                    throw new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
                });

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", userId);
                    throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
                });

        orderProductEntity.setQuantity(orderProductEntity.getQuantity() - 1);

        final var savedOrder = orderRepository.save(orderEntity);
        savedOrder.setUserEntity(userEntity);
        return orderConverter.convertEntityToOrderDetailsDto(savedOrder);
    }

    @Override
    public OrderDetailsDto finishOrder(Long orderId, Long userId) {
        return null;
    }


    private OrderProductEntity createOrderProduct(CreateOrUpdateOrderProductDto orderProductDto) {
        OrderProductEntity orderProduct = orderProductConverter.convertDtoToOrderProductEntity(orderProductDto);
        log.info("orderProductDto converted to orderProductEntity");

        ProductEntity product = productRepository.findById(orderProductDto.getProductId())
                .orElseThrow(() -> {
                    log.error(String.format(PRODUCT_NOT_FOUND, orderProductDto.getProductId()));
                    return new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, orderProductDto.getProductId()));
                });

        orderProduct.setProduct(product);
        log.info("Product respective to OrderProduct set to {}", product.getId());

        orderProduct.setIsActive(product.getIsActive());
        log.info("OrderProduct active status set to '{}'", product.getIsActive());

        if (orderProductDto.getColorId() != 0) {
            ColorEntity color = colorRepository.findById(orderProductDto.getColorId())
                    .orElseThrow(() -> {
                        log.error(String.format(COLOR_NOT_FOUND, orderProductDto.getColorId()));
                        return new ColorNotFoundException(String.format(COLOR_NOT_FOUND, orderProductDto.getColorId()));
                    });

            orderProduct.setColor(color);
            log.info("OrderProduct color set to {}", color.getHexValue());
        }

        final var savedOrderProduct = orderProductRepository.save(orderProduct);
        log.info("OrderProduct saved to data base");

        return savedOrderProduct;
    }
}
