import { HttpModule } from '@nestjs/axios';
import { Module } from '@nestjs/common';
import { RestApiService } from './api.service';
import { ConfigModule } from 'src/module/config.module';

@Module({
  imports: [HttpModule, ConfigModule],
  providers: [RestApiService],
  exports: [RestApiService],
})
export class RestApiModule {}
