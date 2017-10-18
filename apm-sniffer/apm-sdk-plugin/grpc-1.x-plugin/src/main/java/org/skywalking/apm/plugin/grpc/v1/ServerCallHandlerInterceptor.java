/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.plugin.grpc.v1;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import java.lang.reflect.Method;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.skywalking.apm.plugin.grpc.v1.vo.GRPCDynamicFields;

/**
 * {@link ServerCallHandlerInterceptor} record the {@link Metadata} argument into {@link GRPCDynamicFields} for
 * propagate {@link org.skywalking.apm.agent.core.context.ContextCarrier} and also record the {@link
 * io.grpc.MethodDescriptor} into {@link GRPCDynamicFields} for building span.
 *
 * @author zhangxin
 */
public class ServerCallHandlerInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        MethodInterceptResult result) throws Throwable {

    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        Object ret) throws Throwable {
        GRPCDynamicFields cachedObjects = new GRPCDynamicFields();
        cachedObjects.setMetadata((Metadata)allArguments[1]);
        cachedObjects.setDescriptor(((ServerCall)allArguments[0]).getMethodDescriptor());
        ((EnhancedInstance)ret).setSkyWalkingDynamicField(cachedObjects);
        return ret;
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Throwable t) {

    }
}
