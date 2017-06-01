/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.brooklyn.container.location.openshift;

import java.util.Map;

import org.apache.brooklyn.container.location.kubernetes.KubernetesLocationLiveTest;
import org.apache.brooklyn.util.collections.MutableMap;
import org.apache.brooklyn.util.os.Os;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests deploying containers via the {@code openshift} location, to an OpenShift endpoint.
 * By extending {@link KubernetesLocationLiveTest}, we get all the k8s tests.
 * <p>
 * It needs configured with something like:
 * <p>
 * <pre>{@code
 * -Dtest.brooklyn-container-service.openshift.endpoint=https://192.168.99.100:8443/
 * -Dtest.brooklyn-container-service.openshift.certsBaseDir=~/repos/grkvlt/40bdf09b09d5896e19a9d287f41d39bb
 * }</pre>
 */
public class OpenShiftLocationLiveTest extends KubernetesLocationLiveTest {

    public static final String OPENSHIFT_ENDPOINT = System.getProperty("test.brooklyn-container-service.openshift.endpoint", "");
    public static final String CERTS_BASE_DIR = System.getProperty("test.brooklyn-container-service.openshift.certsBaseDir", Os.mergePaths(System.getProperty("user.home"), "openshift-certs"));
    public static final String CA_CERT_FILE = System.getProperty("test.brooklyn-container-service.openshift.caCert", Os.mergePaths(CERTS_BASE_DIR, "ca.crt"));
    public static final String CLIENT_CERT_FILE = System.getProperty("test.brooklyn-container-service.openshift.clientCert", Os.mergePaths(CERTS_BASE_DIR, "admin.crt"));
    public static final String CLIENT_KEY_FILE = System.getProperty("test.brooklyn-container-service.openshift.clientKey", Os.mergePaths(CERTS_BASE_DIR, "admin.key"));
    public static final String NAMESPACE = System.getProperty("test.brooklyn-container-service.openshift.namespace", "");

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftLocationLiveTest.class);

    @Override
    protected OpenShiftLocation newKubernetesLocation(Map<String, ?> flags) throws Exception {
        Map<String, ?> allFlags = MutableMap.<String, Object>builder()
                .put("endpoint", OPENSHIFT_ENDPOINT)
                .put("caCert", CA_CERT_FILE)
                .put("clientCert", CLIENT_CERT_FILE)
                .put("clientKey", CLIENT_KEY_FILE)
                .put("namespace", NAMESPACE)
                .put("privileged", true)
                .putAll(flags)
                .build();
        return (OpenShiftLocation) mgmt.getLocationRegistry().getLocationManaged("openshift", allFlags);
    }
}
