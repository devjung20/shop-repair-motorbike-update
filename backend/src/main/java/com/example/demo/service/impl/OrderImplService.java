package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.TypeEmployee;
import com.example.demo.entity.enums.TypeOrder;
import com.example.demo.exception.BaseException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.BaseResponse;
import com.example.demo.model.params.OrderParams;
import com.example.demo.model.request.*;
import com.example.demo.model.response.StoreResponse;
import com.example.demo.model.response.orderReponse.*;
import com.example.demo.repository.*;
import com.example.demo.security.CustomUserDetail;
import com.example.demo.service.HistoryProductService;
import com.example.demo.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderImplService implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    private final ServiceRepository serviceRepository;
    private final ProductRepository productRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final OrderServiceRepository orderServiceRepository;

    private final HistoryProductService historyProductService;
    private final StoreRepository storeRepository;

    public OrderImplService(OrderRepository orderRepository,
                            CustomerRepository customerRepository,
                            EmployeeRepository employeeRepository,
                            ServiceRepository serviceRepository,
                            ProductRepository productRepository,
                            OrderProductsRepository orderProductsRepository,
                            OrderServiceRepository orderServiceRepository,
                            HistoryProductService historyProductService, StoreRepository storeRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.serviceRepository = serviceRepository;
        this.productRepository = productRepository;
        this.orderProductsRepository = orderProductsRepository;
        this.orderServiceRepository = orderServiceRepository;
        this.historyProductService = historyProductService;
        this.storeRepository = storeRepository;
    }

    @Override
    public BaseResponse getAll(int pageNumber, int pageSize) {
        // Pageable pageable = PageRequest.of(pageNumber, pageSize);
        // Page<OrderEntity> orderEntityPage = orderRepository.findAll(pageable);
        // List<BaseOrder> baseOrders = orderEntityPage.getContent().stream().map(order
        // -> {
        // return this.entityToBase(order);
        // }).collect(Collectors.toList());

        // Page<BaseOrder> baseOrderPage = new PageImpl<>(baseOrders, pageable,
        // orderEntityPage.getTotalElements());
        // return BaseResponse.builder()
        // .data(baseOrderPage)
        // .message("Get all order successfully.")
        // .statusCode(HttpStatus.OK.value())
        // .build();
        return null;
    }

    @Transactional
    @Override
    public BaseResponse create(InsertOrder insertOrder) {
        OrderEntity orderSaved = this.createOrUpdate(insertOrder, null);
        // create list service
        List<OrderServiceEntity> orderServiceEntities = insertOrder.getServices().stream()
                .map(serviceOrders -> {
                    return serviceInOrderToEntity(orderSaved, serviceOrders);
                }).collect(Collectors.toList());

        // save list service to table
        orderServiceRepository.saveAll(orderServiceEntities);

        // create list product
        List<OrderProduct> products = insertOrder.getProducts().stream().map(productOrders -> {
            return productInOrderToEntity(orderSaved, productOrders);
        }).collect(Collectors.toList());
        // save products to table
        orderProductsRepository.saveAll(products);
        return BaseResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("tạo đơn hàng thành công.")
                .data(this.entityToResponse(orderSaved, orderServiceEntities, products))
                .build();
    }

    @Transactional
    public OrderEntity createOrUpdate(InsertOrder insertOrder, Long orderId) {
        if (orderId != null) {
            orderRepository.findByIdAndTypeIsOrder(orderId).orElseThrow(() -> {
                throw new BaseException("Đơn hàng không được tìm thấy hoặc có hóa đơn.");
            });
            EmployeeEntity employeeRepairer = orderRepository.findById(orderId).orElseThrow(() -> {
                throw new EntityNotFoundException("OrderEntity", "Id", orderId.toString());
            }).getRepairer();
            employeeRepository.save(employeeRepairer);
            orderProductsRepository.getAllByOrderId(orderId).stream().forEach(orderProduct -> {
                ProductEntity product = orderProduct.getProduct();
                product.setStorageQuantity(product.getStorageQuantity() + orderProduct.getQuantity());
                productRepository.save(product);
            });
        }
        CustomerEntity customer = this.customerRepository.findByIdAndIsActive(insertOrder.getCustomerId(), true)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Customer", "Id", insertOrder.getCustomerId().toString());
                });
        EmployeeEntity employeeDispatcher = this.employeeRepository.findByIdAndIsActive(insertOrder.getDispatcherId())
                .orElseThrow(() -> {
                    throw new BaseException("Không tìm thấy DispatcherId.");
                });
        if(!employeeDispatcher.getType().toString().equalsIgnoreCase(TypeEmployee.DISPATCHER.toString()) && !employeeDispatcher.getType().toString().equalsIgnoreCase(TypeEmployee.MANAGER.toString())) {
            throw new BaseException("Nhân viên điều phối không hợp lệ.");
        }
        EmployeeEntity employeeRepairer = this.employeeRepository.getRepairerIsActive(insertOrder.getRepairerId())
                .orElseThrow(() -> {
                    throw new BaseException(new StringBuilder("Không tìm thấy nhân viên theo id = ")
                            .append(insertOrder.getRepairerId()).append(" hoặc thợ sửa chữa đang làm việc.")
                            .toString());
                });
        if (!employeeRepairer.getType().toString().equalsIgnoreCase(TypeEmployee.REPAIRER.toString())) {
            throw new BaseException("Nhân viên sửa chữa không hợp lệ.");
        }
        Set<ProductOrders> orderProductSet = new HashSet<>(insertOrder.getProducts());
        List<ProductOrders> productOrdersUpdate = new ArrayList<>();
        for (ProductOrders productOrders : orderProductSet) {
            ProductOrders productOrdersFinal = new ProductOrders();
            productOrdersFinal.setId(productOrders.getId());
            productOrdersFinal.setQuantity(
                    insertOrder.getProducts().stream()
                            .filter(productOrders1 -> {
                                return productOrders1.getId() == productOrders.getId();
                            }).mapToInt(productOrders1 -> (Integer) productOrders1.getQuantity()).sum());
            productOrdersUpdate.add(productOrdersFinal);
        }
        insertOrder.setProducts(productOrdersUpdate);
        insertOrder.getProducts().stream().forEach(productOrders -> {
            if (productRepository.checkExistByIdAndQuantity(productOrders.getId(), productOrders.getQuantity()) == 0) {
                throw new BaseException(
                        new StringBuilder("Không tìm thấy sản phẩm theo id = ").append(productOrders.getId())
                                .append(" hoặc hết sản phẩm trong kho.").toString());
            }
        });
        Set<ServiceOrders> serviceOrdersSet = new HashSet<>(insertOrder.getServices());
        List<ServiceOrders> serviceOrdersFinal = new ArrayList<>();
        for (ServiceOrders serviceOrders : serviceOrdersSet) {
            ServiceOrders serviceOrders1 = new ServiceOrders();
            serviceOrders1.setId(serviceOrders.getId());
            serviceOrders1.setQuantity(
                    insertOrder.getServices().stream()
                            .filter(serviceOrders2 -> {
                                return serviceOrders2.getId() == serviceOrders.getId();
                            })
                            .mapToInt(serviceOrders2 -> (Integer) serviceOrders2.getQuantity())
                            .sum());
            serviceOrdersFinal.add(serviceOrders1);
        }
        insertOrder.setServices(serviceOrdersFinal);
        insertOrder.getServices().stream().forEach(serviceOrders -> {
            if (!serviceRepository.existsByIdAndIsActive(serviceOrders.getId(), true)) {
                throw new BaseException(
                        new StringBuilder("Dịch vụ không được tìm thấy bởi id = ").append(serviceOrders.getId())
                                .toString());
            }
        });
        OrderEntity order;
        if (orderId != null) {
            order = orderRepository.findById(orderId).orElseThrow(() -> {
                throw new EntityNotFoundException("Order", "Id", orderId.toString());
            });
            order.setType(TypeOrder.valueOf(insertOrder.getType()));
            order.setNote(insertOrder.getNote());
            order.setMotorbikeName(insertOrder.getMotorbikeName());
            order.setMotorbikeCode(insertOrder.getMotorbikeCode());
            order.setDispatcher(employeeDispatcher);
            order.setRepairer(employeeRepairer);
            order.setCustomerEntity(customer);
            order.setModifyBy(((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getAccount());
        } else {
            order = OrderEntity.builder()
                    .type(TypeOrder.ORDER)
                    .note(insertOrder.getNote())
                    .motorbikeCode(insertOrder.getMotorbikeCode())
                    .motorbikeName(insertOrder.getMotorbikeName())
                    .dispatcher(employeeDispatcher)
                    .repairer(employeeRepairer)
                    .customerEntity(customer)
                    .build();
            order.setIsActive(true);
            order.setCreateBy(((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getAccount());
            // save order to table
        }
        // employeeRepository.save(employeeRepairer);
        order.setIsActive(true);
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public BaseResponse update(InsertOrder insertOrder, Long aLong) {
        OrderEntity orderUpdate = this.createOrUpdate(insertOrder, aLong);
        // get all services and products in old order
        List<OrderServiceEntity> orderServiceEntities = orderServiceRepository.getAllByOrderId(aLong);
        List<OrderProduct> orderProducts = orderProductsRepository.getAllByOrderId(aLong);

        // handle service
        List<OrderServiceEntity> orderServicesDeleteEntity = orderServiceEntities.stream()
                .filter(orderServiceEntity -> {
                    return !insertOrder.getServices().stream()
                            .anyMatch(serviceOrders -> serviceOrders.getId() == orderServiceEntity.getService().getId()
                                    && serviceOrders.getQuantity() == orderServiceEntity.getQuantity());
                }).collect(Collectors.toList());
        List<OrderServiceEntity> orderServicesSaveEntity = insertOrder.getServices()
                .stream()
                .filter(serviceOrders -> {
                    return !orderServiceEntities.stream()
                            .anyMatch(orderServiceEntity -> orderServiceEntity.getService().getId() == serviceOrders
                                    .getId()
                                    && orderServiceEntity.getQuantity() == serviceOrders.getQuantity());
                })
                .map(serviceOrders -> {
                    return serviceInOrderToEntity(orderUpdate, serviceOrders);
                }).collect(Collectors.toList());
        orderServiceRepository.deleteAll(orderServicesDeleteEntity);
        orderServiceRepository.saveAll(orderServicesSaveEntity);

        // handle product
        orderProductsRepository.deleteAll(orderProducts);
        List<OrderProduct> orderProductList = insertOrder.getProducts().stream().map(productOrders -> {
            return this.productInOrderToEntity(orderUpdate, productOrders);
        }).collect(Collectors.toList());
        orderProductsRepository.saveAll(orderProductList);
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật đơn hàng thành công.")
                .data(this.entityToResponse(orderUpdate, orderServiceRepository.getAllByOrderId(orderUpdate.getId()),
                        orderProductsRepository.getAllByOrderId(orderUpdate.getId())))
                .build();
    }

    public OrderProduct productInOrderToEntity(OrderEntity orderUpdate, ProductOrders productOrders) {
        ProductEntity product = this.productRepository.findById(productOrders.getId()).get();
        product.setStorageQuantity(product.getStorageQuantity() - productOrders.getQuantity());
        productRepository.save(product);
        return OrderProduct.builder()
                .order(orderUpdate)
                .product(product)
                .quantity(productOrders.getQuantity())
                .name(product.getName())
                .price(product.getPriceOut())
                .unit(product.getUnit())
                .build();
    }

    private OrderServiceEntity serviceInOrderToEntity(OrderEntity orderUpdate,
            ServiceOrders serviceOrders) {
        ServiceEntity service = this.serviceRepository.findById(serviceOrders.getId()).get();
        return OrderServiceEntity.builder()
                .order(orderUpdate)
                .service(service)
                .quantity(serviceOrders.getQuantity())
                .name(service.getName())
                .price(service.getPrice())
                // .salaryDispatcher(service.getSalaryDispatcher())
                // .salaryRepairer(service.getSalaryRepairer())
                .build();
    }

    @Override
    public BaseResponse deleteById(Long aLong) {
        OrderEntity order = orderRepository.findById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Order", "Id", aLong.toString());
        });
        order.setIsActive(false);
        orderRepository.save(order);
        return BaseResponse.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message("Xóa đơn hàng thành công.")
                .build();
    }

    @Override
    public BaseResponse getById(Long aLong) {
        OrderEntity order = orderRepository.findById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Order", "Id", aLong.toString());
        });
        return BaseResponse.builder()
                .message("Nhận đơn hàng thành công.")
                .statusCode(HttpStatus.OK.value())
                .data(this.entityToResponse(order, orderServiceRepository.getAllByOrderId(order.getId()),
                        orderProductsRepository.getAllByOrderId(order.getId())))
                .build();
    }

    @Override
    public BaseResponse toInvoice(long orderId) {
        OrderEntity order = orderRepository.getByIdAndType(orderId, "ORDER").orElseThrow(() -> {
            throw new BaseException("Đơn hàng không được tìm thấy theo id: " + orderId);
        });
        EmployeeEntity employee = order.getRepairer();
        List<OrderProduct> orderProducts = orderProductsRepository.getAllByOrderId(orderId);
        HistoryProductRequest historyProductRequest = new HistoryProductRequest();
        historyProductRequest.setNote("Xuất bởi " + employee.getCode());
        List<HistoryDetail> historyDetails = new ArrayList<>();
        orderProducts.stream().forEach(orderProduct -> {
            historyDetails.add(
                    HistoryDetail.builder()
                            .productId(orderProduct.getProduct().getId())
                            .difference(orderProduct.getQuantity() * -1)
                            .build());
        });
        historyProductRequest.setHistoryDetails(historyDetails);
        historyProductService.createHistory(historyProductRequest, false, false);
        order.setType(TypeOrder.INVOICE);
        orderRepository.save(order);
        return BaseResponse.builder()
                .data(null)
                .statusCode(HttpStatus.OK.value())
                .message("Để hóa đơn thành công.")
                .build();
    }

    @Override
    public BaseResponse getByParams(Map<String, String> params) {
        OrderParams orderParams = new OrderParams(params);
        Sort sort = Sort.by(orderParams.getSortOrder(), orderParams.getSortBy());
        Pageable pageable = PageRequest.of(orderParams.getPageNumber() - 1, orderParams.getPageSize(), sort);
        Page<Object[]> objects = orderRepository.getOrderByConditions(orderParams, pageable);
        List<BaseOrder> baseOrders = objects.getContent().stream().map(objects1 -> {
            return this.entityToBase(objects1);
        }).collect(Collectors.toList());
        return BaseResponse.builder()
                .message("Nhận đơn hàng thành công.")
                .statusCode(HttpStatus.OK.value())
                .data(new PageImpl<>(baseOrders, pageable, objects.getTotalElements()))
                .build();
    }

    private OrderResponse entityToResponse(OrderEntity order, List<OrderServiceEntity> orderServiceEntities,
            List<OrderProduct> orderProducts) {
        CustomerEntity customerEntity = order.getCustomerEntity();
        EmployeeEntity employeeDispatcher = order.getDispatcher();
        EmployeeEntity employeeRepairer = order.getRepairer();
        StoreEntity storeEntity = storeRepository.findById(1L).get();
        return OrderResponse.builder()
                .id(order.getId())
                .code(order.getCode())
                .modifiedDate(order.getModifyDate())
                .storeResponse(
                        StoreResponse.builder()
                                .phone(storeEntity.getPhone())
                                .name(storeEntity.getName())
                                .address(storeEntity.getAddress())
                                .email(storeEntity.getEmail())
                                .vat(storeEntity.getVat())
                                .build()
                )
                .infoCustomer(
                        InfoCustomer.builder()
                                .id(customerEntity.getId())
                                .code(customerEntity.getCode())
                                .name(customerEntity.getName())
                                .phone(customerEntity.getPhone())
                                .email(customerEntity.getEmail())
                                .address(customerEntity.getAddress())
                                .motorbikeCode(order.getMotorbikeCode())
                                .motorbikeName(order.getMotorbikeName())
                                .build())
                .infoDispatcher(
                        InfoEmployee.builder()
                                .id(employeeDispatcher.getId())
                                .code(employeeDispatcher.getCode())
                                .name(employeeDispatcher.getName())
                                .phone(employeeDispatcher.getPhone())
                                .email(employeeDispatcher.getEmail())
                                .status(employeeDispatcher.getIsActive())
                                .build())
                .infoRepairer(
                        InfoEmployee.builder()
                                .id(employeeRepairer.getId())
                                .code(employeeRepairer.getCode())
                                .name(employeeRepairer.getName())
                                .phone(employeeRepairer.getPhone())
                                .email(employeeRepairer.getEmail())
                                .status(employeeRepairer.getIsActive())
                                .build())
                .note(order.getNote())
                .infoServices(
                        orderServiceEntities.stream().map(orderServiceEntity -> {
                            Long serviceId = 0L;
                            Optional<ServiceEntity> serviceEntity = serviceRepository
                                    .findById(orderServiceEntity.getService().getId());
                            if (serviceEntity.isPresent()) {
                                serviceId = serviceEntity.get().getId();
                            }
                            return InfoService.builder()
                                    .id(serviceId)
                                    .name(orderServiceEntity.getName())
                                    .price(orderServiceEntity.getPrice())
                                    .quantity(orderServiceEntity.getQuantity())
                                    .build();
                        }).collect(Collectors.toList()))
                .infoProducts(
                        orderProducts.stream().map(orderProduct -> {
                            Integer storageQuantity = 0;
                            Long productId = 0L;
                            Optional<ProductEntity> productEntity = productRepository
                                    .findById(orderProduct.getProduct().getId());
                            if (productEntity.isPresent()) {
                                storageQuantity = productEntity.get().getStorageQuantity();
                                productId = productEntity.get().getId();
                            }
                            return InfoProduct.builder()
                                    .id(productId)
                                    .name(orderProduct.getName())
                                    .quantity(orderProduct.getQuantity())
                                    .price(orderProduct.getPrice())
                                    .unit(orderProduct.getUnit())
                                    .storageQuantity(storageQuantity)
                                    .build();
                        }).collect(Collectors.toList()))
                .build();
    }

    private BaseOrder entityToBase(Object[] objects) {
        return BaseOrder.builder()
                .id((Long) objects[0])
                .code((String) objects[1])
                .customerCode((String) objects[2])
                .customerName((String) objects[3])
                .dispatcherName((String) objects[4])
                .repairerName((String) objects[5])
                .motorbikeCode((String) objects[6])
                .createDate(((Timestamp) objects[7]).toLocalDateTime())
                .type((String) objects[8])
                .build();
    }

}
