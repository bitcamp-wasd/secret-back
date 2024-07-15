import { Module, NestModule } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { SpringCloudConfig } from './config/cloud.config';
import { ConfigModule } from './module/config.module';

import mongoose from 'mongoose';
import { MongoModule } from './module/mongo.module';
import { ChallengeModule } from './challenge/challenge.module';

@Module({
  imports: [ConfigModule, MongoModule, ChallengeModule],
  controllers: [AppController],
  providers: [AppService, SpringCloudConfig],
})
export class AppModule implements NestModule {
  configure(): any {
    mongoose.set('debug', true);
  }
}
