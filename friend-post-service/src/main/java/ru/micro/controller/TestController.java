package ru.micro.controller;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import ru.micro.model.Dto;

import java.util.List;

@RestController
@RequestMapping("/main")
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "Test";
    }

    @PostMapping()
    public void add(@RequestBody Dto dto){

    }

    @PutMapping("/{id}")
    public void update(
            @PathVariable int id,
            @RequestParam Dto dto
    ) {

    }

    @DeleteMapping("/{id}")
    public void deleteDyId(@PathVariable int id) {

    }
}
