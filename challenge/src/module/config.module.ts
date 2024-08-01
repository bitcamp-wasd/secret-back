import { Global, Module } from '@nestjs/common';
import { SpringCloudConfig } from 'src/config/cloud.config';
import { configProviders } from 'src/config/cloud.provider';
import { EurekaConfig } from 'src/config/eureka.config';

@Global()
@Module({
  providers: [...configProviders, SpringCloudConfig, EurekaConfig],
  exports: [...configProviders, SpringCloudConfig, EurekaConfig],
})
export class ConfigModule {}
