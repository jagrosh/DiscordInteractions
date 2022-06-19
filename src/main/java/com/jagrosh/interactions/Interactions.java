/*
 * Copyright 2022 John Grosh (john.a.grosh@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.interactions;

import com.jagrosh.interactions.receive.Interaction;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class Interactions
{
    public static void start(InteractionsClient client, InteractionsConfig config) throws InvalidKeyException, NoSuchAlgorithmException
    {
        final Logger log = LoggerFactory.getLogger("Interactions");
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(Utils.hexToBytes(config.publicKey), spec);
        EdDSAEngine sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
        sgr.initVerify(new EdDSAPublicKey(pubKey));
        
        Spark.threadPool(config.maxThreads);
        Spark.port(config.port);
        if(config.keystore != null && config.keystorePass != null)
            Spark.secure(config.keystore, config.keystorePass, null, null);
        Spark.before(config.path, (req,res) -> 
        {
            // verify that discord is sending the request
            sgr.update((req.headers("x-signature-timestamp") + req.body()).getBytes(Charset.forName("UTF-8")));
            boolean verified = false;
            try
            {
                verified = sgr.verify(Utils.hexToBytes(req.headers("x-signature-ed25519")));
            }
            catch (SignatureException | NullPointerException ex) {}
            if(!verified)
            {
                log.warn(String.format("Unverified request from %s (%s)", req.host(), req.headers("x-signature-ed25519")));
                Spark.halt(401);
            }
        });
        Spark.post(config.path, (req, res) -> 
        {
            log.debug(String.format("Received interaction: %s", req.body()));
            try
            {
                // construct an interaction object
                Interaction interaction = new Interaction(new JSONObject(req.body()));
                String response = client.handle(interaction).toJson().toString();
                log.debug(String.format("Replying with: %s", response));
                res.header("Content-Type", "Application/Json");
                return response;
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                Spark.halt(500);
                return null;
            }
        });
    }
    
    public static class InteractionsConfig
    {
        public String publicKey, path = "/", keystore = null, keystorePass = null;
        public int port = 8443, maxThreads = 1;
    }
}
