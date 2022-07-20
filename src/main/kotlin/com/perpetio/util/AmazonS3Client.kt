package com.perpetio.util

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import io.ktor.server.config.*


class AmazonS3Client(config: HoconApplicationConfig) {

    private val accessKey = config.property("aws.accessKey").getString()
    private val secretKey = config.property("aws.secretKey").getString()

    var credentials = BasicAWSCredentials(accessKey, secretKey)

    val s3Client: AmazonS3 = AmazonS3ClientBuilder.standard()
        .withRegion("eu-central-1")
        .withCredentials(AWSStaticCredentialsProvider(credentials))
        .build()
}