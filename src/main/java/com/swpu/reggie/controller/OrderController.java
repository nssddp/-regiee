package com.swpu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swpu.reggie.common.R;
import com.swpu.reggie.domain.OrderDetail;
import com.swpu.reggie.domain.Orders;
import com.swpu.reggie.dto.OrderDto;
import com.swpu.reggie.service.OrderDetailService;
import com.swpu.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 历史订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize) {

        log.info("page = {},pageSize = {}", page, pageSize);
        //分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrderDto> orderDtoPage = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, orderDtoPage, "records");

        List<Orders> records = pageInfo.getRecords();
        List<OrderDto> list = records.stream().map((item) -> {
            OrderDto orderDto = new OrderDto();

            BeanUtils.copyProperties(item, orderDto);

            Long orderId = item.getId();

            orderDto = orderService.getByIdWithDetail(orderId);

            return orderDto;
        }).collect(Collectors.toList());

        orderDtoPage.setRecords(list);

        return R.success(orderDtoPage);
    }

    /**
     * 后台订单页面
     * @param page
     * @param pageSize
     * @param number
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, String startTime, String endTime) {
        log.info("page = {},pageSize = {}, num = {}, startTime = {}, endTime = {}", page, pageSize, number,startTime,endTime);

        Page<Orders> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(number != null,Orders::getId, number);
        queryWrapper.gt(startTime != null,Orders::getOrderTime,startTime)
                .lt(endTime != null,Orders::getOrderTime,endTime);

        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo, queryWrapper);


        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> statusChange(@RequestBody Orders orders){
        log.info(String.valueOf(orders));
        Long ordersId = orders.getId();
        Orders order = orderService.getById(ordersId);
        order.setStatus(4);
        orderService.updateById(order);

        return R.success("状态已更新");
    }

    @PostMapping("again")
    public R<String> OrderAgain(@RequestBody Orders orders){

        Long ordersId = orders.getId();
        OrderDto orderDto = orderService.getByIdWithDetail(ordersId);

        Orders order = orderService.getById(ordersId);
        long id = IdWorker.getId();
        order.setId(id);
        order.setCheckoutTime(LocalDateTime.now());
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(2);

        List<OrderDetail> orderDetails = orderDto.getOrderDetails();
        orderDetails.stream().map((item) -> {
            item.setOrderId(id);
            item.setId(null);

            return orderDetails;
        }).collect(Collectors.toList());

        orderService.save(order);
        orderDetailService.saveBatch(orderDetails);


        return null;
    }

}