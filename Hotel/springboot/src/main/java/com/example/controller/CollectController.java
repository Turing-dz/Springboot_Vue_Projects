package com.example.controller;

import com.example.common.Result;
import com.example.entity.Collect;
import com.example.entity.Type;
import com.example.service.CollectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController {
    @Resource
    private CollectService collectService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Collect collect) {
        collectService.add(collect);
        return Result.success();
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/deleteByTypeId")
    public Result deleteByTypeId(@RequestParam Integer id) {
        collectService.deleteByTypeId(id);
        return Result.success();
    }

    @GetMapping("/selectOwn")
    public Result selectOwn(@RequestParam Integer id) {
        List<Type> list = collectService.selectOwn(id);
        return Result.success(list);
    }
}
