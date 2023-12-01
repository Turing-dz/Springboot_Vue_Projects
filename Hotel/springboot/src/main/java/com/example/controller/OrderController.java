package com.example.controller;

import com.example.common.Result;
import com.example.entity.Order;
import com.example.service.OrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Resource
    private OrderService orderService;
    @PostMapping("/add")
    public Result add(@RequestBody Order order){
        orderService.add(order);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        orderService.deleteById(id);
        return Result.success();
    }
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        orderService.deleteBatch(ids);
        return Result.success();
    }
    @PutMapping("/update")
    public Result updateById(@RequestBody Order order){
        orderService.updateById(order);
        return Result.success();
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Order order = orderService.selectById(id);
        return Result.success(order);
    }
    @GetMapping("/selectByUserId")
    public Result selectByUserId(@RequestParam Integer id){
        List<Order> orders=orderService.selectByUserId(id);
        return Result.success(orders);
    }
    @GetMapping("/selectAll")
    public Result selectAll(Order order){
        List<Order> list=orderService.selectAll(order);
        return Result.success(list);
    }
    @GetMapping("/selectPage")
    public Result selectPage(Order order,@RequestParam(defaultValue ="1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Order> page=orderService.selectPage(order,pageNum,pageSize);
        return Result.success(page);
    }
    @GetMapping("/selectChecking")
    public Result selectChecking(@RequestParam Integer id) {
        List<Order> orders = orderService.selectByCheckingAndHotelId(id);
        return Result.success(orders);
    }
}
