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

package foundation.icon.gradle.plugins.javaee.util;

import foundation.icon.icx.IconService;
import foundation.icon.icx.SignedTransaction;
import foundation.icon.icx.Transaction;
import foundation.icon.icx.data.Bytes;
import foundation.icon.icx.data.TransactionResult;
import foundation.icon.icx.transport.http.HttpProvider;
import foundation.icon.icx.transport.jsonrpc.RpcError;

import java.io.IOException;
import java.math.BigInteger;

public class TransactionHandler {
    public static final long DEFAULT_WAITING_TIME = 7000;
    private final IconService iconService;

    public TransactionHandler(String uri) {
        this.iconService = new IconService(new HttpProvider(uri));
    }

    public BigInteger estimateStep(Transaction transaction) throws IOException {
        return iconService.estimateStep(transaction).execute();
    }

    public Bytes sendTransaction(SignedTransaction signedTransaction) throws IOException {
        return iconService.sendTransaction(signedTransaction).execute();
    }

    public TransactionResult getResult(Bytes txHash) throws IOException, ResultTimeoutException {
        return getResult(txHash, DEFAULT_WAITING_TIME);
    }

    private TransactionResult getResult(Bytes txHash, long waiting) throws IOException, ResultTimeoutException {
        long limitTime = System.currentTimeMillis() + waiting;
        while (true) {
            try {
                return iconService.getTransactionResult(txHash).execute();
            } catch (RpcError e) {
                if (e.getCode() == -31002 || e.getCode() == -31003) { // pending or executing
                    if (limitTime < System.currentTimeMillis()) {
                        throw new ResultTimeoutException(txHash);
                    }
                    try {
                        // wait until block confirmation
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    continue;
                }
                throw e;
            }
        }
    }
}
