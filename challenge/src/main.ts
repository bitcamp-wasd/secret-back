import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { EurekaConfig } from './config/eureka.config';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const eurekaConfig: EurekaConfig = app.get(EurekaConfig);

  await app.listen(8085);
  eurekaConfig.start();
}
bootstrap();
