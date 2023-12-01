package com.example.controller;

import com.example.common.Result;
import com.example.entity.Hotel;
import com.example.service.HotelService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Resource
    private HotelService hotelService;
    @PostMapping("/add")
    public Result add(@RequestBody Hotel hotel) {
        hotelService.add(hotel);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Hotel hotel) {
        hotelService.update(hotel);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        hotelService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        hotelService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Hotel hotel,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Hotel> page = hotelService.selectPage(hotel, pageNum, pageSize);
        return Result.success(page);
    }
    /**
     * 前台查询所有酒店展示
     */
    @GetMapping("selectAll")
    public Result selectAll(){
        List<Hotel> list=hotelService.selectAll();
        return Result.success(list);
    }
    @GetMapping("/selectById")
    public Result selectById(@RequestParam Integer id) {
        Hotel hotel = hotelService.selectById(id);
        return Result.success(hotel);
    }
    @GetMapping("/selectByName")
    public Result selectByName(@RequestParam String name) {
        List<Hotel> list = hotelService.selectByName(name);
        return Result.success(list);
    }
}
