package com.example.controller;

import com.example.common.Result;
import com.example.entity.Checkin;
import com.example.entity.Order;
import com.example.service.CheckinService;
import com.example.service.OrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/checkin")
public class CheckinController {
    @Resource
    private CheckinService checkinService;
    @PostMapping("/add")
    public Result add(@RequestBody Checkin checkin){
        checkinService.add(checkin);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        checkinService.deleteById(id);
        return Result.success();
    }
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        checkinService.deleteBatch(ids);
        return Result.success();
    }
    @PutMapping("/update")
    public Result updateById(@RequestBody Checkin checkin) {
        checkinService.updateById(checkin);
        return Result.success();
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Checkin checkin = checkinService.selectById(id);
        return Result.success(checkin);
    }
    @GetMapping("/selectByUserId")
    public Result selectByUserId(@RequestParam Integer id) {
        List<Checkin> checkin = checkinService.selectByUserId(id);
        return Result.success(checkin);
    }
    @GetMapping("/selectAll")
    public Result selectAll(Checkin checkin ) {
        List<Checkin> list = checkinService.selectAll(checkin);
        return Result.success(list);
    }
    @GetMapping("/selectPage")
    public Result selectPage(Checkin checkin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Checkin> page = checkinService.selectPage(checkin, pageNum, pageSize);
        return Result.success(page);
    }
    @GetMapping("/checkout/{id}")
    public Result checkout(@PathVariable Integer id){
        checkinService.checkout(id);
        return Result.success();
    }
}
