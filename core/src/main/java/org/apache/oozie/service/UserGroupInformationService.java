/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.oozie.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.hadoop.security.UserGroupInformation;

public class UserGroupInformationService implements Service {

    private ConcurrentMap<String, UserGroupInformation> cache;

    @Override
    public void init(Services services) throws ServiceException {
       cache = new ConcurrentHashMap<String, UserGroupInformation>();
    }

    @Override
    public void destroy() {
        cache.clear();
    }

    @Override
    public Class<? extends Service> getInterface() {
        return UserGroupInformationService.class;
    }

    public UserGroupInformation getProxyUser(String user) throws IOException {
        if (!cache.containsKey(user)) {
            cache.putIfAbsent(user, UserGroupInformation.createProxyUser(user, UserGroupInformation.getLoginUser()));
        }
        return cache.get(user);
    }

}
