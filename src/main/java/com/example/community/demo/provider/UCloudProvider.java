package com.example.community.demo.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.ObjectRemoteAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.auth.UfileObjectRemoteAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileFileException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import com.example.community.demo.exception.CustomizeErrorCode;
import com.example.community.demo.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

@Service
public class UCloudProvider {
    //用于梳理上传

    @Value("${ucloud.public-key}")
    private String publicKey;

    @Value("${ucloud.private-key}")
    private String privateKey;

    @Value("${ucloud.bucketName}")
    private String bucketName;

    @Value("${ucloud.expiresDuration}")
    private Integer expiresDuration;

    @Value("${ucloud.region}")
    private String region;


    @Value("${ucloud.proxySuffix}")
    private String proxySuffix;


    public String upload(InputStream inputStream,String mineType, String fileName) {
        // 对象相关API的授权器
        ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);

        // 对象操作需要ObjectConfig来配置您的地区和域名后缀
        ObjectConfig config = new ObjectConfig(region, proxySuffix);


        //处理一下重名
        String generateFileName = "";
        String[] fileSplit =  fileName.split("\\.");
        if(fileSplit.length > 1){
            generateFileName = UUID.randomUUID().toString() + fileSplit[fileSplit.length - 1];
        }else{
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_FAIL);
        }

        //putObject点进去看，有个重载方法是可以传流的
        try {
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(inputStream, mineType)
                    .nameAs(generateFileName)
                    .toBucket(bucketName)
                    /**
                     * 是否上传校验MD5, Default = true
                     */
                    //  .withVerifyMd5(false)
                    /**
                     * 指定progress callback的间隔, Default = 每秒回调
                     */
                    //  .withProgressConfig(ProgressConfig.callbackWithPercent(10))
                    /**
                     * 配置进度监听
                     */
                    .setOnProgressListener((bytesWritten, contentLength) -> {

                    })
                    .execute();

                    if(response != null){
                        String url = UfileClient.object(objectAuthorization, config)
                                .getDownloadUrlFromPrivateBucket(generateFileName, bucketName, expiresDuration)
                                /**
                                 * 使用Content-Disposition: attachment，并且默认文件名为KeyName
                                 */
//                    .withAttachment()
                                /**
                                 * 使用Content-Disposition: attachment，并且配置文件名
                                 */
//                    .withAttachment("filename")
                                .createUrl();
                        return url;
                    }else{
                        throw new CustomizeException(CustomizeErrorCode.UPLOAD_FAIL);
                    }
        } catch (UfileClientException e) {

            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_FAIL);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_FAIL);
        }
    }
}
