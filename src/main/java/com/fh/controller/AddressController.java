package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.entity.po.Address;
import com.fh.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping("queryAddress")
    public JsonData queryAddress(){
        List<Address> list =  addressService.queryAddress();
        return  JsonData.getJsonSuccess(list);
    }
    @RequestMapping("addAddress")
    public JsonData addAddress(Address address){
        addressService.addAddress(address);
        return JsonData.getJsonSuccess("添加地址成功");
    }

}
