/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sishuok.es.sys.user.service;

import com.sishuok.es.common.inject.annotation.BaseComponent;
import com.sishuok.es.common.service.BaseService;
import com.sishuok.es.sys.user.entity.UserLastOnline;
import com.sishuok.es.sys.user.entity.UserOnline;
import com.sishuok.es.sys.user.repository.UserOnlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class UserOnlineService extends BaseService<UserOnline, String> {

    @Autowired
    @BaseComponent
    private UserOnlineRepository userOnlineRepository;

    /**
     * 上线
     * @param userOnline
     */
    public void online(UserOnline userOnline) {
        save(userOnline);
    }

    /**
     * 下线
     * @param sid
     */
    public void offline(String sid) {
        UserOnline userOnline = findOne(sid);
        if (userOnline != null) {
            delete(userOnline);
        }
        //游客 无需记录上次访问记录
        //此处使用数据库的触发器完成同步
//        if(userOnline.getUserId() == null) {
//            userLastOnlineService.lastOnline(UserLastOnline.fromUserOnline(userOnline));
//        }
    }

    /**
     * 批量下线
     * @param needOfflineIdList
     */
    public void batchOffline(List<String> needOfflineIdList) {
        userOnlineRepository.batchDelete(needOfflineIdList);
    }

    /**
     * 无效的UserOnline
     * @return
     */
    public Page<UserOnline> findExpiredUserOnlineList(Date expiredDate, Pageable pageable) {
        return userOnlineRepository.findExpiredUserOnlineList(expiredDate, pageable);
    }


}
