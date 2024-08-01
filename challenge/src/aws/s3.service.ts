import { PutObjectCommand, S3Client } from '@aws-sdk/client-s3';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner';
import { Injectable } from '@nestjs/common';
import { SpringCloudConfig } from 'src/config/cloud.config';

@Injectable()
export class S3Service {
  private s3Client: S3Client;

  constructor(private springCloudConfig: SpringCloudConfig) {
    this.s3Client = new S3Client({
      region: this.springCloudConfig.get('ncp.region'),
      endpoint: this.springCloudConfig.get('ncp.endPoint'),
      credentials: {
        accessKeyId: this.springCloudConfig.get('ncp.accessKey'),
        secretAccessKey: this.springCloudConfig.get('ncp.secretKey'),
      },
    });
  }

  async uploadVideo(uuid: string) {
    const command = new PutObjectCommand({
      Bucket: this.springCloudConfig.get('ncp.object-storage.videoBucket'),
      Key: uuid + '.mp4',
    });

    const videoPresignedUrl: string = await getSignedUrl(
      this.s3Client,
      command,
      {
        expiresIn: 36000,
      },
    );

    return {
      videoPresignedUrl,
      videoFilename: uuid,
    };
  }

  async uploadThumbnail(filename: string, uuid: string) {
    const fileExtend = this.extractFileExtend(filename).toLowerCase();

    const command = new PutObjectCommand({
      Bucket: this.springCloudConfig.get('ncp.object-storage.thumbnailBucket'),
      Key: uuid + fileExtend,
    });

    const thumbnailPresignedUrl = await getSignedUrl(this.s3Client, command, {
      expiresIn: 36000,
    });
    return {
      thumbnailPresignedUrl: thumbnailPresignedUrl,
      thumbnailFilename: uuid + fileExtend,
    };
  }

  extractFileExtend(filename: string) {
    return filename
      .substring(filename.lastIndexOf('.'), filename.length)
      .toLowerCase();
  }
}
