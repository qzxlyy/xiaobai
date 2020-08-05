package com.fh.service;

import com.fh.entity.po.Address;

import java.util.List;

public interface AddressService {
    List<Address> queryAddress();

    void addAddress(Address address);
}
