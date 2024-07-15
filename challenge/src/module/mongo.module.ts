import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { SpringCloudConfig } from 'src/config/cloud.config';

@Module({
  imports: [
    MongooseModule.forRootAsync({
      useFactory: async (springCloudConfig: SpringCloudConfig) => ({
        uri: await springCloudConfig.get('mongo.uri'),
      }),
      inject: [SpringCloudConfig],
    }),
  ],
})
export class MongoModule {}
