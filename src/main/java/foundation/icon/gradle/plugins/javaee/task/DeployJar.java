/*
 * Copyright 2020 ICON Foundation
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
 */

package foundation.icon.gradle.plugins.javaee.task;

import foundation.icon.gradle.plugins.javaee.ext.DeploymentExtension;
import foundation.icon.gradle.plugins.javaee.util.TransactionHandler;
import foundation.icon.icx.KeyWallet;
import foundation.icon.icx.SignedTransaction;
import foundation.icon.icx.Transaction;
import foundation.icon.icx.TransactionBuilder;
import foundation.icon.icx.data.Address;
import foundation.icon.icx.data.Bytes;
import foundation.icon.icx.data.TransactionResult;
import foundation.icon.icx.transport.jsonrpc.RpcObject;
import foundation.icon.icx.transport.jsonrpc.RpcValue;
import org.gradle.api.DefaultTask;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeployJar extends DefaultTask {
    public static final String SYSTEM_ADDRESS = "cx0000000000000000000000000000000000000000";
    public static final String CONTENT_TYPE_JAVA = "application/java";

    private final Property<String> uri;
    private final Property<Integer> nid;
    private final Property<String> to;

    public DeployJar() {
        super();
        ObjectFactory objectFactory = getProject().getObjects();
        this.uri = objectFactory.property(String.class);
        this.nid = objectFactory.property(Integer.class);
        this.to = objectFactory.property(String.class);
    }

    @Input
    public Property<String> getUri() {
        return uri;
    }

    @Input
    public Property<Integer> getNid() {
        return nid;
    }

    @Input
    public Property<String> getTo() {
        return to;
    }

    @TaskAction
    public void deploy() throws Exception {
        var optJar = getProject().getTasks().getByName(OptimizedJar.getTaskName());
        var extension = (DeploymentExtension) getProject().getExtensions().getByName(DeploymentExtension.getExtName());
        var txHandler = new TransactionHandler(getUri().get());
        var toAddress = new Address(getTo().get());

        String jarPath = "" + optJar.property("outputJarName");
        if (!jarPath.endsWith(".jar")) {
            throw new IllegalArgumentException("Invalid jar file: " + jarPath);
        }
        byte[] content = Files.readAllBytes(Path.of(jarPath));

        System.out.println(">>> deploy to " + getUri().get());
        if (!SYSTEM_ADDRESS.equals(toAddress.toString())) {
            System.out.println(">>> target address = " + getTo().get());
        }
        System.out.println(">>> optimizedJar = " + jarPath);
        System.out.println(">>> keystore = " + extension.getKeystore().get());

        File keystore = new File(extension.getKeystore().get());
        var owner = KeyWallet.load(extension.getPassword().get(), keystore);

        var builder = new RpcObject.Builder();
        extension.getArguments().forEach(arg -> {
            builder.put(arg.getName(), new RpcValue(arg.getValue()));
        });
        Transaction transaction = TransactionBuilder.newBuilder()
                .nid(BigInteger.valueOf(getNid().get()))
                .from(owner.getAddress())
                .to(toAddress)
                .deploy(CONTENT_TYPE_JAVA, content)
                .params(builder.build())
                .build();
        BigInteger estimatedStep = txHandler.estimateStep(transaction);
        SignedTransaction signedTransaction = new SignedTransaction(transaction, owner, estimatedStep);
        Bytes txHash = txHandler.sendTransaction(signedTransaction);
        TransactionResult result = txHandler.getResult(txHash);
        if (BigInteger.ONE.equals(result.getStatus())) {
            System.out.println("Succeeded to deploy: " + txHash);
            System.out.println("SCORE address: " + result.getScoreAddress());
        } else {
            throw new RuntimeException("Failed to deploy: " + txHash);
        }
    }
}
