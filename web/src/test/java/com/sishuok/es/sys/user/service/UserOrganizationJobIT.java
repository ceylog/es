/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sishuok.es.sys.user.service;

import com.google.common.collect.Lists;
import com.sishuok.es.sys.organization.entity.Job;
import com.sishuok.es.sys.organization.entity.Organization;
import com.sishuok.es.sys.organization.service.JobService;
import com.sishuok.es.sys.organization.service.OrganizationService;
import com.sishuok.es.sys.user.aop.UserCacheAspect;
import com.sishuok.es.sys.user.entity.User;
import com.sishuok.es.sys.user.entity.UserOrganizationJob;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-4-5 下午3:24
 * <p>Version: 1.0
 */
public class UserOrganizationJobIT extends BaseUserIT {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private JobService jobService;

    @Autowired
    private UserCacheAspect userCacheAspect;

    @Test
    public void testCascadeSaveOrgnization() {
        User user = createDefaultUser();

        Organization organization1 = new Organization();
        organization1.setName("test1");
        Organization organization2 = new Organization();
        organization2.setName("test2");
        organizationService.save(organization1);
        organizationService.save(organization2);

        Job job1 = new Job();
        job1.setName("test1");
        Job job2 = new Job();
        job2.setName("test2");
        jobService.save(job1);
        jobService.save(job2);

        user.addOrganizationJob(new UserOrganizationJob(organization1));
        user.addOrganizationJob(new UserOrganizationJob(organization2, job1));
        user.addOrganizationJob(new UserOrganizationJob(organization2, job2));

        //清除缓存
        userCacheAspect.cacheEvictAdvice(user);
        clear();

        user = userService.findOne(user.getId());

        Assert.assertEquals(3, user.getOrganizationJobs().size());
        Assert.assertEquals(organization1, user.getOrganizationJobs().get(0).getOrganization());

        Assert.assertEquals(organization2, user.getOrganizationJobs().get(1).getOrganization());
        Assert.assertEquals(organization2, user.getOrganizationJobs().get(2).getOrganization());
    }

    @Test
    public void testCascadeUpdateOrgnization() {
        User user = createDefaultUser();

        Organization organization1 = new Organization();
        organization1.setName("test1");
        Organization organization2 = new Organization();
        organization2.setName("test2");
        organizationService.save(organization1);
        organizationService.save(organization2);


        //清除缓存
        userCacheAspect.cacheEvictAdvice(user);
        clear();


        user.addOrganizationJob(new UserOrganizationJob(organization1));
        user.addOrganizationJob(new UserOrganizationJob(organization2));

        userService.update(user);

        //清除缓存
        userCacheAspect.cacheEvictAdvice(user);
        clear();

        user = userService.findOne(user.getId());
        Assert.assertEquals(2, user.getOrganizationJobs().size());
        Assert.assertEquals(organization1, user.getOrganizationJobs().get(0).getOrganization());
        Assert.assertEquals(organization2, user.getOrganizationJobs().get(1).getOrganization());
    }


    @Test
    public void testCascadeDeleteOrgnization() {
        User user = createDefaultUser();

        Organization organization1 = new Organization();
        organization1.setName("test1");
        Organization organization2 = new Organization();
        organization2.setName("test2");
        organizationService.save(organization1);
        organizationService.save(organization2);


        Job job1 = new Job();
        job1.setName("test1");
        Job job2 = new Job();
        job2.setName("test2");
        jobService.save(job1);
        jobService.save(job2);

        user.addOrganizationJob(new UserOrganizationJob(organization1));
        user.addOrganizationJob(new UserOrganizationJob(organization2, job1));
        user.addOrganizationJob(new UserOrganizationJob(organization2, job2));

        //清除缓存
        userCacheAspect.cacheEvictAdvice(user);
        clear();

        user = userService.findOne(user.getId());
        user.getOrganizationJobs().remove(0);

        //清除缓存
        userCacheAspect.cacheEvictAdvice(user);
        clear();


        user = userService.findOne(user.getId());

        Assert.assertEquals(2, user.getOrganizationJobs().size());

        Assert.assertEquals(organization2, user.getOrganizationJobs().get(0).getOrganization());
        Assert.assertEquals(organization2, user.getOrganizationJobs().get(1).getOrganization());
    }
}
