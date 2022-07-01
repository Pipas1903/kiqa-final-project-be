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
import com.school.kiqa.exception.notFound.AddressNotFound;
import com.school.kiqa.exception.notFound.ColorNotFoundException;
import com.school.kiqa.exception.notFound.OrderNotFoundException;
import com.school.kiqa.exception.notFound.OrderProductNotFoundException;
import com.school.kiqa.exception.notFound.ProductNotFoundException;
import com.school.kiqa.exception.notFound.SessionNotFoundException;
import com.school.kiqa.exception.notFound.UserNotFoundException;
import com.school.kiqa.persistence.entity.AddressEntity;
import com.school.kiqa.persistence.entity.ColorEntity;
import com.school.kiqa.persistence.entity.OrderEntity;
import com.school.kiqa.persistence.entity.OrderProductEntity;
import com.school.kiqa.persistence.entity.ProductEntity;
import com.school.kiqa.persistence.entity.SessionEntity;
import com.school.kiqa.persistence.entity.UserEntity;
import com.school.kiqa.persistence.repository.AddressRepository;
import com.school.kiqa.persistence.repository.ColorRepository;
import com.school.kiqa.persistence.repository.OrderProductRepository;
import com.school.kiqa.persistence.repository.OrderRepository;
import com.school.kiqa.persistence.repository.ProductRepository;
import com.school.kiqa.persistence.repository.SessionRepository;
import com.school.kiqa.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final SessionRepository sessionRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;
    private final ColorRepository colorRepository;
    private final OrderProductConverter orderProductConverter;
    private final OrderConverter orderConverter;
    private final AddressConverter addressConverter;


    @Override
    public OrderDetailsDto getOrderDetails(Long userId, Long orderId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error(String.format(USER_NOT_FOUND, userId));
                    return new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
                });

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error(String.format(ORDER_NOT_FOUND, orderId));
                    return new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
                });

        if (!Objects.equals(orderEntity.getUserEntity().getId(), userEntity.getId())) {
            log.info(String.format(INVALID_ORDER_ID, orderId, userId));
            throw new OrderNotFoundException(String.format(INVALID_ORDER_ID, orderId, userId));
        }

        log.info("Retrieved order from database");
        final var order = putLists(orderEntity);
        order.setUserId(userId);
        log.info("Converted order to order details {}", order.getId());
        return order;
    }

    @Override
    public OrderDetailsDto getOrderDetailsNoLogin(Long sessionId, Long orderId) {
        SessionEntity userEntity = sessionRepository.findById(sessionId)
                .orElseThrow(() -> {
                    log.error(String.format(SESSION_NOT_FOUND, sessionId));
                    return new SessionNotFoundException(String.format(USER_NOT_FOUND, sessionId));
                });

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error(String.format(ORDER_NOT_FOUND, orderId));
                    return new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
                });

        if (!Objects.equals(orderEntity.getUserEntity().getId(), userEntity.getId())) {
            log.info(String.format(INVALID_ORDER_ID_SESSION, orderId, sessionId));
            throw new OrderNotFoundException(String.format(INVALID_ORDER_ID_SESSION, orderId, sessionId));
        }

        log.info("Retrieved order from database");
        final var order = putLists(orderEntity);
        log.info("Converted order to order details {}", order.getId());
        return order;
    }

    @Override
    public OrderDetailsDto addProductToOrder(
            CreateOrUpdateOrderProductDto orderProductDto,
            Long orderId,
            Long userId
    ) {
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
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", userId);
                    throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
                });

        if (orderDto.getAddressDto() == null && userEntity.getAddressEntities().isEmpty()) {
            log.error(USER_DOESNT_HAVE_ADDRESS);
            throw new AddressNotFound(USER_DOESNT_HAVE_ADDRESS);
        }

        OrderEntity orderEntity = orderConverter.convertDtoToOrderEntity(orderDto);

        if (orderDto.getAddressId() != null) {
            final AddressEntity address = addressRepository.findById(orderDto.getAddressId())
                    .orElseThrow(() -> {
                        log.error(String.format(ADDRESS_NOT_FOUND, orderDto.getAddressId()));
                        return new AddressNotFound(String.format(ADDRESS_NOT_FOUND, orderDto.getAddressId()));
                    });
            if (!address.getUserEntity().getId().equals(userId)) {
                log.error(String.format(INVALID_ADDRESS_ID, address.getId(), userId));
                throw new AddressNotFound(String.format(INVALID_ADDRESS_ID, address.getId(), userId));
            }

            orderEntity.setSendingAddress(address);
            log.info("set sending address");
        }

        if (orderDto.getAddressId() == null && orderDto.getAddressDto() != null) {
            AddressEntity address = addressConverter.convertCreateDtoToAddressEntity(orderDto.getAddressDto());
            address.setUserEntity(userEntity);
            addressRepository.save(address);
            orderEntity.setSendingAddress(address);
            log.info("saved new address for user with id {}", userEntity.getId());
            orderEntity.setSendingAddress(address);
        }

        orderEntity.setUserEntity(userEntity);
        log.info("user set for order");

        orderEntity.setCreationDate(LocalDate.now());
        log.info("creation date set");

        orderEntity.setStatus(true);
        log.info("open status set to 'true'");

        final var entity = createOrderEntity(orderDto, orderEntity);
        final var details = putLists(entity);
        details.setUserId(entity.getUserEntity().getId());
        return details;
    }

    @Override
    public OrderDetailsDto createOrderNoLogin(CreateOrUpdateOrderDto orderDto, Long sessionId) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId)
                .orElseThrow(() -> {
                    log.warn(String.format(SESSION_NOT_FOUND, sessionId));
                    throw new SessionNotFoundException(String.format(SESSION_NOT_FOUND, sessionId));
                });

        if (orderDto.getAddressDto() == null) {
            log.error("User did not provide address");
            throw new AddressNotFound(NO_ADDRESS_GIVEN);
        }

        AddressEntity address = addressConverter.convertCreateDtoToAddressEntity(orderDto.getAddressDto());
        addressRepository.save(address);

        OrderEntity orderEntity = orderConverter.convertDtoToOrderEntity(orderDto);

        orderEntity.setSendingAddress(address);
        log.info("address set");

        orderEntity.setCreationDate(LocalDate.now());
        log.info("creation date set");

        orderEntity.setStatus(true);
        log.info("open status set to 'true'");

        orderEntity.setSession(sessionEntity);
        log.info("set session on order");

        return putLists(createOrderEntity(orderDto, orderEntity));
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

        //TODO: UPDATE THIS
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

        //TODO: UPDATE THIS
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

        //TODO: UPDATE THIS
        final var savedOrder = orderRepository.save(orderEntity);
        savedOrder.setUserEntity(userEntity);
        return orderConverter.convertEntityToOrderDetailsDto(savedOrder);
    }

    @Override
    public OrderDetailsDto finishOrder(Long orderId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", userId);
                    throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
                });
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("order with id {} does not exist", orderId);
                    throw new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
                });
        orderEntity.setStatus(false);
        final var savedOrder = orderRepository.save(orderEntity);
        log.info("Order with id {} is finished", orderEntity.getId());
        return orderConverter.convertEntityToOrderDetailsDto(savedOrder);
    }

    @Override
    public OrderDetailsDto finishOrderNoLogin(Long orderId, Long sessionId) {
        sessionRepository.findById(sessionId)
                .orElseThrow(() -> {
                    log.warn(String.format(SESSION_NOT_FOUND, sessionId));
                    throw new SessionNotFoundException(String.format(SESSION_NOT_FOUND, sessionId));
                });
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("order with id {} does not exist", orderId);
                    throw new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
                });
        orderEntity.setStatus(false);
        final var savedOrder = orderRepository.save(orderEntity);
        log.info("Order with id {} is finished", orderEntity.getId());
        return orderConverter.convertEntityToOrderDetailsDto(savedOrder);
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

        orderProduct.setActive(product.isActive());
        log.info("OrderProduct active status set to '{}'", product.isActive());

        if (orderProductDto.getColorId() != 0) {
            ColorEntity color = colorRepository.findById(orderProductDto.getColorId())
                    .orElseThrow(() -> {
                        log.error(String.format(COLOR_NOT_FOUND, orderProductDto.getColorId()));
                        return new ColorNotFoundException(String.format(COLOR_NOT_FOUND, orderProductDto.getColorId()));
                    });

            product.getColors().stream().filter(colorEntity -> colorEntity.getId().equals(orderProductDto.getColorId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.error(String.format(INVALID_COLOR_ID, orderProductDto.getColorId(), product.getId()));
                        return new ColorNotFoundException(String.format(INVALID_COLOR_ID, orderProductDto.getColorId(), product.getId()));
                    });

            orderProduct.setColor(color);
            log.info("OrderProduct color set to {}", color.getHexValue());
        }

        final var savedOrderProduct = orderProductRepository.save(orderProduct);
        log.info("OrderProduct saved to data base");

        return savedOrderProduct;
    }

    private OrderDetailsDto putLists(OrderEntity orderEntity) {
        OrderDetailsDto orderDetailsDto = orderConverter.convertEntityToOrderDetailsDto(orderEntity);
        orderDetailsDto.setAddressDetailsDto(addressConverter.convertEntityToAddressDetailsDto(orderEntity.getSendingAddress()));

        final var orderProductDetails = orderEntity.getOrderProductEntityList().stream()
                .map(orderProductEntity -> {
                    final var converted = orderProductConverter.convertEntityToOrderProductDetailsDto(orderProductEntity);
                    if (orderProductEntity.getColor() != null)
                        converted.setColorId(orderProductEntity.getColor().getId());
                    converted.setOrderId(orderEntity.getId());
                    return converted;
                })
                .collect(Collectors.toList());
        orderDetailsDto.setOrderProductDetailsDtoList(orderProductDetails);
        log.info("retrieving details of the order");
        return orderDetailsDto;
    }

    private OrderEntity createOrderEntity(CreateOrUpdateOrderDto orderDto, OrderEntity orderEntity) {
        List<OrderProductEntity> orderProductEntityList = orderDto.getProductDtos().stream()
                .map(this::createOrderProduct)
                .peek(orderProductRepository::save)
                .collect(Collectors.toList());
        log.info("Created orderProductEntity list");


        double sum = 0;
        for (OrderProductEntity entity : orderProductEntityList) {
            sum = sum + entity.getProduct().getPrice() * entity.getQuantity();
        }
        orderEntity.setTotalPrice(sum);
        log.info("set total price to {} €", orderEntity.getTotalPrice());

        final OrderEntity savedOrder = orderRepository.save(orderEntity);
        log.info("Saved order to database with id {}", savedOrder.getId());

        List<OrderProductEntity> savedOrderProducts = orderProductEntityList.stream()
                .map(orderProduct -> {
                    OrderProductEntity updated = orderProductRepository.findById(orderProduct.getId())
                            .orElseThrow(() ->
                            {
                                log.warn("order product with id {} does not exist", orderProduct.getId());
                                throw new OrderProductNotFoundException(String.format(ORDER_PRODUCT_NOT_FOUND, orderProduct.getId()));
                            });
                    updated.setOrderEntity(savedOrder);
                    log.info("set order entity on order product");
                    final var temp = orderProductRepository.save(updated);
                    log.info("saved order product with id {}", temp.getId());
                    return temp;
                })
                .collect(Collectors.toList());

        savedOrder.setOrderProductEntityList(savedOrderProducts);
        log.info("set order product entity list on order");

        final var updatedOrder = orderRepository.save(savedOrder);
        log.info("got updated order from db");
        return updatedOrder;
    }

}
