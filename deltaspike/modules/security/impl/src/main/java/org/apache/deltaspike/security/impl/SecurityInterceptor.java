/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package org.apache.deltaspike.security.impl;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.deltaspike.security.impl.SecurityExtension.Authorizer;

/**
 * Provides authorization services for component invocations.
 */
@SecurityInterceptorBinding
@Interceptor
public class SecurityInterceptor implements Serializable 
{
    private static final long serialVersionUID = -6567750187000766925L;

    @Inject private SecurityExtension extension;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext invocation) throws Exception 
    {
        Method method = invocation.getMethod();

        for (Authorizer authorizer : extension.lookupAuthorizerStack(method, invocation.getTarget().getClass())) 
        {
            authorizer.authorize(invocation);
        }

        return invocation.proceed();
    }
}
