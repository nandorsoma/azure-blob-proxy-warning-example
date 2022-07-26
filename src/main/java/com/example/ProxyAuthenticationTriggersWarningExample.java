package com.example;

import com.azure.core.http.ProxyOptions;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;

import java.net.InetSocketAddress;
import java.util.UUID;

public class ProxyAuthenticationTriggersWarningExample {

    private static final String ACCOUNT_NAME = "TODO";
    private static final String ACCOUNT_KEY = "TODO";
    private static final String ENDPOINT_SUFFIX = "blob.core.windows.net";

    private static final String PROXY_HOST = "127.0.0.1";
    private static final int PROXY_PORT = 9080;
    private static final String PROXY_USERNAME = "TODO";
    private static final String PROXY_PASSWORD = "TODO";

    private static final String CONTAINER_NAME = "test-container";
    private static final String TEST_CONTENT = "test";


    public static void main(String[] args) {
        BlobServiceClientBuilder clientBuilder = new BlobServiceClientBuilder();
        clientBuilder.endpoint(String.format("https://%s.%s", ACCOUNT_NAME, ENDPOINT_SUFFIX));

        clientBuilder.credential(new StorageSharedKeyCredential(ACCOUNT_NAME, ACCOUNT_KEY));

        NettyAsyncHttpClientBuilder nettyClientBuilder = new NettyAsyncHttpClientBuilder();

        ProxyOptions proxyOptions = new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
        proxyOptions.setCredentials(PROXY_USERNAME, PROXY_PASSWORD);
        nettyClientBuilder.proxy(proxyOptions);

        clientBuilder.httpClient(nettyClientBuilder.build());

        BlobServiceClient client = clientBuilder.buildClient();
        BlobContainerClient containerClient = client.getBlobContainerClient(CONTAINER_NAME);
        containerClient.create();

        BlobClient blobClient = containerClient.getBlobClient(UUID.randomUUID().toString());
        blobClient.upload(BinaryData.fromString(TEST_CONTENT));
    }
}
