package org.tkit.app;

import io.quarkus.test.common.QuarkusTestResource;
import org.tkit.quarkus.test.docker.DockerComposeTestResource;
import org.tkit.quarkus.test.docker.QuarkusTestcontainers;

@QuarkusTestcontainers
@QuarkusTestResource(DockerComposeTestResource.class)
public abstract class AbstractTest {

}
