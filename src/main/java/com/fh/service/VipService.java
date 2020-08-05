package com.fh.service;

import com.fh.entity.po.Vip;

public interface VipService {
    void addVip(Vip vip);

    Vip queryByPhone(long vipPhone);

    Vip queryByName(String vipName);
}
